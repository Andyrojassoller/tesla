# Sistema de Gestión de Pedidos

Sistema de gestión de pedidos que integra PostgreSQL (base de datos relacional) y MongoDB (base de datos NoSQL) mediante una aplicación Java con interfaz de línea de comandos.

## 📋 Descripción

Este proyecto implementa un sistema completo de gestión de pedidos con:

- **PostgreSQL**: Almacena datos estructurados (clientes, productos, pedidos, usuarios)
- **MongoDB**: Almacena información no estructurada (comentarios, preferencias, historial)
- **Java**: Aplicación centralizada que integra ambas bases de datos

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────┐
│     Aplicación Java (CLI)               │
│   - Interfaz de Usuario                 │
│   - Capa de Servicios                   │
│   - Gestión de Conexiones               │
└────────────┬───────────────┬────────────┘
             │               │
    ┌────────▼──────┐   ┌───▼─────────┐
    │  PostgreSQL   │   │   MongoDB   │
    │               │   │             │
    │ • Clientes    │   │ • Comentarios│
    │ • Productos   │   │ • Preferencias│
    │ • Pedidos     │   │ • Historial  │
    │ • Usuarios    │   │              │
    └───────────────┘   └──────────────┘
```

## 🔧 Requisitos Previos

### Software Necesario

1. **PostgreSQL 12+**
   - Descargar de: https://www.postgresql.org/download/
   - Puerto por defecto: 5432

2. **MongoDB 4.11+**
   - Descargar de: https://www.mongodb.com/try/download/community
   - Puerto por defecto: 27017

3. **Java Development Kit (JDK) 17+**
   - Descargar de: https://adoptium.net/
   - Verificar instalación: `java -version`

4. **Apache Maven 3.6+**
   - Descargar de: https://maven.apache.org/download.cgi
   - Verificar instalación: `mvn -version`

## 📦 Instalación y Configuración

### 1. Configurar PostgreSQL

1. Iniciar el servidor PostgreSQL
2. Crear la base de datos:

```bash
psql -U postgres
CREATE DATABASE sistema_pedidos;
\q
```

3. Ejecutar los scripts SQL en orden:

```bash
cd sql
psql -U postgres -d sistema_pedidos -f 01_crear_tablas.sql
psql -U postgres -d sistema_pedidos -f 02_procedimientos_almacenados.sql
psql -U postgres -d sistema_pedidos -f 03_triggers.sql
psql -U postgres -d sistema_pedidos -f 04_vistas.sql
psql -U postgres -d sistema_pedidos -f 05_datos_prueba.sql
```

4. Verificar la creación:

```sql
\c sistema_pedidos
\dt  -- Listar tablas
SELECT * FROM clientes LIMIT 5;
```

### 2. Configurar MongoDB

1. Iniciar el servidor MongoDB:

```bash
mongod
```

2. En otra terminal, conectar y ejecutar los scripts:

```bash
mongosh
use sistema_pedidos
```

3. Ejecutar los scripts de MongoDB:

```bash
load('mongodb/01_crear_coleccion.js')
load('mongodb/02_consultas_avanzadas.js')
```

4. Verificar la creación:

```javascript
db.clientes_info.countDocuments()
db.clientes_info.findOne()
```

### 3. Configurar la Aplicación Java

1. Navegar al directorio del proyecto Java:

```bash
cd java
```

2. Editar configuración de base de datos (si es necesario):

Archivo: `src/main/java/com/sistema/pedidos/config/DatabaseConfig.java`

```java
// PostgreSQL
public static final String PG_URL = "jdbc:postgresql://localhost:5432/sistema_pedidos";
public static final String PG_USER = "postgres";
public static final String PG_PASSWORD = "tu_password";

