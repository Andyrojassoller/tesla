package com.sistema.pedidos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Pedido
 */
public class Pedido {
    private int idPedido;
    private int idCliente;
    private LocalDate fechaPedido;
    private BigDecimal total;
    private String estado;
    private List<DetallePedido> detalles;
    
    // Constructores
    public Pedido() {
        this.detalles = new ArrayList<>();
    }
    
    public Pedido(int idPedido, int idCliente, LocalDate fechaPedido, BigDecimal total, String estado) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }
    
    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public LocalDate getFechaPedido() {
        return fechaPedido;
    }
    
    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public List<DetallePedido> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
    
    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
    }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", idCliente=" + idCliente +
                ", fechaPedido=" + fechaPedido +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", cantidadDetalles=" + detalles.size() +
                '}';
    }
}

/**
 * Clase que representa el detalle de un pedido
 */
class DetallePedido {
    private int idDetalle;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private BigDecimal subtotal;
    
    public DetallePedido() {}
    
    public DetallePedido(int idDetalle, int idPedido, int idProducto, int cantidad, BigDecimal subtotal) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    
    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }
    
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public int getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    
    public int getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    @Override
    public String toString() {
        return "DetallePedido{" +
                "idDetalle=" + idDetalle +
                ", idPedido=" + idPedido +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}
