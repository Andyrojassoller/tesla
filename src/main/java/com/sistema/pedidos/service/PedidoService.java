package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.Cliente;
import com.sistema.pedidos.entity.DetallePedido;
import com.sistema.pedidos.entity.Pedido;
import com.sistema.pedidos.entity.Producto;
import com.sistema.pedidos.entity.TransaccionFinanciera;
import com.sistema.pedidos.repository.ClienteRepository;
import com.sistema.pedidos.repository.PedidoRepository;
import com.sistema.pedidos.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger; // Logger nativo de Java puro

@Service
public class PedidoService {
    
    // Logger nativo sin Lombok para evitar líneas rojas
    private static final Logger log = Logger.getLogger(PedidoService.class.getName());
    
    private final PedidoRepository pedidoRepository;
    private final FinanzasService finanzasService; 
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, 
                         FinanzasService finanzasService,
                         ClienteRepository clienteRepository,
                         ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.finanzasService = finanzasService;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodos() {
        log.info("Obteniendo todos los pedidos");
        return pedidoRepository.findAllWithCliente();
    }
    
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPorId(Integer id) {
        log.info("Buscando pedido por ID: " + id);
        return pedidoRepository.findByIdWithFullDetails(id);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorCliente(Integer idCliente) {
        log.info("Obteniendo pedidos del cliente ID: " + idCliente);
        return pedidoRepository.findByClienteIdCliente(idCliente);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorEstado(String estado) {
        log.info("Obteniendo pedidos con estado: " + estado);
        return pedidoRepository.findByEstadoWithCliente(estado);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Obteniendo pedidos entre " + inicio + " y " + fin);
        return pedidoRepository.findByFechaPedidoBetween(inicio, fin);
    }
    
    @Transactional
    public Pedido crear(Pedido pedido) {
        log.info("Creando nuevo pedido para cliente ID: " + pedido.getCliente().getIdCliente());
        
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getIdCliente())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        pedido.setCliente(cliente);
        pedido.setEstado("pendiente");
        
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getIdProducto()));
            
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para producto: " + producto.getNombre());
            }
            
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setPedido(pedido);
            
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }
    
    @Transactional
    public Pedido actualizarEstado(Integer id, String nuevoEstado) {
        log.info("Actualizando estado del pedido ID: " + id + " a " + nuevoEstado);
        return pedidoRepository.findById(id)
            .map(pedido -> {
                String estadoAnterior = pedido.getEstado();
                pedido.setEstado(nuevoEstado);
                
                // Si cambia a cancelado, restaurar stock
                if (nuevoEstado.equals("cancelado") && !estadoAnterior.equals("cancelado")) {
                    restaurarStock(pedido);
                }
                
                // INTEGRACIÓN FINANCIERA AUTOMÁTICA
                if ("Completado".equalsIgnoreCase(nuevoEstado) && !"Completado".equalsIgnoreCase(estadoAnterior)) {
                    TransaccionFinanciera ingreso = new TransaccionFinanciera();
                    ingreso.setDescripcion("Ingreso automático - Pedido #" + pedido.getIdPedido());
                    
                    // Convertimos el BigDecimal de la orden a Double para el módulo financiero
                    if (pedido.getTotal() != null) {
                        ingreso.setMonto(pedido.getTotal().doubleValue());
                    } else {
                        ingreso.setMonto(0.0);
                    }
                    
                    ingreso.setTipo("INGRESO");
                    ingreso.setCategoria("VENTA");
                    
                    finanzasService.registrarMovimiento(ingreso);
                }
                
                return pedidoRepository.save(pedido);
            })
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
    
    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando pedido ID: " + id);
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            restaurarStock(pedido.get());
            pedidoRepository.deleteById(id);
        }
    }
    
    @Transactional(readOnly = true)
    public long contarPorEstado(String estado) {
        return pedidoRepository.countByEstado(estado);
    }
    
    @Transactional(readOnly = true)
    public Double obtenerTotalVentas() {
        Double total = pedidoRepository.getTotalVentas();
        return total != null ? total : 0.0;
    }
    
    private void restaurarStock(Pedido pedido) {
        log.info("Restaurando stock de pedido ID: " + pedido.getIdPedido());
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
    }
}
