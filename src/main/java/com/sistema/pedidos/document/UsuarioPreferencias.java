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

@Document(collection = "usuarios_preferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPreferencias {
    
    @Id
    private String id;
    
    @Field("id_usuario")
    private Integer idUsuario;
    
    @Field("nombre_usuario")
    private String nombreUsuario;
    
    private String idioma = "es";
    
    @Field("metodo_pago_preferido")
    private String metodoPagoPreferido;
    
    @Field("notificaciones_email")
    private Boolean notificacionesEmail = true;
    
    @Field("notificaciones_sistema")
    private Boolean notificacionesSistema = true;
    
    @Field("categorias_favoritas")
    private List<String> categoriasFavoritas = new ArrayList<>();
    
    @Field("newsletter")
    private Boolean newsletter = false;
    
    @Field("tema_interfaz")
    private String temaInterfaz = "light"; // light, dark
    
    @Field("productos_por_pagina")
    private Integer productosPorPagina = 10;
    
    @Field("fecha_actualizacion")
    private LocalDate fechaActualizacion;
}
