# Sistema de Gestión de Pedidos - Aplicación Web

Sistema completo de gestión de pedidos con arquitectura Spring Boot, bases de datos híbridas (PostgreSQL + MongoDB) e interfaz web moderna.

## 🚀 Características

- **Interfaz Web Moderna**: Frontend responsivo con Bootstrap 5 y Thymeleaf
- **Arquitectura Spring Boot**: Framework empresarial con inyección de dependencias
- **Bases de Datos Híbridas**: 
  - PostgreSQL para datos transaccionales
  - MongoDB para información dinámica de clientes
- **Gestión Completa**:
  - CRUD de Clientes
  - CRUD de Productos con control de stock
  - Gestión de Pedidos con validación de stock
  - Dashboard con estadísticas en tiempo real

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.6+**
- **PostgreSQL 14+** (puerto 5432)
- **MongoDB 6.0+** (puerto 27017)
- Navegador web moderno

## 🛠️ Instalación

### 1. Clonar/Descargar el Proyecto

```bash
cd "c:\Users\Jose\Downloads\proyecto bd"
```

### 2. Configurar Base de Datos PostgreSQL

```sql
-- Ejecutar en psql o pgAdmin
CREATE DATABASE sistema_pedidos;

-- Conectarse a la base de datos
\c sistema_pedidos

-- Ejecutar el script de creación (postgresql/estructura.sql)
\i postgresql/estructura.sql

-- (Opcional) Cargar datos de prueba
\i postgresql/datos_prueba.sql
```

### 3. Verificar MongoDB

Asegúrate de que MongoDB esté ejecutándose:

```bash
mongosh --eval "db.adminCommand('ping')"
```

La base de datos `pedidos_db` y la colección `clientes_info` se crearán automáticamente.

### 4. Compilar el Proyecto

```bash
cd java
mvn clean install
```

## ▶️ Ejecución

### Opción 1: Script Automático (Windows)

```bash
ejecutar.bat
```

### Opción 2: Maven Directamente

```bash
cd java
mvn spring-boot:run
```

### Opción 3: JAR Compilado

```bash
cd java
mvn package
java -jar target/sistema-gestion-pedidos-1.0-SNAPSHOT.jar
```

## 🌐 Acceso a la Aplicación

Una vez iniciada, abre tu navegador en:

```
http://localhost:8080
```

### Páginas Disponibles

- **Dashboard**: `/` - Estadísticas y acceso rápido
- **Clientes**: `/clientes` - Gestión de clientes
- **Productos**: `/productos` - Catálogo de productos
- **Pedidos**: `/pedidos` - Gestión de pedidos

## 🏗️ Estructura del Proyecto

```
proyecto bd/
├── java/
│   └── src/main/
│       ├── java/com/sistema/pedidos/
│       │   ├── entity/               # Entidades JPA
│       │   │   ├── Cliente.java
│       │   │   ├── Producto.java
│       │   │   ├── Pedido.java
│       │   │   ├── DetallePedido.java
│       │   │   └── Usuario.java
│       │   ├── document/             # Documentos MongoDB
│       │   │   └── ClienteInfo.java
│       │   ├── repository/           # Repositorios Spring Data
│       │   │   ├── ClienteRepository.java
│       │   │   ├── ProductoRepository.java
│       │   │   ├── PedidoRepository.java
│       │   │   ├── DetallePedidoRepository.java
│       │   │   ├── UsuarioRepository.java
│       │   │   └── ClienteInfoRepository.java
│       │   ├── service/              # Lógica de negocio
│       │   │   ├── NewClienteService.java
│       │   │   ├── ProductoService.java
│       │   │   └── PedidoService.java
│       │   ├── controller/           # Controladores web
│       │   │   ├── HomeController.java
│       │   │   ├── ClienteController.java
│       │   │   ├── ProductoController.java
│       │   │   └── PedidoController.java
│       │   └── SistemaGestionPedidosApplication.java
│       ├── resources/
│       │   ├── templates/            # Vistas Thymeleaf
│       │   │   ├── layout.html
│       │   │   ├── index.html
│       │   │   ├── clientes/
│       │   │   ├── productos/
│       │   │   └── pedidos/
│       │   └── application.properties
│       └── pom.xml
├── postgresql/
│   ├── estructura.sql
│   └── datos_prueba.sql
├── mongodb/
│   └── estructura.js
└── README_WEB.md
```

