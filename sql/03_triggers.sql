-- ===================================================
-- TRIGGERS Y FUNCIONES DE TRIGGER
-- ===================================================

-- ===================================================
-- TRIGGER: Validar stock antes de insertar detalle_pedido
-- ===================================================
CREATE OR REPLACE FUNCTION validar_stock_producto()
RETURNS TRIGGER AS $$
DECLARE
    v_stock_actual INTEGER;
BEGIN
    -- Obtener stock actual del producto
    SELECT stock INTO v_stock_actual
    FROM productos
    WHERE id_producto = NEW.id_producto;

    -- Validar que hay suficiente stock
    IF v_stock_actual < NEW.cantidad THEN
        RAISE EXCEPTION 'Stock insuficiente para el producto ID %. Stock disponible: %, Solicitado: %', 
            NEW.id_producto, v_stock_actual, NEW.cantidad;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validar_stock
BEFORE INSERT ON detalle_pedido
FOR EACH ROW
EXECUTE FUNCTION validar_stock_producto();

COMMENT ON FUNCTION validar_stock_producto IS 'Valida que hay stock suficiente antes de agregar productos a un pedido';

-- ===================================================
-- TRIGGER: Actualizar total del pedido automáticamente
-- ===================================================
CREATE OR REPLACE FUNCTION actualizar_total_pedido()
RETURNS TRIGGER AS $$
DECLARE
    v_nuevo_total NUMERIC(10,2);
BEGIN
    -- Calcular el nuevo total del pedido
    SELECT COALESCE(SUM(subtotal), 0) INTO v_nuevo_total
    FROM detalle_pedido
    WHERE id_pedido = COALESCE(NEW.id_pedido, OLD.id_pedido);

    -- Actualizar el total en la tabla pedidos
    UPDATE pedidos
    SET total = v_nuevo_total
    WHERE id_pedido = COALESCE(NEW.id_pedido, OLD.id_pedido);

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_actualizar_total_insert
AFTER INSERT ON detalle_pedido
FOR EACH ROW
EXECUTE FUNCTION actualizar_total_pedido();

CREATE TRIGGER trg_actualizar_total_update
AFTER UPDATE ON detalle_pedido
FOR EACH ROW
EXECUTE FUNCTION actualizar_total_pedido();

CREATE TRIGGER trg_actualizar_total_delete
AFTER DELETE ON detalle_pedido
FOR EACH ROW
EXECUTE FUNCTION actualizar_total_pedido();

COMMENT ON FUNCTION actualizar_total_pedido IS 'Recalcula automáticamente el total del pedido cuando se modifica el detalle';

-- ===================================================
-- TRIGGER: Registrar fecha de último acceso de usuario
-- ===================================================
CREATE OR REPLACE FUNCTION actualizar_ultimo_acceso()
RETURNS TRIGGER AS $$
BEGIN
    NEW.ultimo_acceso := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_ultimo_acceso
BEFORE UPDATE ON usuarios
FOR EACH ROW
WHEN (OLD.ultimo_acceso IS DISTINCT FROM NEW.ultimo_acceso OR NEW.ultimo_acceso IS NULL)
EXECUTE FUNCTION actualizar_ultimo_acceso();

COMMENT ON FUNCTION actualizar_ultimo_acceso IS 'Actualiza la fecha de último acceso del usuario';

-- ===================================================
-- TRIGGER: Validar email único en clientes
-- ===================================================
CREATE OR REPLACE FUNCTION validar_email_unico()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM clientes 
        WHERE email = NEW.email 
        AND id_cliente != NEW.id_cliente
    ) THEN
        RAISE EXCEPTION 'El email % ya está registrado', NEW.email;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validar_email
BEFORE INSERT OR UPDATE ON clientes
FOR EACH ROW
EXECUTE FUNCTION validar_email_unico();

COMMENT ON FUNCTION validar_email_unico IS 'Valida que el email sea único en la tabla de clientes';

-- ===================================================
-- TRIGGER: Restaurar stock al cancelar pedido
-- ===================================================
CREATE OR REPLACE FUNCTION restaurar_stock_cancelacion()
RETURNS TRIGGER AS $$
BEGIN
    -- Solo restaurar stock si el estado cambia a 'Cancelado'
    IF NEW.estado = 'Cancelado' AND OLD.estado != 'Cancelado' THEN
        UPDATE productos p
        SET stock = stock + dp.cantidad
        FROM detalle_pedido dp
        WHERE dp.id_pedido = NEW.id_pedido
        AND p.id_producto = dp.id_producto;
        
        RAISE NOTICE 'Stock restaurado para pedido ID %', NEW.id_pedido;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_restaurar_stock
AFTER UPDATE ON pedidos
FOR EACH ROW
WHEN (NEW.estado = 'Cancelado' AND OLD.estado != 'Cancelado')
EXECUTE FUNCTION restaurar_stock_cancelacion();

COMMENT ON FUNCTION restaurar_stock_cancelacion IS 'Restaura el stock de productos cuando un pedido es cancelado';

-- ===================================================
-- MENSAJE DE CONFIRMACIÓN
-- ===================================================
DO $$
BEGIN
    RAISE NOTICE 'Triggers creados exitosamente';
    RAISE NOTICE 'Total de triggers: 7';
END $$;
