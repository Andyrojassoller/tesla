package com.sistema.pedidos.controller;

import com.sistema.pedidos.entity.Pedido;
import com.sistema.pedidos.service.ClienteService;
import com.sistema.pedidos.service.PedidoService;
import com.sistema.pedidos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    
    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    
    @GetMapping
    public String listar(@RequestParam(required = false) String estado, 
                        @RequestParam(required = false) String mensaje,
                        Model model) {
        List<Pedido> pedidos;
        if (estado != null && !estado.isEmpty()) {
            pedidos = pedidoService.obtenerPorEstado(estado);
            model.addAttribute("estadoActual", estado);
        } else {
            pedidos = pedidoService.obtenerTodos();
        }
        
        if (mensaje != null) {
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("tipoMensaje", "success");
        }
        
        model.addAttribute("pedidos", pedidos);
        return "pedidos/lista";
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return pedidoService.obtenerPorId(id)
            .map(pedido -> {
                model.addAttribute("pedido", pedido);
                return "pedidos/detalle";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Pedido no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/pedidos";
            });
    }
    
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public String mostrarFormulario(Model model, org.springframework.security.core.Authentication authentication) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("productos", productoService.obtenerActivos());
        
        // Determinar si el usuario es ADMIN
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (esAdmin) {
            // ADMIN puede escoger cualquier cliente
            model.addAttribute("clientes", clienteService.obtenerTodos());
            model.addAttribute("esAdmin", true);
        } else {
            // CLIENTE: buscar su cliente asociado por email (nombreUsuario)
            String nombreUsuario = authentication.getName();
            String emailCliente = nombreUsuario.contains("@") ? nombreUsuario : nombreUsuario + "@cliente.local";
            
            clienteService.obtenerPorEmail(emailCliente).or(() -> {
                // Si no existe, crear el cliente automáticamente
                com.sistema.pedidos.entity.Cliente nuevoCliente = new com.sistema.pedidos.entity.Cliente();
                nuevoCliente.setNombre(nombreUsuario);
                nuevoCliente.setEmail(emailCliente);
                nuevoCliente.setTelefono("Sin teléfono");
                return java.util.Optional.of(clienteService.crear(nuevoCliente));
            }).ifPresent(cliente -> {
                model.addAttribute("clienteLogueado", cliente);
            });
            model.addAttribute("esAdmin", false);
        }
        
        return "pedidos/formulario";
    }
    
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> guardar(@RequestBody Pedido pedido) {
        try {
            pedidoService.crear(pedido);
            return org.springframework.http.ResponseEntity.ok("OK");
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/actualizar-estado/{id}")
    public String actualizarEstado(@PathVariable Integer id, 
                                  @RequestParam String estado, 
                                  RedirectAttributes redirectAttributes) {
        try {
            pedidoService.actualizarEstado(id, estado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/pedidos/" + id;
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Pedido eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/pedidos";
    }
    
    @GetMapping("/cliente/{idCliente}")
    public String porCliente(@PathVariable Integer idCliente, Model model, RedirectAttributes redirectAttributes) {
        return clienteService.obtenerPorId(idCliente)
            .map(cliente -> {
                List<Pedido> pedidos = pedidoService.obtenerPorCliente(idCliente);
                model.addAttribute("pedidos", pedidos);
                model.addAttribute("cliente", cliente);
                return "pedidos/lista";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/pedidos";
            });
    }
}
