package com.sistema.pedidos.repository;

import com.sistema.pedidos.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    
    List<Comentario> findByProductoIdProductoAndActivoTrue(Integer idProducto);
    
    List<Comentario> findByUsuarioIdUsuarioAndActivoTrue(Integer idUsuario);
    
    List<Comentario> findByActivoTrueOrderByFechaCreacionDesc();
    
    @Query("SELECT c FROM Comentario c JOIN FETCH c.usuario WHERE c.producto.idProducto = ?1 AND c.activo = true ORDER BY c.fechaCreacion DESC")
    List<Comentario> obtenerComentariosDeProducto(Integer idProducto);
    
    @Query("SELECT c FROM Comentario c JOIN FETCH c.producto WHERE c.usuario.idUsuario = ?1 AND c.activo = true ORDER BY c.fechaCreacion DESC")
    List<Comentario> obtenerComentariosDeUsuarioConProducto(Integer idUsuario);
    
    @Query("SELECT c FROM Comentario c JOIN FETCH c.producto JOIN FETCH c.usuario WHERE c.activo = true ORDER BY c.fechaCreacion DESC")
    List<Comentario> obtenerTodosConProductoYUsuario();
    
    @Query("SELECT AVG(c.calificacion) FROM Comentario c WHERE c.producto.idProducto = ?1 AND c.activo = true")
    Double obtenerCalificacionPromedio(Integer idProducto);
}
