-- ===================================================
-- DATOS DE PRUEBA PARA EL SISTEMA
-- ===================================================

-- Limpiar datos existentes
TRUNCATE TABLE detalle_pedido, pedidos, usuarios, productos, clientes RESTART IDENTITY CASCADE;

-- ===================================================
-- INSERTAR CLIENTES
-- ===================================================
INSERT INTO clientes (nombre, email, telefono, fecha_registro) VALUES
('Juan Pérez', 'juan.perez@email.com', '555-0101', '2024-01-15'),
('María García', 'maria.garcia@email.com', '555-0102', '2024-02-20'),
('Carlos López', 'carlos.lopez@email.com', '555-0103', '2024-03-10'),
('Ana Martínez', 'ana.martinez@email.com', '555-0104', '2024-04-05'),
('Pedro Rodríguez', 'pedro.rodriguez@email.com', '555-0105', '2024-05-12'),
('Laura Sánchez', 'laura.sanchez@email.com', '555-0106', '2024-06-18'),
('Diego Torres', 'diego.torres@email.com', '555-0107', '2024-07-22'),
('Sofia Ramírez', 'sofia.ramirez@email.com', '555-0108', '2024-08-30'),
('Miguel Flores', 'miguel.flores@email.com', '555-0109', '2024-09-14'),
('Carmen Díaz', 'carmen.diaz@email.com', '555-0110', '2024-10-25'),
('Roberto Morales', 'roberto.morales@email.com', '555-0111', '2024-11-01'),
('Patricia Cruz', 'patricia.cruz@email.com', '555-0112', '2024-11-15'),
('Fernando Reyes', 'fernando.reyes@email.com', '555-0113', '2024-11-20'),
('Gabriela Ortiz', 'gabriela.ortiz@email.com', '555-0114', '2024-11-22'),
('Andrés Mendoza', 'andres.mendoza@email.com', '555-0115', '2024-11-25');

-- ===================================================
-- INSERTAR PRODUCTOS
-- ===================================================
INSERT INTO productos (nombre, precio, stock) VALUES
-- Electrónicos
('Laptop Dell Inspiron 15', 899.99, 25),
('Mouse Logitech MX Master', 79.99, 50),
('Teclado Mecánico RGB', 129.99, 40),
('Monitor Samsung 27"', 349.99, 30),
('Webcam Logitech C920', 89.99, 35),
('Auriculares Sony WH-1000XM4', 349.99, 20),
('Cable USB-C 2m', 19.99, 100),
('Hub USB 4 puertos', 29.99, 60),
-- Oficina
('Silla Ergonómica', 299.99, 15),
('Escritorio Ajustable', 449.99, 10),
('Lámpara LED Escritorio', 39.99, 45),
('Organizador de Escritorio', 24.99, 55),
-- Accesorios
('Mochila para Laptop', 59.99, 40),
('Mouse Pad XXL', 24.99, 70),
('Soporte para Laptop', 34.99, 50),
('Alfombrilla Ergonómica', 19.99, 65),
-- Software/Libros
('Licencia Office 365 - 1 año', 69.99, 200),
('Antivirus Premium - 1 año', 49.99, 150),
('Libro: Clean Code', 39.99, 30),
('Libro: Design Patterns', 44.99, 25),
-- Más productos
('Cámara Web 4K', 159.99, 18),
('Micrófono USB Profesional', 119.99, 22),
('Switch Ethernet 8 puertos', 79.99, 28),
('Router WiFi 6', 189.99, 20),
('Disco Duro Externo 2TB', 89.99, 35);

-- ===================================================
-- INSERTAR USUARIOS
-- ===================================================
-- Contraseñas: admin123, cliente123, empleado123 (deberían estar hasheadas en producción)
INSERT INTO usuarios (usuario, clave, rol, id_cliente) VALUES
('admin', 'admin123', 'admin', NULL),
('juan.perez', 'cliente123', 'cliente', 1),
('maria.garcia', 'cliente123', 'cliente', 2),
('empleado1', 'empleado123', 'empleado', NULL),
('carlos.lopez', 'cliente123', 'cliente', 3),
('ana.martinez', 'cliente123', 'cliente', 4),
('empleado2', 'empleado123', 'empleado', NULL);

