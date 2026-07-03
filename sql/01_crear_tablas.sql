-- ===================================================
-- PROYECTO: SISTEMA DE GESTIÓN DE PEDIDOS
-- Base de Datos: PostgreSQL
-- Fecha: 30 de noviembre de 2025
-- ===================================================

-- Eliminar tablas existentes (en orden inverso por dependencias)
DROP TABLE IF EXISTS detalle_pedido CASCADE;
DROP TABLE IF EXISTS pedidos CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS productos CASCADE;
DROP TABLE IF EXISTS clientes CASCADE;

-- ===================================================
-- TABLA: clientes
-- Descripción: Almacena información básica de los clientes
-- ===================================================
CREATE TABLE clientes (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_registro DATE DEFAULT CURRENT_DATE,
    CONSTRAINT chk_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

-- Comentarios en la tabla
COMMENT ON TABLE clientes IS 'Almacena la información básica de los clientes';
COMMENT ON COLUMN clientes.id_cliente IS 'Identificador único del cliente';
COMMENT ON COLUMN clientes.nombre IS 'Nombre completo del cliente';
COMMENT ON COLUMN clientes.email IS 'Correo electrónico único del cliente';
COMMENT ON COLUMN clientes.telefono IS 'Número de teléfono de contacto';
COMMENT ON COLUMN clientes.fecha_registro IS 'Fecha de registro del cliente en el sistema';

-- ===================================================
-- TABLA: productos
-- Descripción: Catálogo de productos disponibles
-- ===================================================
CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock INTEGER DEFAULT 0,
    CONSTRAINT chk_precio CHECK (precio >= 0),
    CONSTRAINT chk_stock CHECK (stock >= 0)
);

-- Comentarios en la tabla
COMMENT ON TABLE productos IS 'Catálogo de productos disponibles para la venta';
COMMENT ON COLUMN productos.id_producto IS 'Identificador único del producto';
COMMENT ON COLUMN productos.nombre IS 'Nombre del producto';
COMMENT ON COLUMN productos.precio IS 'Precio unitario del producto';
COMMENT ON COLUMN productos.stock IS 'Cantidad disponible en inventario';

-- ===================================================
-- TABLA: pedidos
-- Descripción: Registra los pedidos realizados por clientes
-- ===================================================
CREATE TABLE pedidos (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    fecha_pedido DATE DEFAULT CURRENT_DATE,
    total NUMERIC(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Pendiente',
    CONSTRAINT fk_id_cliente FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT chk_total CHECK (total >= 0),
    CONSTRAINT chk_estado CHECK (estado IN ('Pendiente', 'Procesando', 'Enviado', 'Entregado', 'Cancelado'))
);

-- Comentarios en la tabla
COMMENT ON TABLE pedidos IS 'Registra los pedidos realizados por los clientes';
COMMENT ON COLUMN pedidos.id_pedido IS 'Identificador único del pedido';
COMMENT ON COLUMN pedidos.id_cliente IS 'Referencia al cliente que realizó el pedido';
COMMENT ON COLUMN pedidos.fecha_pedido IS 'Fecha en que se realizó el pedido';
COMMENT ON COLUMN pedidos.total IS 'Monto total del pedido';
COMMENT ON COLUMN pedidos.estado IS 'Estado actual del pedido';

-- ===================================================
-- TABLA: detalle_pedido
-- Descripción: Detalla los productos incluidos en cada pedido
-- ===================================================
CREATE TABLE detalle_pedido (
    id_detalle SERIAL PRIMARY KEY,
    id_pedido INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_id_pedido FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id_pedido)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_id_producto FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_subtotal CHECK (subtotal >= 0)
);

-- Comentarios en la tabla
COMMENT ON TABLE detalle_pedido IS 'Especifica los productos incluidos en cada pedido';
COMMENT ON COLUMN detalle_pedido.id_detalle IS 'Identificador único del detalle';
COMMENT ON COLUMN detalle_pedido.id_pedido IS 'Referencia al pedido';
COMMENT ON COLUMN detalle_pedido.id_producto IS 'Referencia al producto';
COMMENT ON COLUMN detalle_pedido.cantidad IS 'Cantidad de unidades del producto';
COMMENT ON COLUMN detalle_pedido.subtotal IS 'Subtotal de la línea (precio × cantidad)';

-- ===================================================
-- TABLA: usuarios
-- Descripción: Usuarios del sistema con autenticación
-- ===================================================
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(64) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    id_cliente INTEGER,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP,
    CONSTRAINT fk_id_cliente_usuarios FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT chk_rol CHECK (rol IN ('admin', 'cliente', 'empleado'))
);

-- Comentarios en la tabla
COMMENT ON TABLE usuarios IS 'Usuarios del sistema con credenciales de acceso';
COMMENT ON COLUMN usuarios.id IS 'Identificador único del usuario';
COMMENT ON COLUMN usuarios.usuario IS 'Nombre de usuario para login';
COMMENT ON COLUMN usuarios.clave IS 'Contraseña hasheada del usuario';
COMMENT ON COLUMN usuarios.rol IS 'Rol del usuario en el sistema';
COMMENT ON COLUMN usuarios.id_cliente IS 'Referencia opcional al cliente asociado';
COMMENT ON COLUMN usuarios.fecha_creacion IS 'Fecha de creación del usuario';
COMMENT ON COLUMN usuarios.ultimo_acceso IS 'Fecha y hora del último acceso';

-- ===================================================
-- ÍNDICES PARA MEJORAR RENDIMIENTO
-- ===================================================
CREATE INDEX idx_clientes_email ON clientes(email);
CREATE INDEX idx_pedidos_cliente ON pedidos(id_cliente);
CREATE INDEX idx_pedidos_fecha ON pedidos(fecha_pedido);
CREATE INDEX idx_detalle_pedido ON detalle_pedido(id_pedido);
CREATE INDEX idx_detalle_producto ON detalle_pedido(id_producto);
CREATE INDEX idx_usuarios_usuario ON usuarios(usuario);

-- ===================================================
-- MENSAJE DE CONFIRMACIÓN
-- ===================================================
DO $$
BEGIN
    RAISE NOTICE 'Tablas creadas exitosamente';
    RAISE NOTICE 'Total de tablas: 5 (clientes, productos, pedidos, detalle_pedido, usuarios)';
END $$;
