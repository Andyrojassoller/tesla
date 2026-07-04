package com.sistema.pedidos.repository;

import com.sistema.pedidos.document.HistorialNavegacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialNavegacionRepository extends MongoRepository<HistorialNavegacion, String> {
    
    List<HistorialNavegacion> findByIdUsuarioOrderByFechaVisitaDesc(Integer idUsuario);
    
    List<HistorialNavegacion> findByIdProductoOrderByFechaVisitaDesc(Integer idProducto);
    
    List<HistorialNavegacion> findByIdUsuarioAndFechaVisitaBetweenOrderByFechaVisitaDesc(
        Integer idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    Long countByIdUsuarioAndIdProducto(Integer idUsuario, Integer idProducto);
    
    List<HistorialNavegacion> findTop10ByIdUsuarioOrderByFechaVisitaDesc(Integer idUsuario);
}
