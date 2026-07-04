package com.sistema.pedidos.repository;
import com.sistema.pedidos.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByEstado(String estado);
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
}
