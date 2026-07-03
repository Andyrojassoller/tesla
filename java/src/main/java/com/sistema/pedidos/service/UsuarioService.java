package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario) {
        log.info("Buscando usuario: {}", nombreUsuario);
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> obtenerActivos() {
        return usuarioRepository.findByActivoTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    @Transactional
    public Usuario crear(Usuario usuario) {
        log.info("Creando usuario: {}", usuario.getNombreUsuario());
        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario actualizar(Integer id, Usuario usuario) {
        log.info("Actualizando usuario ID: {}", id);
        return usuarioRepository.findById(id)
            .map(existente -> {
                existente.setNombreUsuario(usuario.getNombreUsuario());
                if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                    existente.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
                }
                existente.setRol(usuario.getRol());
                existente.setActivo(usuario.getActivo());
                return usuarioRepository.save(existente);
            })
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando usuario ID: {}", id);
        usuarioRepository.deleteById(id);
    }
    
    public boolean validarCredenciales(String nombreUsuario, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        return usuario.isPresent() && 
               usuario.get().getActivo() && 
               passwordEncoder.matches(contrasena, usuario.get().getContrasena());
    }
}
