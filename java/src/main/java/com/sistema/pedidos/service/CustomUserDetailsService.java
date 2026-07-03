package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        log.info("Intentando cargar usuario: {}", nombreUsuario);
        
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> {
                log.error("Usuario no encontrado: {}", nombreUsuario);
                return new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario);
            });
        
        log.info("Usuario encontrado: {}, rol: {}, activo: {}", usuario.getNombreUsuario(), usuario.getRol(), usuario.getActivo());
        
        if (!usuario.getActivo()) {
            log.error("Usuario inactivo: {}", nombreUsuario);
            throw new UsernameNotFoundException("Usuario inactivo: " + nombreUsuario);
        }
        
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase())
        );
        
        log.info("Autoridades asignadas: {}", authorities);
        
        return new User(
            usuario.getNombreUsuario(),
            usuario.getContrasena(),
            usuario.getActivo(),
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities
        );
    }
}