// MongoDB
public static final String MONGO_URL = "mongodb://localhost:27017";
public static final String MONGO_DATABASE = "sistema_pedidos";
```

3. Compilar el proyecto:

```bash
mvn clean compile
```

4. Ejecutar la aplicación:

```bash
mvn exec:java -Dexec.mainClass="com.sistema.pedidos.Main"
```

O generar un JAR ejecutable:

```bash
mvn clean package
java -jar target/sistema-pedidos-1.0-SNAPSHOT.jar
```

## 📖 Estructura del Proyecto

```
proyecto-bd/
│
├── sql/                                  # Scripts PostgreSQL
│   ├── 01_crear_tablas.sql
│   ├── 02_procedimientos_almacenados.sql
│   ├── 03_triggers.sql
│   ├── 04_vistas.sql
│   └── 05_datos_prueba.sql
│
├── mongodb/                              # Scripts MongoDB
│   ├── 01_crear_coleccion.js
│   └── 02_consultas_avanzadas.js
│
├── java/                                 # Aplicación Java
│   ├── pom.xml
│   └── src/main/java/com/sistema/pedidos/
│       ├── Main.java                    # Clase principal
│       ├── config/
│       │   └── DatabaseConfig.java      # Configuración
│       ├── db/
│       │   ├── PostgreSQLConnection.java
│       │   └── MongoDBConnection.java
│       ├── model/
│       │   ├── Cliente.java
│       │   ├── Producto.java
│       │   ├── Pedido.java
│       │   └── ClienteInfo.java
│       └── service/
│           ├── ClienteService.java
│           └── ClienteInfoService.java
│
└── README.md                            # Este archivo
```

## 🚀 Uso de la Aplicación

### Menú Principal

Al ejecutar la aplicación, verás el siguiente menú:

```
╔═══════════════════════════════════════════════╗
║     SISTEMA DE GESTIÓN DE PEDIDOS             ║
╠═══════════════════════════════════════════════╣
║  1. Gestión de Clientes (PostgreSQL)         ║
║  2. Comentarios y Preferencias (MongoDB)      ║
║  3. Vista Integrada de Datos                  ║
║  4. Reportes y Estadísticas                   ║
║  0. Salir                                     ║
╚═══════════════════════════════════════════════╝
```

### Funcionalidades

#### 1. Gestión de Clientes (PostgreSQL)
- Listar todos los clientes
- Buscar cliente por ID
- Buscar clientes por nombre
- Agregar nuevo cliente
- Actualizar información del cliente
- Eliminar cliente

#### 2. Comentarios y Preferencias (MongoDB)
- Ver información completa de cliente
- Agregar comentarios sobre productos
- Actualizar preferencias (idioma, método de pago)
- Agregar categorías favoritas
- Ver clientes con notificaciones activas

#### 3. Vista Integrada
- Muestra información combinada de PostgreSQL y MongoDB
- Datos básicos del cliente
- Comentarios y preferencias
- Vista unificada de toda la información

#### 4. Reportes y Estadísticas
- Estadísticas de ventas
- Productos más vendidos
- Análisis de clientes

## 📊 Modelo de Datos

### PostgreSQL - Tablas Principales

**clientes**
- id_cliente (PK)
- nombre
- email (único)
- telefono
- fecha_registro

**productos**
- id_producto (PK)
- nombre
- precio
- stock

**pedidos**
- id_pedido (PK)
- id_cliente (FK)
- fecha_pedido
- estado
- total

**detalle_pedido**
- id_detalle (PK)
- id_pedido (FK)
- id_producto (FK)
- cantidad
- precio_unitario

### MongoDB - Colección clientes_info

```json
{
  "id_cliente": 1,
  "comentarios": [
    {
      "texto": "Excelente producto",
      "fecha": "2024-01-15",
      "producto_relacionado": 5,
      "calificacion": 5
    }
  ],
  "preferencias": {
    "idioma": "español",
    "metodo_pago": "tarjeta",
    "notificaciones": true,
    "categorias_favoritas": ["Electrónica", "Oficina"],
    "newsletter": true
  },
  "historial_navegacion": [
    {
      "id_producto": 3,
      "fecha_visita": "2024-01-14",
      "tiempo_visualizacion": 120
    }
  ]
}
```

## 🔍 Consultas Útiles

### PostgreSQL

```sql
-- Ver resumen de clientes
SELECT * FROM vista_clientes_resumen;

