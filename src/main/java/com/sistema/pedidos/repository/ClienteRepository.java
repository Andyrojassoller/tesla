package com.sistema.pedidos.repository;

import com.sistema.pedidos.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    Optional<Cliente> findByEmail(String email);
    
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.pedidos WHERE c.idCliente = :id")
    Optional<Cliente> findByIdWithPedidos(@Param("id") Integer id);
    
    @Query("SELECT COUNT(c) FROM Cliente c")
    long countTotalClientes();
}
