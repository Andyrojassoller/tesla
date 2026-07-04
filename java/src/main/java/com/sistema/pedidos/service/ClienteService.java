package com.sistema.pedidos.service;

import com.sistema.pedidos.document.ClienteInfo;
import com.sistema.pedidos.entity.Cliente;
import com.sistema.pedidos.repository.ClienteInfoRepository;
import com.sistema.pedidos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final ClienteInfoRepository clienteInfoRepository;
    
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodos() {
        log.info("Obteniendo todos los clientes");
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Integer id) {
        log.info("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorEmail(String email) {
        log.info("Buscando cliente por email: {}", email);
        return clienteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        log.info("Buscando clientes por nombre: {}", nombre);
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional
    public Cliente crear(Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getNombre());
        Cliente nuevoCliente = clienteRepository.save(cliente);
        
        // Crear info en MongoDB
        crearInfoMongoDB(nuevoCliente.getIdCliente());
        
        return nuevoCliente;
    }
    
    @Transactional
    public Cliente actualizar(Integer id, Cliente cliente) {
        log.info("Actualizando cliente ID: {}", id);
        return clienteRepository.findById(id)
            .map(existente -> {
                existente.setNombre(cliente.getNombre());
                existente.setEmail(cliente.getEmail());
                existente.setTelefono(cliente.getTelefono());
                return clienteRepository.save(existente);
            })
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
    
    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando cliente ID: {}", id);
        clienteRepository.deleteById(id);
        clienteInfoRepository.deleteByIdCliente(id);
    }
    
    @Transactional(readOnly = true)
    public long contarTotal() {
        return clienteRepository.countTotalClientes();
    }
    
    private void crearInfoMongoDB(Integer idCliente) {
        ClienteInfo info = new ClienteInfo();
        info.setIdCliente(idCliente);
        
        ClienteInfo.Preferencias prefs = new ClienteInfo.Preferencias();
        prefs.setIdioma("español");
        prefs.setMetodoPago("tarjeta");
        prefs.setNotificaciones(true);
        prefs.setNewsletter(false);
        
        info.setPreferencias(prefs);
        clienteInfoRepository.save(info);
        
        log.info("Info MongoDB creada para cliente ID: {}", idCliente);
    }
}
