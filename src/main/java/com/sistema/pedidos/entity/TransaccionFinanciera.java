package com.sistema.pedidos.entity; // Asegúrate de mantener tu package real

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones_financieras")
@Data
public class TransaccionFinanciera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion; // si le pusiste descripcion o description mantén tu variable

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String tipo; // "INGRESO" o "EGRESO"

    @Column(nullable = false)
    private String categoria; // "VENTA", "PROVEEDOR", etc.

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // --- AGREGA ESTOS SETTERS MANUALES AQUÍ ABAJO PARA CORREGIR EL ERROR VISUAL ---

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}