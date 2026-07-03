package com.sistema.pedidos.controller;

import com.sistema.pedidos.service.ClienteService;
import com.sistema.pedidos.service.ComentarioService;
import com.sistema.pedidos.service.PedidoService;
import com.sistema.pedidos.service.ProductoService;
import com.sistema.pedidos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final ComentarioService comentarioService;
    
    @GetMapping
    public String dashboard(Model model) {
        // Estadísticas generales
        long totalProductos = productoService.obtenerTodos().size();
        long totalPedidos = pedidoService.obtenerTodos().size();
        long totalClientes = clienteService.obtenerTodos().size();
        long totalUsuarios = usuarioService.obtenerTodos().size();
        long totalComentarios = comentarioService.obtenerTodos().size();
        
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalComentarios", totalComentarios);
        
        return "admin/dashboard";
    }
}
