package com.sistema.pedidos.controller;

import com.sistema.pedidos.service.ClienteService;
import com.sistema.pedidos.service.PedidoService;
import com.sistema.pedidos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/dashboard")
    public String index(Model model) {
        try {
            // Estadísticas para el dashboard
            long totalClientes = clienteService.contarTotal();
            long totalProductos = productoService.contarActivos();
            long pedidosPendientes = pedidoService.contarPorEstado("pendiente");
            double totalVentas = pedidoService.obtenerTotalVentas();
            
            model.addAttribute("totalClientes", totalClientes);
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("pedidosPendientes", pedidosPendientes);
            model.addAttribute("totalVentas", totalVentas);
        } catch (Exception e) {
            // Si hay error, inicializar con valores por defecto
            model.addAttribute("totalClientes", 0L);
            model.addAttribute("totalProductos", 0L);
            model.addAttribute("pedidosPendientes", 0L);
            model.addAttribute("totalVentas", 0.0);
        }
        
        return "index";
    } 
        
}
