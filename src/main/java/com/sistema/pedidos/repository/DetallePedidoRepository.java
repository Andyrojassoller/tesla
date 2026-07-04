package com.sistema.pedidos.repository;

import com.sistema.pedidos.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    
    List<DetallePedido> findByPedidoIdPedido(Integer idPedido);
    
    List<DetallePedido> findByProductoIdProducto(Integer idProducto);
    
    @Query("SELECT d FROM DetallePedido d WHERE d.producto.idProducto = :idProducto")
    List<DetallePedido> findDetallesByProducto(@Param("idProducto") Integer idProducto);
}
