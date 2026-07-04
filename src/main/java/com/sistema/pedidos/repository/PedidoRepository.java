package com.sistema.pedidos.repository;

import com.sistema.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.cliente.idCliente = :idCliente")
    List<Pedido> findByClienteIdCliente(@Param("idCliente") Integer idCliente);
    
    List<Pedido> findByEstado(String estado);
    
    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente")
    List<Pedido> findAllWithCliente();
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.estado = :estado")
    List<Pedido> findByEstadoWithCliente(@Param("estado") String estado);
    
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles WHERE p.idPedido = :id")
    Optional<Pedido> findByIdWithDetalles(@Param("id") Integer id);
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto WHERE p.idPedido = :id")
    Optional<Pedido> findByIdWithFullDetails(@Param("id") Integer id);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    long countByEstado(@Param("estado") String estado);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.estado = 'completado'")
    Double getTotalVentas();
}
