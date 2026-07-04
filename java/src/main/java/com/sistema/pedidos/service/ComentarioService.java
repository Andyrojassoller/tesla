package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.Comentario;
import com.sistema.pedidos.repository.ComentarioRepository;
import com.sistema.pedidos.repository.ProductoRepository;
import com.sistema.pedidos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComentarioService {
    
    private final ComentarioRepository comentarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    
    public List<Comentario> obtenerTodos() {
        return comentarioRepository.obtenerTodosConProductoYUsuario();
    }
    
    public List<Comentario> obtenerPorProducto(Integer idProducto) {
        return comentarioRepository.obtenerComentariosDeProducto(idProducto);
    }
    
    public List<Comentario> obtenerPorUsuario(Integer idUsuario) {
        return comentarioRepository.obtenerComentariosDeUsuarioConProducto(idUsuario);
    }
    
    public Optional<Comentario> obtenerPorId(Integer id) {
        return comentarioRepository.findById(id);
    }
    
    public Double obtenerCalificacionPromedio(Integer idProducto) {
        Double promedio = comentarioRepository.obtenerCalificacionPromedio(idProducto);
        return promedio != null ? promedio : 0.0;
    }
    
    public Comentario crear(Comentario comentario) {
        if (comentario.getProducto() == null || comentario.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("Debe especificar un producto");
        }
        
        if (comentario.getUsuario() == null || comentario.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("Debe especificar un usuario");
        }
        
        if (comentario.getCalificacion() < 1 || comentario.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        
        // Verificar que el producto existe
        productoRepository.findById(comentario.getProducto().getIdProducto())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        // Verificar que el usuario existe
        usuarioRepository.findById(comentario.getUsuario().getIdUsuario())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        comentario.setActivo(true);
        return comentarioRepository.save(comentario);
    }
    
    public Comentario actualizar(Integer id, Comentario comentario) {
        return comentarioRepository.findById(id)
            .map(existente -> {
                existente.setContenido(comentario.getContenido());
                existente.setCalificacion(comentario.getCalificacion());
                return comentarioRepository.save(existente);
            })
            .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
    }
    
    public void eliminar(Integer id) {
        comentarioRepository.findById(id)
            .map(comentario -> {
                comentario.setActivo(false);
                return comentarioRepository.save(comentario);
            })
            .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
    }
}
