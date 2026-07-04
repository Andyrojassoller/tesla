-- Script de datos de prueba para el Sistema de Gestión de Pedidos
-- Incluye usuarios con contraseñas encriptadas usando BCrypt

-- =====================================================
-- USUARIOS (contraseña por defecto: "password123")
-- =====================================================
-- BCrypt hash de "password123": $2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu

-- Usuario Administrador
INSERT INTO usuarios (nombre_usuario, contrasena, rol, activo) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'ADMIN', true);

-- Usuarios Clientes de prueba
INSERT INTO usuarios (nombre_usuario, contrasena, rol, activo) VALUES 
('cliente1', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'CLIENTE', true),
('cliente2', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'CLIENTE', true),
('jperez', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'CLIENTE', true),
('mgarcia', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'CLIENTE', true);

-- =====================================================
-- CLIENTES
-- =====================================================
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, activo) VALUES 
('Juan', 'Pérez', 'juan.perez@email.com', '987654321', 'Av. Los Incas 123, Lima', true),
('María', 'García', 'maria.garcia@email.com', '987654322', 'Jr. Miraflores 456, Arequipa', true),
('Carlos', 'López', 'carlos.lopez@email.com', '987654323', 'Calle Las Flores 789, Cusco', true),
('Ana', 'Martínez', 'ana.martinez@email.com', '987654324', 'Av. Principal 321, Trujillo', true),
('Luis', 'Rodríguez', 'luis.rodriguez@email.com', '987654325', 'Jr. Comercio 654, Piura', true);

-- =====================================================
-- PRODUCTOS
-- =====================================================
-- Categoría: Electrónica
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo) VALUES 
('Laptop HP Pavilion 15', 'Laptop con Intel Core i5, 8GB RAM, 256GB SSD', 2499.00, 15, 'Electrónica', true),
('Mouse Inalámbrico Logitech', 'Mouse óptico inalámbrico 2.4GHz', 45.90, 50, 'Electrónica', true),
('Teclado Mecánico RGB', 'Teclado gaming con iluminación RGB', 189.00, 25, 'Electrónica', true),
('Monitor LED 24 pulgadas', 'Monitor Full HD 1920x1080', 599.00, 20, 'Electrónica', true),
('Auriculares Bluetooth', 'Auriculares inalámbricos con cancelación de ruido', 129.90, 30, 'Electrónica', true);

-- Categoría: Oficina
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo) VALUES 
('Silla Ergonómica', 'Silla de oficina con soporte lumbar', 350.00, 12, 'Oficina', true),
('Escritorio de Madera', 'Escritorio ejecutivo 120x60cm', 450.00, 8, 'Oficina', true),
('Lámpara LED de Escritorio', 'Lámpara ajustable con 3 niveles de brillo', 75.00, 40, 'Oficina', true),
('Organizador de Escritorio', 'Set de organizadores de acrílico', 35.00, 60, 'Oficina', true),
('Alfombrilla de Ratón XXL', 'Alfombrilla extendida 80x40cm', 55.00, 35, 'Oficina', true);

-- Categoría: Hogar
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo) VALUES 
('Cafetera Eléctrica', 'Cafetera programable 12 tazas', 180.00, 18, 'Hogar', true),
('Licuadora 3 Velocidades', 'Licuadora con vaso de vidrio 1.5L', 120.00, 22, 'Hogar', true),
('Aspiradora Portátil', 'Aspiradora inalámbrica 2 en 1', 250.00, 10, 'Hogar', true),
('Juego de Sartenes', 'Set de 3 sartenes antiadherentes', 95.00, 28, 'Hogar', true);

-- Categoría: Deportes
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo) VALUES 
('Bicicleta de Montaña', 'Bicicleta MTB aro 29 con 21 velocidades', 899.00, 6, 'Deportes', true),
('Mancuernas 5kg (Par)', 'Set de mancuernas con recubrimiento de neopreno', 65.00, 45, 'Deportes', true),
('Colchoneta de Yoga', 'Colchoneta antideslizante 6mm grosor', 45.00, 38, 'Deportes', true),
('Cuerda para Saltar', 'Cuerda ajustable con contador digital', 25.00, 55, 'Deportes', true);