## 🔧 Configuración

Edita `java/src/main/resources/application.properties` para cambiar:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_pedidos
spring.datasource.username=postgres
spring.datasource.password=tu_password

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/pedidos_db

# Puerto del servidor
server.port=8080
```

## 📊 Stack Tecnológico

### Backend
- **Spring Boot 3.2.0**: Framework principal
- **Spring Data JPA**: ORM para PostgreSQL
- **Spring Data MongoDB**: Integración MongoDB
- **Hibernate**: Proveedor JPA
- **Lombok**: Reducción de boilerplate

### Frontend
- **Thymeleaf**: Motor de plantillas
- **Bootstrap 5.3.2**: Framework CSS
- **jQuery 3.7.1**: Biblioteca JavaScript
- **WebJars**: Gestión de dependencias frontend

### Bases de Datos
- **PostgreSQL**: Datos transaccionales (clientes, productos, pedidos)
- **MongoDB**: Datos dinámicos (comentarios, preferencias, historial)

## 🎯 Funcionalidades Principales

### Gestión de Clientes
- ✅ Crear nuevo cliente (PostgreSQL + MongoDB)
- ✅ Editar información del cliente
- ✅ Ver historial de pedidos
- ✅ Buscar clientes por nombre o email
- ✅ Eliminar cliente (con validación de pedidos)

### Gestión de Productos
- ✅ CRUD completo de productos
- ✅ Control de stock con alertas visuales
- ✅ Filtrado por categoría
- ✅ Activación/desactivación de productos
- ✅ Indicadores de bajo stock (<10 unidades)

### Gestión de Pedidos
- ✅ Crear pedido con validación de stock
- ✅ Actualización automática de stock
- ✅ Cambio de estado (Pendiente/En Proceso/Completado/Cancelado)
- ✅ Restauración de stock al cancelar
- ✅ Cálculo automático de totales
- ✅ Filtrado por estado

### Dashboard
- ✅ Total de clientes registrados
- ✅ Total de productos activos
- ✅ Pedidos pendientes
- ✅ Total de ventas
- ✅ Accesos rápidos a funciones

## 🧪 Pruebas

### Ejecutar Tests
```bash
cd java
mvn test
```

### Verificar Compilación
```bash
mvn clean compile
```

## 📝 Logs

Los logs se guardan en:
```
java/logs/app.log
```

Nivel de log configurable en `application.properties`:
```properties
logging.level.com.sistema.pedidos=DEBUG
```

## 🐛 Solución de Problemas

### Error de Conexión PostgreSQL
```bash
# Verificar estado
pg_isready

# Verificar puerto
netstat -an | findstr 5432
```

### Error de Conexión MongoDB
```bash
# Verificar estado
mongosh --eval "db.adminCommand('ping')"

# Verificar puerto
netstat -an | findstr 27017
```

### Puerto 8080 en Uso
Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### Errores de Compilación Maven
```bash
# Limpiar y recompilar
mvn clean install -U

# Forzar actualización de dependencias
mvn clean install -U -DskipTests
```

## 📈 Próximas Mejoras

- [ ] Sistema de autenticación y autorización
- [ ] API REST para integración con móviles
- [ ] Reportes en PDF/Excel
- [ ] Notificaciones por email
- [ ] Integración con pasarelas de pago
- [ ] Módulo de inventario avanzado
- [ ] Estadísticas con gráficos (Chart.js)

## 👥 Autor

Sistema de Gestión de Pedidos - 2024

## 📄 Licencia

Proyecto académico - Uso educativo

---

**¡Aplicación lista para usar!** 🎉

Para iniciar: `ejecutar.bat` → Abrir navegador en http://localhost:8080
