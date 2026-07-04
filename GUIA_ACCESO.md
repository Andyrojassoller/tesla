# Sistema de Gestión de Pedidos - Guía de Acceso

## 🔐 Credenciales de Prueba

### Usuario Administrador
- **Usuario:** `admin`
- **Contraseña:** `password123`
- **Rol:** ADMIN
- **Permisos:**
  - Acceso completo a todas las funcionalidades
  - Gestión de clientes (crear, editar, eliminar)
  - Gestión de productos (crear, editar, eliminar)
  - Gestión de pedidos (ver todos, crear, editar)
  - Ver todos los comentarios
  - Acceso al panel de administración

### Usuarios Clientes
Todos los siguientes usuarios tienen la contraseña: `password123`

1. **cliente1** - Usuario de prueba general
2. **cliente2** - Usuario de prueba general  
3. **jperez** - Juan Pérez (asociado al cliente en BD)
4. **mgarcia** - María García (asociado al cliente en BD)

**Rol:** CLIENTE
**Permisos:**
- Ver productos
- Crear pedidos
- Ver sus propios pedidos
- Agregar comentarios a productos
- Editar sus preferencias
- Ver su historial de navegación

## 🚀 Primeros Pasos

1. **Iniciar la aplicación**
   ```bash
   cd java
   mvnw spring-boot:run
   ```

2. **Acceder al sistema**
   - URL: http://localhost:8080
   - Hacer clic en "Iniciar Sesión" o ir a http://localhost:8080/login

3. **Cargar datos de prueba**
   - Los datos se pueden cargar ejecutando el script `data-test.sql`
   - O insertarlos manualmente desde pgAdmin/psql

## 📋 Funcionalidades por Rol

### ADMIN puede:
- ✅ Gestionar clientes (CRUD completo)
- ✅ Gestionar productos (CRUD completo)  
- ✅ Gestionar pedidos (ver todos, crear, editar estados)
- ✅ Ver todos los comentarios del sistema
- ✅ Eliminar comentarios inapropiados
- ✅ Agregar comentarios propios
- ✅ Editar sus preferencias
- ✅ Ver su historial de navegación

### CLIENTE puede:
- ✅ Ver catálogo de productos
- ✅ Ver detalles de productos (registra historial)
- ✅ Crear nuevos pedidos
- ✅ Ver sus propios pedidos
- ✅ Agregar comentarios con calificaciones (1-5 estrellas)
- ✅ Ver comentarios de otros usuarios
- ✅ Ver sus propios comentarios
- ✅ Editar sus preferencias (idioma, método de pago, notificaciones, categorías favoritas)
- ✅ Ver su historial de navegación completo

## 🎨 Características Implementadas

### 1. Sistema de Autenticación (Spring Security)
- Login con usuario y contraseña
- Contraseñas encriptadas con BCrypt
- Remember me funcional
- Registro de nuevos usuarios (rol CLIENTE por defecto)
- Página de perfil de usuario
- Cierre de sesión seguro

### 2. Control de Acceso por Roles
- Anotaciones `@PreAuthorize` en controladores
- Restricciones a nivel de URL en SecurityConfig
- Menú dinámico según rol (sidebar)
- Botones/acciones visibles según permisos

### 3. Gestión de Comentarios
- Los usuarios pueden comentar productos
- Sistema de calificación con estrellas (1-5)
- Calificación promedio por producto
- ADMIN puede ver y eliminar todos los comentarios
- Los usuarios ven sus propios comentarios

### 4. Gestión de Preferencias (MongoDB)
- Idioma de interfaz (es, en, pt)
- Tema (claro, oscuro, automático)
- Método de pago preferido
- Notificaciones (email, sistema)
- Newsletter
- Categorías favoritas
- Productos por página

### 5. Historial de Navegación (MongoDB)
- Registra automáticamente cuando un usuario ve un producto
- Guarda: producto, fecha/hora, IP, user agent
- Estadísticas: total visitas, productos únicos, categorías
- Vista completa del historial personal

## 🗄️ Bases de Datos

### PostgreSQL (Datos Estructurados)
- Usuarios
- Clientes
- Productos
- Pedidos
- Detalles de pedidos
- Comentarios

### MongoDB (Datos Flexibles)
- Preferencias de usuario
- Historial de navegación
- Cliente info extendida

## 🔗 URLs Importantes

| URL | Descripción | Acceso |
|-----|-------------|--------|
| `/` | Dashboard principal | Todos |
| `/login` | Página de login | Público |
| `/registro` | Registro de usuarios | Público |
| `/perfil` | Perfil de usuario | Autenticado |
| `/productos` | Catálogo de productos | Todos |
| `/productos/ver/{id}` | Detalle de producto | Todos |
| `/clientes` | Gestión de clientes | ADMIN |
| `/pedidos` | Lista de pedidos | Autenticado |
| `/pedidos/nuevo` | Crear pedido | ADMIN, CLIENTE |
| `/comentarios` | Todos los comentarios | ADMIN |
| `/comentarios/producto/{id}` | Comentarios de producto | Todos |
| `/comentarios/mis-comentarios` | Mis comentarios | Autenticado |
| `/preferencias` | Editar preferencias | Autenticado |
| `/historial` | Historial de navegación | Autenticado |

## ⚠️ Notas Importantes

1. **Seguridad:**
   - Las contraseñas de prueba son simples para facilitar testing
   - En producción, usar contraseñas fuertes y únicas
   - Cambiar el secreto de JWT/sesión en producción

2. **Base de Datos:**
   - PostgreSQL debe estar en puerto 5432
   - MongoDB debe estar en puerto 27017
   - Actualizar credenciales en `application.properties`

3. **Datos de Prueba:**
   - El script incluye 5 clientes
   - 21 productos en 5 categorías
   - 4 pedidos de ejemplo
   - 7 comentarios de prueba
   - 5 usuarios (1 admin + 4 clientes)

## 🎯 Casos de Uso para Probar

1. **Como ADMIN:**
   - Crear un nuevo producto
   - Editar el stock de productos existentes
   - Ver todos los pedidos del sistema
   - Eliminar un comentario inapropiado

2. **Como CLIENTE:**
   - Registrarse en el sistema
   - Navegar por el catálogo
   - Ver detalle de un producto (genera historial)
   - Agregar un comentario con calificación
   - Crear un nuevo pedido
   - Editar preferencias (cambiar categorías favoritas)
   - Ver historial de productos visitados

3. **Sin autenticar:**
   - Ver catálogo de productos
   - Ver comentarios (sin poder agregar)
   - Intentar acceder a áreas restringidas (redirige a login)
