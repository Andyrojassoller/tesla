package com.sistema.pedidos.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sistema.pedidos.config.DatabaseConfig;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Gestor de conexiones para MongoDB
 */
public class MongoDBConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(MongoDBConnection.class);
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;
    
    private MongoDBConnection() {
        // Constructor privado
    }
    
    /**
     * Obtiene el cliente de MongoDB
     * @return Cliente MongoDB
     */
    public static MongoClient getClient() {
        if (mongoClient == null) {
            try {
                MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(DatabaseConfig.MONGO_CONNECTION_STRING))
                    .applyToConnectionPoolSettings(builder ->
                        builder.maxSize(DatabaseConfig.MAX_POOL_SIZE)
                            .maxWaitTime(DatabaseConfig.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    )
                    .build();
                
                mongoClient = MongoClients.create(settings);
                logger.info("Cliente MongoDB creado exitosamente");
            } catch (Exception e) {
                logger.error("Error al crear el cliente de MongoDB", e);
                throw new RuntimeException("No se pudo conectar a MongoDB", e);
            }
        }
        return mongoClient;
    }
    
    /**
     * Obtiene la base de datos
     * @return Base de datos MongoDB
     */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            database = getClient().getDatabase(DatabaseConfig.MONGO_DATABASE);
            logger.info("Acceso a base de datos MongoDB: {}", DatabaseConfig.MONGO_DATABASE);
        }
        return database;
    }
    
    /**
     * Obtiene la colección de clientes
     * @return Colección de clientes
     */
    public static MongoCollection<Document> getClientesCollection() {
        return getDatabase().getCollection(DatabaseConfig.MONGO_COLLECTION);
    }
    
    /**
     * Cierra la conexión a MongoDB
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            logger.info("Conexión a MongoDB cerrada");
        }
    }
    
    /**
     * Verifica la conexión ejecutando un ping
     * @return true si está conectado, false en caso contrario
     */
    public static boolean testConnection() {
        try {
            getDatabase().runCommand(new Document("ping", 1));
            logger.info("Conexión a MongoDB verificada exitosamente");
            return true;
        } catch (Exception e) {
            logger.error("Error al verificar la conexión de MongoDB", e);
            return false;
        }
    }
}
