package com.sistema.pedidos.repository;
import com.sistema.pedidos.entity.TransaccionFinanciera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransaccionFinancieraRepository extends JpaRepository<TransaccionFinanciera, Long>{
    List<TransaccionFinanciera> findTop10ByOrderByFechaRegistroDesc();

    @Query("SELECT SUM(t.monto) FROM TransaccionFinanciera t WHERE t.tipo = 'INGRESO'")
    Double sumTotalIngresos();

    @Query("SELECT SUM(t.monto) FROM TransaccionFinanciera t WHERE t.tipo = 'EGRESO'")
    Double sumTotalEgresos();
    
}
