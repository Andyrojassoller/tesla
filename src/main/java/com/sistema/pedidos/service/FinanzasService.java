package com.sistema.pedidos.service;

import com.sistema.pedidos.entity.TransaccionFinanciera;
import com.sistema.pedidos.repository.TransaccionFinancieraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FinanzasService {

    private final TransaccionFinancieraRepository repository;

    @Autowired
    public FinanzasService(TransaccionFinancieraRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TransaccionFinanciera> obtenerUltimosMovimientos() {
        return repository.findTop10ByOrderByFechaRegistroDesc();
    }

    @Transactional
    public TransaccionFinanciera registrarMovimiento(TransaccionFinanciera transaccion) {
        return repository.save(transaccion);
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalIngresos() {
        Double ingresos = repository.sumTotalIngresos();
        return ingresos != null ? ingresos : 0.0;
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalEgresos() {
        Double egresos = repository.sumTotalEgresos();
        return egresos != null ? egresos : 0.0;
    }

    @Transactional(readOnly = true)
    public Double obtenerBalanceNeto() {
        return obtenerTotalIngresos() - obtenerTotalEgresos();
    }
}
