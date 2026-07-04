package com.sistema.pedidos.controller;

import com.sistema.pedidos.entity.Comentario;
import com.sistema.pedidos.entity.Producto;
import com.sistema.pedidos.entity.Usuario;
import com.sistema.pedidos.service.ComentarioService;
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
@RequestMapping("/comentarios")
@RequiredArgsConstructor
public class ComentarioController {
    
    private final ComentarioService comentarioService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listar(Model model) {
        List<Comentario> comentarios = comentarioService.obtenerTodos();
        model.addAttribute("comentarios", comentarios);
        return "comentarios/lista";
    }
    
    @GetMapping("/producto/{idProducto}")
    public String porProducto(@PathVariable Integer idProducto, Model model, RedirectAttributes redirectAttributes) {
        return productoService.obtenerPorId(idProducto)
            .map(producto -> {
                List<Comentario> comentarios = comentarioService.obtenerPorProducto(idProducto);
                Double calificacionPromedio = comentarioService.obtenerCalificacionPromedio(idProducto);
                
                model.addAttribute("producto", producto);
                model.addAttribute("comentarios", comentarios);
                model.addAttribute("calificacionPromedio", calificacionPromedio);
                model.addAttribute("nuevoComentario", new Comentario());
                return "comentarios/producto";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/productos";
            });
    }
    
    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public String agregar(@RequestParam Integer idProducto,
                         @RequestParam String contenido,
                         @RequestParam Integer calificacion,
                         RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String nombreUsuario = auth.getName();
            
            Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            Producto producto = productoService.obtenerPorId(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            
            Comentario comentario = new Comentario();
            comentario.setProducto(producto);
            comentario.setUsuario(usuario);
            comentario.setContenido(contenido);
            comentario.setCalificacion(calificacion);
            
            comentarioService.crear(comentario);
            
            redirectAttributes.addFlashAttribute("mensaje", "Comentario agregado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        return "redirect:/comentarios/producto/" + idProducto;
    }
    
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Integer id, 
                          @RequestParam(required = false) Integer idProducto,
                          RedirectAttributes redirectAttributes) {
        try {
            comentarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Comentario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        if (idProducto != null) {
            return "redirect:/comentarios/producto/" + idProducto;
        }
        return "redirect:/comentarios";
    }
    
    @GetMapping("/mis-comentarios")
    @PreAuthorize("isAuthenticated()")
    public String misComentarios(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        
        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        List<Comentario> comentarios = comentarioService.obtenerPorUsuario(usuario.getIdUsuario());
        model.addAttribute("comentarios", comentarios);
        return "comentarios/mis-comentarios";
    }
}
