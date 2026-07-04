package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.Producto;
import com.sistema.pedidos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        log.info("Obteniendo productos activos");
        return productoRepository.findByActivoTrue();
    }
    
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerPorId(Integer id) {
        log.info("Buscando producto por ID: {}", id);
        return productoRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerPorCategoria(String categoria) {
        log.info("Obteniendo productos de categoría: {}", categoria);
        return productoRepository.findByCategoria(categoria);
    }
    
    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        return productoRepository.findAllCategorias();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> obtenerConBajoStock(Integer cantidad) {
        log.info("Obteniendo productos con stock menor a: {}", cantidad);
        return productoRepository.findByStockLessThan(cantidad);
    }
    
    @Transactional
    public Producto crear(Producto producto) {
        log.info("Creando nuevo producto: {}", producto.getNombre());
        return productoRepository.save(producto);
    }
    
    @Transactional
    public Producto actualizar(Integer id, Producto producto) {
        log.info("Actualizando producto ID: {}", id);
        return productoRepository.findById(id)
            .map(existente -> {
                existente.setNombre(producto.getNombre());
                existente.setDescripcion(producto.getDescripcion());
                existente.setPrecio(producto.getPrecio());
                existente.setStock(producto.getStock());
                existente.setCategoria(producto.getCategoria());
                existente.setActivo(producto.getActivo());
                return productoRepository.save(existente);
            })
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
    
    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando producto ID: {}", id);
        productoRepository.deleteById(id);
    }
    
    @Transactional
    public boolean actualizarStock(Integer id, Integer cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStock(cantidad);
            productoRepository.save(producto);
            log.info("Stock actualizado para producto ID: {}, nuevo stock: {}", id, cantidad);
            return true;
        }
        return false;
    }
    
    @Transactional(readOnly = true)
    public long contarActivos() {
        return productoRepository.countProductosActivos();
    }
}
