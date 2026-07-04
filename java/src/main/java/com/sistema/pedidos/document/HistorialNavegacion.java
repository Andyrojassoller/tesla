package com.sistema.pedidos.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "historial_navegacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialNavegacion {
    
    @Id
    private String id;
    
    @Field("id_usuario")
    private Integer idUsuario;
    
    @Field("nombre_usuario")
    private String nombreUsuario;
    
    @Field("id_producto")
    private Integer idProducto;
    
    @Field("nombre_producto")
    private String nombreProducto;
    
    @Field("categoria_producto")
    private String categoriaProducto;
    
    @Field("fecha_visita")
    private LocalDateTime fechaVisita;
    
    @Field("ip_address")
    private String ipAddress;
    
    @Field("user_agent")
    private String userAgent;
}
