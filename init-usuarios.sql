-- Script para crear tabla de usuarios y datos básicos
-- Ejecutar este script PRIMERO antes de iniciar la aplicación

-- Crear tabla de usuarios si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP,
    ultimo_acceso TIMESTAMP
);

-- Insertar usuario administrador
-- Usuario: admin
-- Contraseña: password123 (encriptada con BCrypt)
INSERT INTO usuarios (nombre_usuario, contrasena, rol, activo, fecha_creacion) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'ADMIN', true, NOW())
ON CONFLICT (nombre_usuario) DO NOTHING;

-- Insertar usuarios cliente de prueba
INSERT INTO usuarios (nombre_usuario, contrasena, rol, activo, fecha_creacion) 
VALUES 
('cliente1', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'CLIENTE', true, NOW()),
('cliente2', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'CLIENTE', true, NOW())
ON CONFLICT (nombre_usuario) DO NOTHING;

-- Verificar que los usuarios se crearon correctamente
SELECT id_usuario, nombre_usuario, rol, activo FROM usuarios;
