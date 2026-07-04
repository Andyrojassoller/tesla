package com.sistema.pedidos.config;

/**
 * Clase de configuración para las conexiones de bases de datos
 */
public class DatabaseConfig {
    
    // Configuración PostgreSQL
    public static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/sistema_pedidos";
    public static final String POSTGRES_USER = "postgres";
    public static final String POSTGRES_PASSWORD = "admin";
    
    // Configuración MongoDB
    public static final String MONGO_CONNECTION_STRING = "mongodb://localhost:27017";
    public static final String MONGO_DATABASE = "sistema_pedidos";
    public static final String MONGO_COLLECTION = "clientes_info";
    
    // Configuración de la aplicación
    public static final int CONNECTION_TIMEOUT = 30000; // 30 segundos
    public static final int MAX_POOL_SIZE = 10;
    
    private DatabaseConfig() {
        // Constructor privado para prevenir instanciación
    }
}