-- Categoría: Libros
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo) VALUES 
('Clean Code - Robert Martin', 'Libro sobre buenas prácticas de programación', 85.00, 20, 'Libros', true),
('Cien Años de Soledad', 'Novela clásica de Gabriel García Márquez', 45.00, 30, 'Libros', true),
('El Arte de la Guerra', 'Tratado clásico de estrategia militar', 35.00, 25, 'Libros', true);

-- =====================================================
-- PEDIDOS DE EJEMPLO
-- =====================================================
-- Pedido 1 (Cliente Juan Pérez)
INSERT INTO pedidos (id_cliente, fecha_pedido, estado, total) VALUES 
(1, CURRENT_TIMESTAMP - INTERVAL '5 days', 'ENTREGADO', 2544.90);

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES 
(1, 1, 1, 2499.00, 2499.00),
(1, 2, 1, 45.90, 45.90);

-- Pedido 2 (Cliente María García)
INSERT INTO pedidos (id_cliente, fecha_pedido, estado, total) VALUES 
(2, CURRENT_TIMESTAMP - INTERVAL '3 days', 'EN_PROCESO', 1079.00);

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES 
(2, 4, 1, 599.00, 599.00),
(2, 3, 1, 189.00, 189.00),
(2, 7, 1, 75.00, 75.00),
(2, 8, 2, 55.00, 110.00),
(2, 9, 2, 35.00, 70.00),
(2, 10, 1, 35.00, 35.00);

-- Pedido 3 (Cliente Carlos López)
INSERT INTO pedidos (id_cliente, fecha_pedido, estado, total) VALUES 
(3, CURRENT_TIMESTAMP - INTERVAL '1 day', 'PENDIENTE', 1244.00);

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES 
(3, 6, 1, 350.00, 350.00),
(3, 15, 1, 899.00, 899.00);

-- Pedido 4 (Cliente Ana Martínez)
INSERT INTO pedidos (id_cliente, fecha_pedido, estado, total) VALUES 
(4, CURRENT_TIMESTAMP, 'PENDIENTE', 565.00);

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES 
(4, 11, 1, 180.00, 180.00),
(4, 12, 1, 120.00, 120.00),
(4, 14, 1, 95.00, 95.00),
(4, 19, 2, 85.00, 170.00);

-- =====================================================
-- COMENTARIOS
-- =====================================================
-- Comentarios en productos (necesitaremos los IDs de usuarios primero)
INSERT INTO comentarios (id_producto, id_usuario, contenido, calificacion, activo) VALUES 
(1, 4, 'Excelente laptop, muy rápida y el precio está muy bien. La recomiendo.', 5, true),
(1, 5, 'Buena calidad pero el envío tardó un poco más de lo esperado.', 4, true),
(2, 4, 'Mouse muy cómodo, perfecto para trabajar todo el día.', 5, true),
(4, 5, 'La pantalla tiene muy buenos colores, ideal para diseño.', 5, true),
(11, 4, 'La cafetera es fácil de usar y el café queda delicioso.', 5, true),
(15, 5, 'Excelente bicicleta, muy resistente. Perfecta para el campo.', 5, true),
(19, 4, 'Libro imprescindible para todo programador. Muy didáctico.', 5, true);

-- =====================================================
-- INFORMACIÓN PARA ACCESO
-- =====================================================
-- Usuario: admin
-- Contraseña: password123
-- Rol: ADMIN (acceso completo)

-- Usuario: cliente1, cliente2, jperez, mgarcia
-- Contraseña: password123 (para todos)
-- Rol: CLIENTE (acceso limitado)

-- NOTA: En producción, cambiar estas contraseñas por unas más seguras
-- y usar un BCrypt encoder diferente para cada usuario.

-- Para generar nuevas contraseñas BCrypt en Java:
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String hashedPassword = encoder.encode("tu_contraseña");
