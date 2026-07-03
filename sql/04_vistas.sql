-- ===================================================
-- VISTAS DEL SISTEMA
-- ===================================================

-- ===================================================
-- VISTA: Resumen de clientes con estadísticas
-- ===================================================
CREATE OR REPLACE VIEW vista_clientes_resumen AS
SELECT 
    c.id_cliente,
    c.nombre,
    c.email,
    c.telefono,
    c.fecha_registro,
    COUNT(DISTINCT p.id_pedido) as total_pedidos,
    COALESCE(SUM(p.total), 0) as total_gastado,
    COALESCE(AVG(p.total), 0) as promedio_pedido,
    MAX(p.fecha_pedido) as ultima_compra
FROM clientes c
LEFT JOIN pedidos p ON c.id_cliente = p.id_cliente AND p.estado != 'Cancelado'
GROUP BY c.id_cliente, c.nombre, c.email, c.telefono, c.fecha_registro
ORDER BY total_gastado DESC;

COMMENT ON VIEW vista_clientes_resumen IS 'Resumen de clientes con sus estadísticas de compra';

-- ===================================================
-- VISTA: Detalles completos de pedidos
-- ===================================================
CREATE OR REPLACE VIEW vista_pedidos_completos AS
SELECT 
    p.id_pedido,
    p.fecha_pedido,
    p.estado,
    c.id_cliente,
    c.nombre as nombre_cliente,
    c.email as email_cliente,
    pr.id_producto,
    pr.nombre as nombre_producto,
    pr.precio as precio_unitario,
    dp.cantidad,
    dp.subtotal,
    p.total as total_pedido
FROM pedidos p
INNER JOIN clientes c ON p.id_cliente = c.id_cliente
INNER JOIN detalle_pedido dp ON p.id_pedido = dp.id_pedido
INNER JOIN productos pr ON dp.id_producto = pr.id_producto
ORDER BY p.fecha_pedido DESC, p.id_pedido, dp.id_detalle;

COMMENT ON VIEW vista_pedidos_completos IS 'Vista completa de pedidos con información de clientes y productos';

-- ===================================================
-- VISTA: Productos con información de ventas
-- ===================================================
CREATE OR REPLACE VIEW vista_productos_inventario AS
SELECT 
    p.id_producto,
    p.nombre,
    p.precio,
    p.stock,
    COALESCE(SUM(dp.cantidad), 0) as unidades_vendidas,
    COALESCE(SUM(dp.subtotal), 0) as ingresos_generados,
    COUNT(DISTINCT dp.id_pedido) as numero_pedidos,
    CASE 
        WHEN p.stock = 0 THEN 'Sin Stock'
        WHEN p.stock < 10 THEN 'Stock Bajo'
        WHEN p.stock < 50 THEN 'Stock Medio'
        ELSE 'Stock Alto'
    END as estado_stock
FROM productos p
LEFT JOIN detalle_pedido dp ON p.id_producto = dp.id_producto
LEFT JOIN pedidos ped ON dp.id_pedido = ped.id_pedido AND ped.estado != 'Cancelado'
GROUP BY p.id_producto, p.nombre, p.precio, p.stock
ORDER BY unidades_vendidas DESC;

COMMENT ON VIEW vista_productos_inventario IS 'Información de productos con estadísticas de ventas e inventario';

-- ===================================================
-- VISTA: Pedidos pendientes y en proceso
-- ===================================================
CREATE OR REPLACE VIEW vista_pedidos_activos AS
SELECT 
    p.id_pedido,
    p.fecha_pedido,
    p.estado,
    c.nombre as cliente,
    c.email,
    c.telefono,
    p.total,
    COUNT(dp.id_detalle) as cantidad_items,
    CURRENT_DATE - p.fecha_pedido as dias_desde_pedido
