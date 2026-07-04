package com.sistema.pedidos.controller;
import com.sistema.pedidos.model.Empleado;
import com.sistema.pedidos.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // Listar todos los empleados
    @GetMapping
    public String listarEmpleados(Model model) {
        List<Empleado> lista = empleadoService.obtenerTodos();
        model.addAttribute("empleados", lista);
        model.addAttribute("empleadoNuevo", new Empleado()); // Objeto vacío para el modal de creación
        return "empleados"; 
    }

    // Registrar un nuevo empleado o actualizar uno existente
    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleadoNuevo") Empleado empleado, 
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("empleados", empleadoService.obtenerTodos());
            model.addAttribute("org_springframework_validation_BindingResult_empleadoNuevo", result);
            return "empleados";
        }
        
        empleadoService.guardar(empleado);
        return "redirect:/empleados?success=guardado";
    }

    // Cargar datos de un empleado para edición vía JSON (para usarlo cómodamente con JavaScript en el modal)
    @GetMapping("/api/{id}")
    @ResponseBody
    public Empleado obtenerEmpleadoJson(@PathVariable("id") Integer id) {
        return empleadoService.obtenerPorId(id).orElse(new Empleado());
    }

    // Registrar el pago automático de la planilla hacia el módulo de finanzas
    @PostMapping("/pagar/{id}")
    public String pagarPlanilla(@PathVariable("id") Integer id) {
        try {
            empleadoService.registrarPagoPlanilla(id);
            return "redirect:/empleados?success=pago_exitoso";
        } catch (Exception e) {
            return "redirect:/empleados?error=pago_fallido";
        }
    }

    // Eliminar un empleado físicamente del sistema
    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable("id") Integer id) {
        empleadoService.eliminar(id);
        return "redirect:/empleados?success=eliminado";
    }
}
