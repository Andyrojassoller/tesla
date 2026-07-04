-- ===================================================
-- PROCEDIMIENTOS ALMACENADOS Y FUNCIONES
-- ===================================================

-- ===================================================
-- FUNCIÓN: Registrar un nuevo pedido
-- ===================================================
CREATE OR REPLACE FUNCTION registrar_pedido(
    p_id_cliente INTEGER,
    p_productos JSON
) RETURNS INTEGER AS $$
DECLARE
    v_id_pedido INTEGER;
    v_total NUMERIC(10,2) := 0;
    v_producto JSON;
    v_subtotal NUMERIC(10,2);
    v_precio NUMERIC(10,2);
BEGIN
    -- Validar que el cliente existe
    IF NOT EXISTS (SELECT 1 FROM clientes WHERE id_cliente = p_id_cliente) THEN
        RAISE EXCEPTION 'Cliente con ID % no existe', p_id_cliente;
    END IF;

    -- Crear el pedido
    INSERT INTO pedidos (id_cliente, total)
    VALUES (p_id_cliente, 0)
    RETURNING id_pedido INTO v_id_pedido;

    -- Procesar cada producto
    FOR v_producto IN SELECT * FROM json_array_elements(p_productos)
    LOOP
        -- Obtener el precio del producto
        SELECT precio INTO v_precio
        FROM productos
        WHERE id_producto = (v_producto->>'id_producto')::INTEGER;

        IF v_precio IS NULL THEN
            RAISE EXCEPTION 'Producto con ID % no existe', v_producto->>'id_producto';
        END IF;

        -- Calcular subtotal
        v_subtotal := v_precio * (v_producto->>'cantidad')::INTEGER;
        v_total := v_total + v_subtotal;

        -- Insertar detalle del pedido
        INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal)
        VALUES (
            v_id_pedido,
            (v_producto->>'id_producto')::INTEGER,
            (v_producto->>'cantidad')::INTEGER,
            v_subtotal
        );

        -- Actualizar stock
        UPDATE productos
        SET stock = stock - (v_producto->>'cantidad')::INTEGER
        WHERE id_producto = (v_producto->>'id_producto')::INTEGER;
    END LOOP;

    -- Actualizar el total del pedido
    UPDATE pedidos
    SET total = v_total
    WHERE id_pedido = v_id_pedido;

    RETURN v_id_pedido;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION registrar_pedido IS 'Registra un nuevo pedido con sus productos';

-- ===================================================
-- FUNCIÓN: Obtener historial de compras de un cliente
-- ===================================================
CREATE OR REPLACE FUNCTION obtener_historial_cliente(
    p_id_cliente INTEGER
) RETURNS TABLE (
    id_pedido INTEGER,
    fecha_pedido DATE,
    total NUMERIC,
    estado VARCHAR,
    cantidad_productos BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        p.id_pedido,
        p.fecha_pedido,
        p.total,
        p.estado,
        COUNT(dp.id_detalle) as cantidad_productos
    FROM pedidos p
    LEFT JOIN detalle_pedido dp ON p.id_pedido = dp.id_pedido
    WHERE p.id_cliente = p_id_cliente
    GROUP BY p.id_pedido, p.fecha_pedido, p.total, p.estado
    ORDER BY p.fecha_pedido DESC;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION obtener_historial_cliente IS 'Obtiene el historial de pedidos de un cliente';

-- ===================================================
-- FUNCIÓN: Calcular total de ventas por período
-- ===================================================
CREATE OR REPLACE FUNCTION calcular_ventas_periodo(
    p_fecha_inicio DATE,
    p_fecha_fin DATE
) RETURNS TABLE (
    fecha DATE,
    cantidad_pedidos BIGINT,
    total_ventas NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        fecha_pedido,
        COUNT(*) as cantidad_pedidos,
        SUM(total) as total_ventas
    FROM pedidos
    WHERE fecha_pedido BETWEEN p_fecha_inicio AND p_fecha_fin
        AND estado != 'Cancelado'
    GROUP BY fecha_pedido
    ORDER BY fecha_pedido;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION calcular_ventas_periodo IS 'Calcula las ventas totales en un período específico';

-- ===================================================
-- FUNCIÓN: Obtener productos más vendidos
-- ===================================================
CREATE OR REPLACE FUNCTION productos_mas_vendidos(
    p_limite INTEGER DEFAULT 10
) RETURNS TABLE (
    id_producto INTEGER,
    nombre_producto VARCHAR,
    cantidad_vendida BIGINT,
    ingresos_generados NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        pr.id_producto,
        pr.nombre,
        SUM(dp.cantidad) as cantidad_vendida,
        SUM(dp.subtotal) as ingresos_generados
    FROM productos pr
    INNER JOIN detalle_pedido dp ON pr.id_producto = dp.id_producto
    INNER JOIN pedidos p ON dp.id_pedido = p.id_pedido
    WHERE p.estado != 'Cancelado'
    GROUP BY pr.id_producto, pr.nombre
    ORDER BY cantidad_vendida DESC
    LIMIT p_limite;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION productos_mas_vendidos IS 'Lista los productos más vendidos';

-- ===================================================
-- FUNCIÓN: Actualizar estado de pedido
-- ===================================================
CREATE OR REPLACE FUNCTION actualizar_estado_pedido(
    p_id_pedido INTEGER,
    p_nuevo_estado VARCHAR
) RETURNS BOOLEAN AS $$
DECLARE
    v_estado_actual VARCHAR;
BEGIN
    -- Obtener estado actual
    SELECT estado INTO v_estado_actual
    FROM pedidos
    WHERE id_pedido = p_id_pedido;

    IF v_estado_actual IS NULL THEN
        RAISE EXCEPTION 'Pedido con ID % no existe', p_id_pedido;
    END IF;

    -- Actualizar estado
    UPDATE pedidos
    SET estado = p_nuevo_estado
    WHERE id_pedido = p_id_pedido;

    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION actualizar_estado_pedido IS 'Actualiza el estado de un pedido';

-- ===================================================
-- MENSAJE DE CONFIRMACIÓN
-- ===================================================
DO $$
BEGIN
    RAISE NOTICE 'Procedimientos almacenados creados exitosamente';
    RAISE NOTICE 'Total de funciones: 5';
END $$;
