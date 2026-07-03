package com.sistema.pedidos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa la información adicional de un cliente en MongoDB
 */
public class ClienteInfo {
    private int idCliente;
    private List<Comentario> comentarios;
    private Preferencias preferencias;
    private List<HistorialNavegacion> historialNavegacion;
    
    // Constructores
    public ClienteInfo() {
        this.comentarios = new ArrayList<>();
        this.historialNavegacion = new ArrayList<>();
    }
    
    public ClienteInfo(int idCliente) {
        this.idCliente = idCliente;
        this.comentarios = new ArrayList<>();
        this.historialNavegacion = new ArrayList<>();
    }
    
    // Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public List<Comentario> getComentarios() {
        return comentarios;
    }
    
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
    
    public void agregarComentario(Comentario comentario) {
        this.comentarios.add(comentario);
    }
    
    public Preferencias getPreferencias() {
        return preferencias;
    }
    
    public void setPreferencias(Preferencias preferencias) {
        this.preferencias = preferencias;
    }
    
    public List<HistorialNavegacion> getHistorialNavegacion() {
        return historialNavegacion;
    }
    
    public void setHistorialNavegacion(List<HistorialNavegacion> historialNavegacion) {
        this.historialNavegacion = historialNavegacion;
    }
    
    @Override
    public String toString() {
        return "ClienteInfo{" +
                "idCliente=" + idCliente +
                ", comentarios=" + comentarios.size() +
                ", preferencias=" + preferencias +
                ", historialNavegacion=" + historialNavegacion.size() +
                '}';
    }
}

/**
 * Clase que representa un comentario de cliente
 */
class Comentario {
    private String texto;
    private LocalDate fecha;
    private Integer productoRelacionado;
    private Integer calificacion;
    
    public Comentario() {}
    
    public Comentario(String texto, LocalDate fecha) {
        this.texto = texto;
        this.fecha = fecha;
    }
    
    // Getters y Setters
    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public Integer getProductoRelacionado() {
        return productoRelacionado;
    }
    
    public void setProductoRelacionado(Integer productoRelacionado) {
        this.productoRelacionado = productoRelacionado;
    }
    
    public Integer getCalificacion() {
        return calificacion;
    }
    
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    
    @Override
    public String toString() {
        return "Comentario{" +
                "texto='" + texto + '\'' +
                ", fecha=" + fecha +
                ", calificacion=" + calificacion +
                '}';
    }
}

/**
 * Clase que representa las preferencias de un cliente
 */
class Preferencias {
    private String idioma;
    private String metodoPago;
    private boolean notificaciones;
    private List<String> categoriasFavoritas;
    private boolean newsletter;
    
    public Preferencias() {
        this.categoriasFavoritas = new ArrayList<>();
    }
    
    // Getters y Setters
    public String getIdioma() {
        return idioma;
    }
    
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public boolean isNotificaciones() {
        return notificaciones;
    }
    
    public void setNotificaciones(boolean notificaciones) {
        this.notificaciones = notificaciones;
    }
    
    public List<String> getCategoriasFavoritas() {
        return categoriasFavoritas;
    }
    
    public void setCategoriasFavoritas(List<String> categoriasFavoritas) {
        this.categoriasFavoritas = categoriasFavoritas;
    }
    
    public boolean isNewsletter() {
        return newsletter;
    }
    
    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }
    
    @Override
    public String toString() {
        return "Preferencias{" +
                "idioma='" + idioma + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", notificaciones=" + notificaciones +
                '}';
    }
}

/**
 * Clase que representa el historial de navegación
 */
class HistorialNavegacion {
    private int idProducto;
    private LocalDate fechaVisita;
    private int tiempoVisualizacion;
    
    public HistorialNavegacion() {}
    
    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public LocalDate getFechaVisita() {
        return fechaVisita;
    }
    
    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
    }
    
    public int getTiempoVisualizacion() {
        return tiempoVisualizacion;
    }
    
    public void setTiempoVisualizacion(int tiempoVisualizacion) {
        this.tiempoVisualizacion = tiempoVisualizacion;
    }
}
