package com.sistema.pedidos.controller;

import com.sistema.pedidos.entity.Producto;
import com.sistema.pedidos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    
    private final ProductoService productoService;
    
    @GetMapping
    public String listar(@RequestParam(required = false) String categoria, Model model) {
        List<Producto> productos;
        
        if (categoria != null && !categoria.isEmpty()) {
            productos = productoService.obtenerPorCategoria(categoria);
            model.addAttribute("categoriaActual", categoria);
        } else {
            productos = productoService.obtenerTodos();
        }
        
        List<String> categorias = productoService.obtenerCategorias();
        
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        
        return "productos/lista";
    }
    
    @GetMapping("/categoria/{categoria}")
    public String porCategoria(@PathVariable String categoria, Model model) {
        return listar(categoria, model);
    }
    
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return productoService.obtenerPorId(id)
            .map(producto -> {
                model.addAttribute("producto", producto);
                return "productos/detalle";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/productos";
            });
    }
    
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("accion", "nuevo");
        model.addAttribute("esNuevo", true);
        model.addAttribute("categorias", productoService.obtenerCategorias());
        return "productos/formulario";
    }
    
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return productoService.obtenerPorId(id)
            .map(producto -> {
                model.addAttribute("producto", producto);
                model.addAttribute("accion", "editar");
                model.addAttribute("esNuevo", false);
                model.addAttribute("categorias", productoService.obtenerCategorias());
                return "productos/formulario";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/productos";
            });
    }
    
    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@Valid @ModelAttribute Producto producto, 
                         BindingResult result, 
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("accion", producto.getIdProducto() == null ? "nuevo" : "editar");
            return "productos/formulario";
        }
        
        try {
            if (producto.getIdProducto() == null) {
                productoService.crear(producto);
                redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
            } else {
                productoService.actualizar(producto.getIdProducto(), producto);
                redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            }
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/productos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("accion", producto.getIdProducto() == null ? "nuevo" : "editar");
            return "productos/formulario";
        }
    }
    
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/productos";
    }
}
