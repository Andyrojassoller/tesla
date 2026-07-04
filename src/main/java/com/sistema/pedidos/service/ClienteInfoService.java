package com.sistema.pedidos.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.sistema.pedidos.db.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servicio para gestionar información de clientes en MongoDB
 */
public class ClienteInfoService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClienteInfoService.class);
    
    /**
     * Obtiene la información de un cliente por su ID
     */
    public Document obtenerClienteInfo(int idCliente) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            Document clienteInfo = collection.find(Filters.eq("id_cliente", idCliente)).first();
            
            if (clienteInfo != null) {
                logger.info("Información encontrada para cliente ID: {}", idCliente);
            } else {
                logger.warn("No se encontró información para cliente ID: {}", idCliente);
            }
            
            return clienteInfo;
        } catch (Exception e) {
            logger.error("Error al obtener información del cliente", e);
            return null;
        }
    }
    
    /**
     * Inserta información de un nuevo cliente
     */
    public boolean insertarClienteInfo(int idCliente, String idioma, String metodoPago, boolean notificaciones) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            
            // Crear documento de preferencias
            Document preferencias = new Document()
                .append("idioma", idioma)
                .append("metodo_pago", metodoPago)
                .append("notificaciones", notificaciones)
                .append("categorias_favoritas", new ArrayList<>())
                .append("newsletter", false);
            
            // Crear documento principal
            Document clienteInfo = new Document()
                .append("id_cliente", idCliente)
                .append("comentarios", new ArrayList<>())
                .append("preferencias", preferencias)
                .append("historial_navegacion", new ArrayList<>());
            
            collection.insertOne(clienteInfo);
            logger.info("Información insertada para cliente ID: {}", idCliente);
            return true;
        } catch (Exception e) {
            logger.error("Error al insertar información del cliente", e);
            return false;
        }
    }
    
    /**
     * Agrega un comentario a un cliente
     */
    public boolean agregarComentario(int idCliente, String texto, Integer productoRelacionado, Integer calificacion) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            
            Document comentario = new Document()
                .append("texto", texto)
                .append("fecha", Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            
            if (productoRelacionado != null) {
                comentario.append("producto_relacionado", productoRelacionado);
            }
            if (calificacion != null) {
                comentario.append("calificacion", calificacion);
            }
            
            Bson filter = Filters.eq("id_cliente", idCliente);
            Bson update = Updates.push("comentarios", comentario);
            
            collection.updateOne(filter, update);
            logger.info("Comentario agregado para cliente ID: {}", idCliente);
            return true;
        } catch (Exception e) {
            logger.error("Error al agregar comentario", e);
            return false;
        }
    }
    
    /**
     * Actualiza las preferencias de un cliente
     */
    public boolean actualizarPreferencias(int idCliente, String idioma, String metodoPago, 
                                        boolean notificaciones, boolean newsletter) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            
            Bson filter = Filters.eq("id_cliente", idCliente);
            Bson update = Updates.combine(
                Updates.set("preferencias.idioma", idioma),
                Updates.set("preferencias.metodo_pago", metodoPago),
                Updates.set("preferencias.notificaciones", notificaciones),
                Updates.set("preferencias.newsletter", newsletter)
            );
            
            collection.updateOne(filter, update);
            logger.info("Preferencias actualizadas para cliente ID: {}", idCliente);
            return true;
        } catch (Exception e) {
            logger.error("Error al actualizar preferencias", e);
            return false;
        }
    }
    
    /**
     * Agrega una categoría favorita a un cliente
     */
    public boolean agregarCategoriaFavorita(int idCliente, String categoria) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            
            Bson filter = Filters.eq("id_cliente", idCliente);
            Bson update = Updates.addToSet("preferencias.categorias_favoritas", categoria);
            
            collection.updateOne(filter, update);
            logger.info("Categoría favorita '{}' agregada para cliente ID: {}", categoria, idCliente);
            return true;
        } catch (Exception e) {
            logger.error("Error al agregar categoría favorita", e);
            return false;
        }
    }
    
    /**
     * Agrega un registro al historial de navegación
     */
    public boolean agregarHistorialNavegacion(int idCliente, int idProducto, int tiempoVisualizacion) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            
            Document historial = new Document()
                .append("id_producto", idProducto)
                .append("fecha_visita", Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("tiempo_visualizacion", tiempoVisualizacion);
            
            Bson filter = Filters.eq("id_cliente", idCliente);
            Bson update = Updates.push("historial_navegacion", historial);
            
            collection.updateOne(filter, update);
            logger.info("Historial agregado para cliente ID: {} - Producto ID: {}", idCliente, idProducto);
            return true;
        } catch (Exception e) {
            logger.error("Error al agregar historial de navegación", e);
            return false;
        }
    }
    
    /**
     * Obtiene todos los comentarios de un cliente
     */
    @SuppressWarnings("unchecked")
    public List<Document> obtenerComentarios(int idCliente) {
        try {
            Document clienteInfo = obtenerClienteInfo(idCliente);
            if (clienteInfo != null) {
                return (List<Document>) clienteInfo.get("comentarios");
            }
        } catch (Exception e) {
            logger.error("Error al obtener comentarios", e);
        }
        return new ArrayList<>();
    }
    
    /**
     * Obtiene las preferencias de un cliente
     */
    public Document obtenerPreferencias(int idCliente) {
        try {
            Document clienteInfo = obtenerClienteInfo(idCliente);
            if (clienteInfo != null) {
                return (Document) clienteInfo.get("preferencias");
            }
        } catch (Exception e) {
            logger.error("Error al obtener preferencias", e);
        }
        return null;
    }
    
    /**
     * Elimina la información de un cliente
     */
    public boolean eliminarClienteInfo(int idCliente) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            collection.deleteOne(Filters.eq("id_cliente", idCliente));
            logger.info("Información eliminada para cliente ID: {}", idCliente);
            return true;
        } catch (Exception e) {
            logger.error("Error al eliminar información del cliente", e);
            return false;
        }
    }
    
    /**
     * Obtiene clientes que desean recibir notificaciones
     */
    public List<Document> obtenerClientesConNotificaciones() {
        List<Document> clientes = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getClientesCollection();
            collection.find(Filters.eq("preferencias.notificaciones", true))
                     .into(clientes);
            logger.info("Se encontraron {} clientes con notificaciones activas", clientes.size());
        } catch (Exception e) {
            logger.error("Error al obtener clientes con notificaciones", e);
        }
        return clientes;
    }
}
