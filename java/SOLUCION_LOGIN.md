# 🔧 Solución de Problemas - Login y Registro

## Problema: No se puede hacer login ni registrar usuarios

### ✅ Pasos para Solucionar

#### 1. Verificar que PostgreSQL esté corriendo
```bash
# En Windows, verificar el servicio
services.msc
# Buscar "postgresql" y asegurarse que esté iniciado
```

#### 2. Crear la base de datos (si no existe)
```sql
-- Conectarse a PostgreSQL como usuario postgres
psql -U postgres

-- Crear la base de datos
CREATE DATABASE sistema_pedidos;

-- Salir
\q
```

#### 3. Ejecutar el script de inicialización de usuarios
```bash
# Conectarse a la base de datos
psql -U postgres -d sistema_pedidos

# Ejecutar el script
\i "C:/Users/Jose/Downloads/proyecto bd/java/init-usuarios.sql"

# Verificar que se crearon los usuarios
SELECT * FROM usuarios;

# Deberías ver:
# id_usuario | nombre_usuario | rol    | activo
# -----------+----------------+--------+--------
#          1 | admin          | ADMIN  | true
#          2 | cliente1       | CLIENTE| true
#          3 | cliente2       | CLIENTE| true
```

#### 4. Verificar application.properties
Asegúrate de que las credenciales sean correctas:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_pedidos
spring.datasource.username=postgres
spring.datasource.password=admin
```

#### 5. Reiniciar la aplicación
```bash
# Detener la aplicación si está corriendo
# Ctrl+C en la terminal

# Iniciar nuevamente
cd "C:/Users/Jose/Downloads/proyecto bd/java"
mvnw spring-boot:run
```

#### 6. Probar el login
```
URL: http://localhost:8080/login
Usuario: admin
Contraseña: password123
```

### 🐛 Verificar Logs

Si sigue sin funcionar, revisar los logs de la aplicación:

```bash
# Buscar mensajes como:
# "Intentando cargar usuario: admin"
# "Usuario encontrado: admin, rol: ADMIN, activo: true"
# "Autoridades asignadas: [ROLE_ADMIN]"
```

### ❌ Errores Comunes

#### Error 1: "Usuario no encontrado"
**Causa:** La tabla usuarios no existe o está vacía
**Solución:** Ejecutar el script `init-usuarios.sql`

#### Error 2: "Access denied for user"
**Causa:** Credenciales incorrectas en application.properties
**Solución:** Verificar usuario y contraseña de PostgreSQL

#### Error 3: "relation 'usuarios' does not exist"
**Causa:** La base de datos no se ha inicializado
**Solución:** 
- Verificar que `spring.jpa.hibernate.ddl-auto=update` en application.properties
- O ejecutar el script SQL manualmente

#### Error 4: "Bad credentials"
**Causa:** La contraseña no coincide con el hash BCrypt
**Solución:** Asegurarse de usar "password123" o verificar el hash en la BD

### 🔍 Verificación Manual de Contraseña

Para verificar si la contraseña está correcta:

```java
// Crear un pequeño test
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String password = "password123";
String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu";
boolean matches = encoder.matches(password, hash);
System.out.println("Coincide: " + matches); // Debe imprimir: true
```

### 📝 Credenciales de Prueba

**Usuario Administrador:**
- Usuario: `admin`
- Contraseña: `password123`
- Rol: ADMIN

**Usuarios Cliente:**
- Usuario: `cliente1` o `cliente2`
- Contraseña: `password123`
- Rol: CLIENTE

### 🔄 Reset Completo (Si todo falla)

```sql
-- Conectarse a PostgreSQL
psql -U postgres -d sistema_pedidos

-- Eliminar la tabla de usuarios
DROP TABLE IF EXISTS usuarios CASCADE;

-- Volver a crear la tabla
CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP,
    ultimo_acceso TIMESTAMP
);

-- Insertar usuario admin
INSERT INTO usuarios (nombre_usuario, contrasena, rol, activo, fecha_creacion) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu', 'ADMIN', true, NOW());

-- Verificar
SELECT * FROM usuarios;
```

### 📞 Siguiente Paso

Después de verificar que el login funciona con el usuario `admin`, puedes:
1. Probar el registro de nuevos usuarios desde la interfaz
2. Cargar el resto de datos de prueba con `data-test.sql`
3. Explorar todas las funcionalidades del sistema

### ⚠️ Nota Importante

La contraseña `password123` está encriptada con BCrypt. El hash que se almacena en la base de datos es:
```
$2a$10$N9qo8uLOickgx2ZMRZoMye/h6FKFZzH4YGkQZuLtqHQ6K0VhGvXGu
```

NO cambies este hash manualmente, siempre debe ser generado por BCrypt.
