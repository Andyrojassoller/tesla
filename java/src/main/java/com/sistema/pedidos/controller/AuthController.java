package com.sistema.pedidos.controller;

import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UsuarioService usuarioService;
    
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(required = false) String error,
                              @RequestParam(required = false) String logout,
                              Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada correctamente");
        }
        return "auth/login";
    }
    
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        log.info("Mostrando formulario de registro");
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        return "auth/registro";
    }
    
    @PostMapping("/registro")
    public String registrar(@RequestParam("nombreUsuario") String nombreUsuario,
                           @RequestParam("contrasena") String contrasena,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        log.info("Intento de registro - Usuario: {}", nombreUsuario);
        
        // Validar que el nombre de usuario no esté vacío
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            log.error("Nombre de usuario vacío");
            model.addAttribute("error", "El nombre de usuario es obligatorio");
            return "auth/registro";
        }
        
        // Validar que la contraseña no esté vacía
        if (contrasena == null || contrasena.isEmpty()) {
            log.error("Contraseña vacía");
            model.addAttribute("error", "La contraseña es obligatoria");
            return "auth/registro";
        }
        
        // Validar longitud mínima de contraseña
        if (contrasena.length() < 6) {
            log.error("Contraseña muy corta");
            model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "auth/registro";
        }
        
        // Verificar si el usuario ya existe
        if (usuarioService.obtenerPorNombreUsuario(nombreUsuario).isPresent()) {
            log.warn("Usuario ya existe: {}", nombreUsuario);
            model.addAttribute("error", "El nombre de usuario '" + nombreUsuario + "' ya está en uso");
            return "auth/registro";
        }
        
        try {
            log.info("Creando nuevo usuario: {}", nombreUsuario);
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setContrasena(contrasena);
            usuario.setRol("CLIENTE");
            usuario.setActivo(true);
            
            usuarioService.crear(usuario);
            log.info("Usuario creado exitosamente: {}", nombreUsuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente. Por favor inicie sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "auth/registro";
        }
    }
    
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "auth/acceso-denegado";
    }
    
    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        
        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("usuario", usuario);
        return "auth/perfil";
    }
}
