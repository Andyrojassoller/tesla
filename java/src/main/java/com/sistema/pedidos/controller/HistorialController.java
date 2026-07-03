package com.sistema.pedidos.controller;

import com.sistema.pedidos.document.HistorialNavegacion;
import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.service.HistorialNavegacionService;
import com.sistema.pedidos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/historial")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class HistorialController {
    
    private final HistorialNavegacionService historialService;
    private final UsuarioService usuarioService;
    
    @GetMapping
    public String verHistorial(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        
        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        List<HistorialNavegacion> historial = historialService.obtenerHistorialUsuario(usuario.getIdUsuario());
        
        model.addAttribute("historial", historial);
        model.addAttribute("usuario", usuario);
        
        return "historial/lista";
    }
}
