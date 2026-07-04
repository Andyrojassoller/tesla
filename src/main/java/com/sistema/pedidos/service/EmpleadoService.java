package com.sistema.pedidos.service;
import com.sistema.pedidos.model.Empleado;
import com.sistema.pedidos.entity.TransaccionFinanciera;
import com.sistema.pedidos.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class EmpleadoService {
    private static final Logger log = Logger.getLogger(EmpleadoService.class.getName());

    private final EmpleadoRepository empleadoRepository;
    private final FinanzasService finanzasService; // Inyectamos finanzas para la automatización

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository, FinanzasService finanzasService) {
        this.empleadoRepository = empleadoRepository;
        this.finanzasService = finanzasService;
    }

    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodos() {
        log.info("Obteniendo la lista de todos los empleados");
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Empleado> obtenerPorId(Integer id) {
        log.info("Buscando empleado con ID: " + id);
        return empleadoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Empleado> obtenerPorEstado(String estado) {
        log.info("Filtrando empleados por estado: " + estado);
        return empleadoRepository.findByEstado(estado);
    }

    @Transactional
    public Empleado guardar(Empleado empleado) {
        log.info("Guardando/actualizando datos del empleado: " + empleado.getNombre());
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando empleado con ID: " + id);
        empleadoRepository.deleteById(id);
    }

    // --- AQUÍ ESTÁ LA INTEGRACIÓN AUTOMÁTICA CON FINANZAS ---
    @Transactional
    public void registrarPagoPlanilla(Integer idEmpleado) {
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));

        if (!"ACTIVO".equalsIgnoreCase(empleado.getEstado())) {
            throw new RuntimeException("No se puede pagar la planilla a un empleado inactivo o de licencia.");
        }

        log.info("Procesando pago de planilla automático para: " + empleado.getNombre() + " " + empleado.getApellido());

        // Creamos el egreso financiero de forma automática
        TransaccionFinanciera egreso = new TransaccionFinanciera();
        egreso.setDescripcion("Pago de Planilla - " + empleado.getCargo() + ": " + empleado.getNombre() + " " + empleado.getApellido());
        
        // Convertimos el BigDecimal del sueldo a Double para el módulo contable
        if (empleado.getSueldo() != null) {
            egreso.setMonto(empleado.getSueldo().doubleValue());
        } else {
            egreso.setMonto(0.0);
        }

        egreso.setTipo("EGRESO");
        egreso.setCategoria("PLANILLA");

        // Disparamos el registro en el módulo de finanzas
        finanzasService.registrarMovimiento(egreso);
    }
}
