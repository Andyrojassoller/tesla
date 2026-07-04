package com.sistema.pedidos.controller;
import com.sistema.pedidos.entity.TransaccionFinanciera;
import com.sistema.pedidos.service.FinanzasService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/finanzas")
public class FinanzasController {
    private final FinanzasService finanzasService;

    @Autowired
    public FinanzasController(FinanzasService finanzasService) {
        this.finanzasService = finanzasService;
    }

    @GetMapping
    public String verFinanzas(Model model) {
        model.addAttribute("titulo", "Gestión de Finanzas");
        model.addAttribute("transacciones", finanzasService.obtenerUltimosMovimientos());
        model.addAttribute("totalIngresos", finanzasService.obtenerTotalIngresos());
        model.addAttribute("totalEgresos", finanzasService.obtenerTotalEgresos());
        model.addAttribute("balanceNeto", finanzasService.obtenerBalanceNeto());
        model.addAttribute("nuevaTransaccion", new TransaccionFinanciera());
        return "finanzas/panel"; 
    }

    @PostMapping("/registrar")
    public String registrarTransaccion(@ModelAttribute("nuevaTransaccion") TransaccionFinanciera transaccion) {
        finanzasService.registrarMovimiento(transaccion);
        return "redirect:/finanzas?exito=true";
    }
}