FROM pedidos p
INNER JOIN clientes c ON p.id_cliente = c.id_cliente
LEFT JOIN detalle_pedido dp ON p.id_pedido = dp.id_pedido
WHERE p.estado IN ('Pendiente', 'Procesando', 'Enviado')
GROUP BY p.id_pedido, p.fecha_pedido, p.estado, c.nombre, c.email, c.telefono, p.total
ORDER BY p.fecha_pedido ASC;

COMMENT ON VIEW vista_pedidos_activos IS 'Lista de pedidos que están pendientes, procesando o enviados';

-- ===================================================
-- VISTA: Análisis de ventas por mes
-- ===================================================
CREATE OR REPLACE VIEW vista_ventas_mensuales AS
SELECT 
    EXTRACT(YEAR FROM fecha_pedido) as año,
    EXTRACT(MONTH FROM fecha_pedido) as mes,
    TO_CHAR(fecha_pedido, 'Month YYYY') as periodo,
    COUNT(DISTINCT id_pedido) as cantidad_pedidos,
    COUNT(DISTINCT id_cliente) as clientes_unicos,
    SUM(total) as ingresos_totales,
    AVG(total) as ticket_promedio,
    MIN(total) as venta_minima,
    MAX(total) as venta_maxima
FROM pedidos
WHERE estado != 'Cancelado'
GROUP BY 
    EXTRACT(YEAR FROM fecha_pedido),
    EXTRACT(MONTH FROM fecha_pedido),
    TO_CHAR(fecha_pedido, 'Month YYYY')
ORDER BY año DESC, mes DESC;

COMMENT ON VIEW vista_ventas_mensuales IS 'Análisis de ventas agrupado por mes';

-- ===================================================
-- VISTA: Top clientes
-- ===================================================
CREATE OR REPLACE VIEW vista_top_clientes AS
SELECT 
    c.id_cliente,
    c.nombre,
    c.email,
    COUNT(p.id_pedido) as total_pedidos,
    SUM(p.total) as total_gastado,
    AVG(p.total) as promedio_por_pedido,
    MIN(p.fecha_pedido) as primera_compra,
    MAX(p.fecha_pedido) as ultima_compra,
    CURRENT_DATE - MAX(p.fecha_pedido) as dias_sin_comprar
FROM clientes c
INNER JOIN pedidos p ON c.id_cliente = p.id_cliente
WHERE p.estado != 'Cancelado'
GROUP BY c.id_cliente, c.nombre, c.email
HAVING COUNT(p.id_pedido) > 0
ORDER BY total_gastado DESC
LIMIT 50;

COMMENT ON VIEW vista_top_clientes IS 'Top 50 clientes por monto total gastado';

-- ===================================================
-- VISTA: Usuarios del sistema con información de cliente
-- ===================================================
CREATE OR REPLACE VIEW vista_usuarios_sistema AS
SELECT 
    u.id,
    u.usuario,
    u.rol,
    u.fecha_creacion,
    u.ultimo_acceso,
    CASE 
        WHEN u.ultimo_acceso IS NULL THEN 'Nunca'
        WHEN CURRENT_DATE - u.ultimo_acceso::DATE = 0 THEN 'Hoy'
        WHEN CURRENT_DATE - u.ultimo_acceso::DATE = 1 THEN 'Ayer'
        ELSE (CURRENT_DATE - u.ultimo_acceso::DATE) || ' días'
    END as ultimo_acceso_texto,
    c.nombre as nombre_cliente,
    c.email as email_cliente
FROM usuarios u
LEFT JOIN clientes c ON u.id_cliente = c.id_cliente
ORDER BY u.fecha_creacion DESC;

COMMENT ON VIEW vista_usuarios_sistema IS 'Lista de usuarios del sistema con información asociada';

-- ===================================================
-- MENSAJE DE CONFIRMACIÓN
-- ===================================================
DO $$
BEGIN
    RAISE NOTICE 'Vistas creadas exitosamente';
    RAISE NOTICE 'Total de vistas: 7';
END $$;
