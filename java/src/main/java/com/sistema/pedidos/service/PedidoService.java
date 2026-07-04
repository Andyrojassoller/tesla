package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.Cliente;
import com.sistema.pedidos.entity.DetallePedido;
import com.sistema.pedidos.entity.Pedido;
import com.sistema.pedidos.entity.Producto;
import com.sistema.pedidos.repository.ClienteRepository;
import com.sistema.pedidos.repository.PedidoRepository;
import com.sistema.pedidos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodos() {
        log.info("Obteniendo todos los pedidos");
        return pedidoRepository.findAllWithCliente();
    }
    
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPorId(Integer id) {
        log.info("Buscando pedido por ID: {}", id);
        return pedidoRepository.findByIdWithFullDetails(id);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorCliente(Integer idCliente) {
        log.info("Obteniendo pedidos del cliente ID: {}", idCliente);
        return pedidoRepository.findByClienteIdCliente(idCliente);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorEstado(String estado) {
        log.info("Obteniendo pedidos con estado: {}", estado);
        return pedidoRepository.findByEstadoWithCliente(estado);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Obteniendo pedidos entre {} y {}", inicio, fin);
        return pedidoRepository.findByFechaPedidoBetween(inicio, fin);
    }
    
    @Transactional
    public Pedido crear(Pedido pedido) {
        log.info("Creando nuevo pedido para cliente ID: {}", pedido.getCliente().getIdCliente());
        
        // Validar cliente
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getIdCliente())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        pedido.setCliente(cliente);
        pedido.setEstado("pendiente");
        
        // Validar y procesar detalles
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getIdProducto()));
            
            // Validar stock
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para producto: " + producto.getNombre());
            }
            
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setPedido(pedido);
            
            // Reducir stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.calcularTotal();
        
        return pedidoRepository.save(pedido);
    }
    
    @Transactional
    public Pedido actualizarEstado(Integer id, String nuevoEstado) {
        log.info("Actualizando estado del pedido ID: {} a {}", id, nuevoEstado);
        return pedidoRepository.findById(id)
            .map(pedido -> {
                String estadoAnterior = pedido.getEstado();
                pedido.setEstado(nuevoEstado);
                
                // Si se cancela, restaurar stock
                if (nuevoEstado.equals("cancelado") && !estadoAnterior.equals("cancelado")) {
                    restaurarStock(pedido);
                }
                
                return pedidoRepository.save(pedido);
            })
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
    
    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando pedido ID: {}", id);
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
        log.info("Restaurando stock de pedido ID: {}", pedido.getIdPedido());
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
    }
}
