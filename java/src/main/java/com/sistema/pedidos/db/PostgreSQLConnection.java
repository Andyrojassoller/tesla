package com.sistema.pedidos.db;

import com.sistema.pedidos.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexiones para PostgreSQL
 */
public class PostgreSQLConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLConnection.class);
    private static Connection connection = null;
    
    private PostgreSQLConnection() {
        // Constructor privado
    }
    
    /**
     * Obtiene una conexión a PostgreSQL
     * @return Conexión activa
     * @throws SQLException Si hay error en la conexión
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                    DatabaseConfig.POSTGRES_URL,
                    DatabaseConfig.POSTGRES_USER,
                    DatabaseConfig.POSTGRES_PASSWORD
                );
                logger.info("Conexión a PostgreSQL establecida exitosamente");
            } catch (ClassNotFoundException e) {
                logger.error("Driver de PostgreSQL no encontrado", e);
                throw new SQLException("Driver no encontrado", e);
            }
        }
        return connection;
    }
    
    /**
     * Cierra la conexión a PostgreSQL
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Conexión a PostgreSQL cerrada");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión de PostgreSQL", e);
            }
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si está conectado, false en caso contrario
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