-- ===================================================
-- INSERTAR PEDIDOS Y DETALLES
-- ===================================================

-- Pedido 1: Juan Pérez
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (1, '2024-11-01', 0, 'Entregado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(1, 1, 1, 899.99),
(1, 2, 1, 79.99),
(1, 3, 1, 129.99);

-- Pedido 2: María García
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (2, '2024-11-05', 0, 'Entregado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(2, 4, 1, 349.99),
(2, 7, 2, 39.98);

-- Pedido 3: Carlos López
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (3, '2024-11-10', 0, 'Entregado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(3, 9, 1, 299.99),
(3, 10, 1, 449.99);

-- Pedido 4: Ana Martínez
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (4, '2024-11-12', 0, 'Enviado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(4, 6, 1, 349.99),
(4, 13, 1, 59.99);

-- Pedido 5: Pedro Rodríguez
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (5, '2024-11-15', 0, 'Procesando');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(5, 17, 2, 139.98),
(5, 18, 1, 49.99);

-- Pedido 6: Laura Sánchez
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (6, '2024-11-18', 0, 'Procesando');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(6, 19, 1, 39.99),
(6, 20, 1, 44.99),
(6, 14, 1, 24.99);

-- Pedido 7: Diego Torres
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (7, '2024-11-20', 0, 'Pendiente');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(7, 21, 1, 159.99),
(7, 22, 1, 119.99);

-- Pedido 8: Sofia Ramírez
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (8, '2024-11-22', 0, 'Pendiente');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(8, 1, 1, 899.99),
(8, 13, 1, 59.99),
(8, 15, 1, 34.99);

-- Pedido 9: Miguel Flores (Segundo pedido de María García)
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (2, '2024-11-24', 0, 'Entregado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(9, 23, 1, 79.99),
(9, 7, 3, 59.97);

-- Pedido 10: Carmen Díaz
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (10, '2024-11-26', 0, 'Enviado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(10, 24, 1, 189.99),
(10, 25, 1, 89.99);

-- Pedido 11: Roberto Morales
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (11, '2024-11-27', 0, 'Entregado');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(11, 2, 2, 159.98),
(11, 14, 2, 49.98);

-- Pedido 12: Patricia Cruz
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (12, '2024-11-28', 0, 'Procesando');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(12, 11, 1, 39.99),
(12, 12, 2, 49.98);

-- Pedido 13: Juan Pérez (segundo pedido)
INSERT INTO pedidos (id_cliente, fecha_pedido, total, estado) 
VALUES (1, '2024-11-29', 0, 'Pendiente');
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES
(13, 4, 1, 349.99),
(13, 8, 2, 59.98);

-- ===================================================
-- VERIFICACIÓN DE DATOS
-- ===================================================
DO $$
DECLARE
    v_clientes INTEGER;
    v_productos INTEGER;
    v_pedidos INTEGER;
    v_usuarios INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_clientes FROM clientes;
    SELECT COUNT(*) INTO v_productos FROM productos;
    SELECT COUNT(*) INTO v_pedidos FROM pedidos;
    SELECT COUNT(*) INTO v_usuarios FROM usuarios;
    
    RAISE NOTICE '====================================';
    RAISE NOTICE 'DATOS DE PRUEBA INSERTADOS';
    RAISE NOTICE '====================================';
    RAISE NOTICE 'Clientes insertados: %', v_clientes;
    RAISE NOTICE 'Productos insertados: %', v_productos;
    RAISE NOTICE 'Pedidos insertados: %', v_pedidos;
    RAISE NOTICE 'Usuarios insertados: %', v_usuarios;
    RAISE NOTICE '====================================';
END $$;

-- Mostrar resumen de ventas
SELECT 
    'Resumen de Ventas' as titulo,
    COUNT(*) as total_pedidos,
    SUM(total) as ingresos_totales,
    AVG(total) as ticket_promedio
FROM pedidos
WHERE estado != 'Cancelado';
