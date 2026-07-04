package com.sistema.pedidos.controller;

import com.sistema.pedidos.entity.Cliente;
import com.sistema.pedidos.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.obtenerTodos();
        model.addAttribute("clientes", clientes);
        return "clientes/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("esNuevo", true);
        return "clientes/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Cliente cliente, 
                         BindingResult result, 
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/formulario";
        }
        
        try {
            if (cliente.getIdCliente() == null) {
                clienteService.crear(cliente);
                redirectAttributes.addFlashAttribute("mensaje", "Cliente creado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                clienteService.actualizar(cliente.getIdCliente(), cliente);
                redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        return "redirect:/clientes";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return clienteService.obtenerPorId(id)
            .map(cliente -> {
                model.addAttribute("cliente", cliente);
                model.addAttribute("esNuevo", false);
                return "clientes/formulario";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/clientes";
            });
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/clientes";
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam String nombre, Model model) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        model.addAttribute("clientes", clientes);
        model.addAttribute("busqueda", nombre);
        return "clientes/lista";
    }
}