-- Pedidos completos con detalles
SELECT * FROM vista_pedidos_completos;

-- Top clientes
SELECT * FROM vista_top_clientes LIMIT 10;

-- Registrar un nuevo pedido
SELECT registrar_pedido(1, 'ARRAY[2,3]'::INTEGER[], 'ARRAY[1,2]'::INTEGER[]);
```

### MongoDB

```javascript
// Buscar comentarios de un cliente
db.clientes_info.find(
  { id_cliente: 1 },
  { comentarios: 1 }
)

// Clientes con notificaciones activas
db.clientes_info.find({
  "preferencias.notificaciones": true
})

// Agregar comentario
db.clientes_info.updateOne(
  { id_cliente: 1 },
  {
    $push: {
      comentarios: {
        texto: "Muy buen servicio",
        fecha: new Date(),
        calificacion: 5
      }
    }
  }
)
```

## 🛠️ Solución de Problemas

### Error: No se puede conectar a PostgreSQL

1. Verificar que el servicio está iniciado:
```bash
pg_ctl status
```

2. Verificar credenciales en `DatabaseConfig.java`

3. Verificar firewall y puerto 5432

### Error: No se puede conectar a MongoDB

1. Verificar que MongoDB está ejecutándose:
```bash
mongosh --eval "db.adminCommand('ping')"
```

2. Verificar URL de conexión en `DatabaseConfig.java`

3. Verificar puerto 27017

### Error: Dependencias de Maven no se descargan

1. Limpiar repositorio local:
```bash
mvn clean
```

2. Forzar actualización:
```bash
mvn clean install -U
```

3. Verificar conexión a internet

## 📝 Características Implementadas

- ✅ Base de datos relacional (PostgreSQL) con 5 tablas
- ✅ 5 procedimientos almacenados
- ✅ 7 triggers para validación y automatización
- ✅ 7 vistas para reportes
- ✅ Base de datos NoSQL (MongoDB) con validación de esquema
- ✅ 15 consultas avanzadas en MongoDB
- ✅ Aplicación Java con arquitectura en capas
- ✅ Conexiones seguras con pools de conexiones
- ✅ Interfaz CLI interactiva
- ✅ Integración de datos entre PostgreSQL y MongoDB
- ✅ Manejo de errores y logging
- ✅ Datos de prueba pre-cargados

## 🎯 Casos de Uso

### 1. Registrar un nuevo cliente

1. Menú principal → Opción 1 (Gestión de Clientes)
2. Opción 4 (Agregar nuevo cliente)
3. Ingresar datos: nombre, email, teléfono
4. Opcionalmente agregar preferencias en MongoDB

### 2. Ver información completa de un cliente

1. Menú principal → Opción 3 (Vista Integrada)
2. Ingresar ID del cliente
3. Ver datos de PostgreSQL y MongoDB combinados

### 3. Agregar comentario sobre un producto

1. Menú principal → Opción 2 (Comentarios y Preferencias)
2. Opción 2 (Agregar comentario)
3. Ingresar ID cliente, texto, producto relacionado y calificación

## 📈 Mejoras Futuras

- [ ] Interfaz gráfica (GUI) con JavaFX
- [ ] API REST con Spring Boot
- [ ] Autenticación y autorización de usuarios
- [ ] Reportes en PDF
- [ ] Dashboard con estadísticas en tiempo real
- [ ] Integración con sistemas de pago
- [ ] Notificaciones por email
- [ ] Exportación de datos a Excel/CSV

## 👥 Autor

Proyecto de Base de Datos - Sistema de Gestión de Pedidos

## 📄 Licencia

Este proyecto es de uso académico.

## 🤝 Contribuciones

Para contribuir al proyecto:

1. Fork el repositorio
2. Crear una rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commit los cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## 📞 Soporte

Para reportar errores o solicitar nuevas funcionalidades, crear un issue en el repositorio.

---

**Última actualización**: Enero 2024
