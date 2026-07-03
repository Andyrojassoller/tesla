package com.sistema.pedidos.repository;

import com.sistema.pedidos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
    List<Usuario> findByActivoTrue();
    
    List<Usuario> findByRol(String rol);
}
