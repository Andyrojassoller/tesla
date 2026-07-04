package com.sistema.pedidos.service;

import com.sistema.pedidos.document.UsuarioPreferencias;
import com.sistema.pedidos.repository.UsuarioPreferenciasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenciasService {
    
    private final UsuarioPreferenciasRepository preferenciasRepository;
    
    public Optional<UsuarioPreferencias> obtenerPorUsuario(Integer idUsuario) {
        return preferenciasRepository.findByIdUsuario(idUsuario);
    }
    
    public Optional<UsuarioPreferencias> obtenerPorNombreUsuario(String nombreUsuario) {
        return preferenciasRepository.findByNombreUsuario(nombreUsuario);
    }
    
    public UsuarioPreferencias crearPreferenciasDefecto(Integer idUsuario, String nombreUsuario) {
        UsuarioPreferencias preferencias = new UsuarioPreferencias();
        preferencias.setIdUsuario(idUsuario);
        preferencias.setNombreUsuario(nombreUsuario);
        preferencias.setFechaActualizacion(LocalDate.now());
        return preferenciasRepository.save(preferencias);
    }
    
    public UsuarioPreferencias actualizar(UsuarioPreferencias preferencias) {
        preferencias.setFechaActualizacion(LocalDate.now());
        return preferenciasRepository.save(preferencias);
    }
    
    public UsuarioPreferencias obtenerOCrear(Integer idUsuario, String nombreUsuario) {
        return obtenerPorUsuario(idUsuario)
            .orElseGet(() -> crearPreferenciasDefecto(idUsuario, nombreUsuario));
    }
}
