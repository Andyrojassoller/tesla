package com.sistema.pedidos.controller;
import com.sistema.pedidos.service.PedidoService;
import com.sistema.pedidos.service.FinanzasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReportesController {
    private final PedidoService pedidoService;
    private final FinanzasService finanzasService;

    @Autowired
    public ReportesController(PedidoService pedidoService, FinanzasService finanzasService) {
        this.pedidoService = pedidoService;
        this.finanzasService = finanzasService;
    }

    @GetMapping
    public String verReportes(Model model) {
        model.addAttribute("titulo", "Reportes y Estadísticas");
        
        // Datos de Pedidos
        model.addAttribute("pedidosPendientes", pedidoService.contarPorEstado("pendiente"));
        model.addAttribute("pedidosProcesando", pedidoService.contarPorEstado("procesando"));
        model.addAttribute("pedidosCompletados", pedidoService.contarPorEstado("Completado"));
        model.addAttribute("pedidosCancelados", pedidoService.contarPorEstado("cancelado"));
        
        // Datos Financieros
        model.addAttribute("totalIngresos", finanzasService.obtenerTotalIngresos());
        model.addAttribute("totalEgresos", finanzasService.obtenerTotalEgresos());
        model.addAttribute("balanceNeto", finanzasService.obtenerBalanceNeto());
        
        return "reportes/dashboard";
    }
}
