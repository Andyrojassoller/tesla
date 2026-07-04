package com.sistema.pedidos.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "clientes_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteInfo {
    
    @Id
    private String id;
    
    @Field("id_cliente")
    private Integer idCliente;
    
    private List<Comentario> comentarios = new ArrayList<>();
    
    private Preferencias preferencias;
    
    @Field("historial_navegacion")
    private List<HistorialNavegacion> historialNavegacion = new ArrayList<>();
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comentario {
        private String texto;
        private LocalDate fecha;
        
        @Field("producto_relacionado")
        private Integer productoRelacionado;
        
        private Integer calificacion;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Preferencias {
        private String idioma;
        
        @Field("metodo_pago")
        private String metodoPago;
        
        private Boolean notificaciones;
        
        @Field("categorias_favoritas")
        private List<String> categoriasFavoritas = new ArrayList<>();
        
        private Boolean newsletter;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistorialNavegacion {
        @Field("id_producto")
        private Integer idProducto;
        
        @Field("fecha_visita")
        private LocalDate fechaVisita;
        
        @Field("tiempo_visualizacion")
        private Integer tiempoVisualizacion;
    }
}
