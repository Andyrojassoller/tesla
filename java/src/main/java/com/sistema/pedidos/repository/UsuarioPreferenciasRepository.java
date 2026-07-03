package com.sistema.pedidos.repository;

import com.sistema.pedidos.document.UsuarioPreferencias;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioPreferenciasRepository extends MongoRepository<UsuarioPreferencias, String> {
    
    Optional<UsuarioPreferencias> findByIdUsuario(Integer idUsuario);
    
    Optional<UsuarioPreferencias> findByNombreUsuario(String nombreUsuario);
    
    void deleteByIdUsuario(Integer idUsuario);
}
