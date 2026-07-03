package com.sistema.pedidos.controller;

import com.sistema.pedidos.document.UsuarioPreferencias;
import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.service.PreferenciasService;
import com.sistema.pedidos.service.ProductoService;
import com.sistema.pedidos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/preferencias")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PreferenciasController {
    
    private final PreferenciasService preferenciasService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    
    @GetMapping
    public String verPreferencias(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        
        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        UsuarioPreferencias preferencias = preferenciasService.obtenerOCrear(
            usuario.getIdUsuario(), nombreUsuario);
        
        model.addAttribute("preferencias", preferencias);
        model.addAttribute("categorias", productoService.obtenerCategorias());
        
        return "preferencias/editar";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute UsuarioPreferencias preferencias,
                         @RequestParam(required = false) List<String> categoriasFavoritas,
                         RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String nombreUsuario = auth.getName();
            
            Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            UsuarioPreferencias preferenciaExistente = preferenciasService.obtenerOCrear(
                usuario.getIdUsuario(), nombreUsuario);
            
            // Actualizar campos
            preferenciaExistente.setIdioma(preferencias.getIdioma());
            preferenciaExistente.setMetodoPagoPreferido(preferencias.getMetodoPagoPreferido());
            preferenciaExistente.setNotificacionesEmail(preferencias.getNotificacionesEmail());
            preferenciaExistente.setNotificacionesSistema(preferencias.getNotificacionesSistema());
            preferenciaExistente.setNewsletter(preferencias.getNewsletter());
            preferenciaExistente.setTemaInterfaz(preferencias.getTemaInterfaz());
            preferenciaExistente.setProductosPorPagina(preferencias.getProductosPorPagina());
            
            if (categoriasFavoritas != null) {
                preferenciaExistente.setCategoriasFavoritas(categoriasFavoritas);
            }
            
            preferenciasService.actualizar(preferenciaExistente);
            
            redirectAttributes.addFlashAttribute("mensaje", "Preferencias actualizadas exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        return "redirect:/preferencias";
    }
}
