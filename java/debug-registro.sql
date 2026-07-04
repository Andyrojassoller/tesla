-- Script de verificación para debugging del registro

-- 1. Ver todos los usuarios actuales
SELECT id_usuario, nombre_usuario, rol, activo, fecha_creacion 
FROM usuarios 
ORDER BY fecha_creacion DESC;

-- 2. Buscar si existe el usuario Jose
SELECT * FROM usuarios WHERE nombre_usuario = 'Jose';

-- 3. Ver la estructura de la tabla
\d usuarios

-- 4. Si quieres eliminar el usuario Jose para probar de nuevo:
-- DELETE FROM usuarios WHERE nombre_usuario = 'Jose';

-- 5. Insertar manualmente el usuario Jose con contraseña holaMundo426
-- Primero genera el hash BCrypt de holaMundo426 en Java y úsalo aquí
-- O usa este hash pre-generado para "holaMundo426":
-- INSERT INTO usuarios (nombre_usuario, contrasena, rol, activo, fecha_creacion) 
-- VALUES ('Jose', '$2a$10$kMZj7hPqGKLXHjzVYqNNluYXKLTNJC8xZ9Y1rQ3mGFe2GXpZb.Lzy', 'CLIENTE', true, NOW());
