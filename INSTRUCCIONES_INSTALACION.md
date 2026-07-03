# 📦 Guía de Instalación - Sistema de Gestión de Pedidos

Esta guía te llevará paso a paso para instalar y ejecutar el sistema completo.

## ⏱️ Tiempo Estimado de Instalación

- PostgreSQL: 10-15 minutos
- MongoDB: 10-15 minutos
- Java y Maven: 5-10 minutos
- Configuración del proyecto: 5 minutos

**Total**: Aproximadamente 30-45 minutos

---

## 📋 Paso 1: Instalar PostgreSQL

### Windows

1. **Descargar PostgreSQL**:
   - Ir a: https://www.postgresql.org/download/windows/
   - Descargar el instalador EDB (versión 12 o superior)

2. **Ejecutar el instalador**:
   - Doble clic en el archivo descargado
   - Seguir el asistente de instalación
   - **IMPORTANTE**: Recordar la contraseña del usuario `postgres`

3. **Configuración durante la instalación**:
   - Puerto: `5432` (dejar por defecto)
   - Locale: Español o Default
   - Stack Builder: Puede omitirse

4. **Verificar la instalación**:
   ```cmd
   psql --version
   ```
   Debe mostrar: `psql (PostgreSQL) 12.x` o superior

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### macOS

```bash
brew install postgresql@14
brew services start postgresql@14
```

---

## 📋 Paso 2: Instalar MongoDB

### Windows

1. **Descargar MongoDB Community**:
   - Ir a: https://www.mongodb.com/try/download/community
   - Seleccionar: Windows, MSI
   - Descargar

2. **Ejecutar el instalador**:
   - Doble clic en el archivo `.msi`
   - Tipo de instalación: "Complete"
   - Instalar "MongoDB as a Service": ✓
   - Puerto: `27017` (por defecto)

3. **Instalar MongoDB Shell (mongosh)**:
   - Descargar desde: https://www.mongodb.com/try/download/shell
   - Instalar el archivo MSI

4. **Verificar la instalación**:
   ```cmd
   mongosh --version
   ```

### Linux (Ubuntu/Debian)

```bash
# Importar clave pública
wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | sudo apt-key add -

# Crear archivo de lista
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/6.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-6.0.list

# Actualizar e instalar
sudo apt update
sudo apt install -y mongodb-org

# Iniciar MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod
```

### macOS

```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

---

## 📋 Paso 3: Instalar Java (JDK)

### Windows

1. **Descargar JDK 17**:
   - Ir a: https://adoptium.net/
   - Seleccionar: Version 17 (LTS), Windows, x64
   - Descargar

2. **Ejecutar el instalador**:
   - Doble clic en el archivo `.msi`
   - Seguir el asistente
   - **Importante**: Marcar "Add to PATH"

3. **Verificar la instalación**:
   ```cmd
   java -version
   ```
   Debe mostrar: `openjdk version "17.x.x"`

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

### macOS

```bash
brew install openjdk@17
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

---

## 📋 Paso 4: Instalar Maven

### Windows

1. **Descargar Maven**:
   - Ir a: https://maven.apache.org/download.cgi
   - Descargar: `apache-maven-x.x.x-bin.zip`

2. **Instalar**:
   - Extraer el ZIP en: `C:\Program Files\Apache\maven`
   
3. **Configurar variables de entorno**:
   - Abrir "Variables de entorno del sistema"
   - Agregar a PATH: `C:\Program Files\Apache\maven\bin`

4. **Verificar**:
   ```cmd
   mvn -version
   ```

### Linux (Ubuntu/Debian)

```bash
sudo apt install maven
mvn -version
```

### macOS

```bash
brew install maven
mvn -version
```

---

## 📋 Paso 5: Configurar las Bases de Datos

### 5.1 Configurar PostgreSQL

1. **Abrir terminal/cmd**

2. **Conectar a PostgreSQL**:
   ```cmd
   psql -U postgres
   ```
   (Ingresar la contraseña configurada durante la instalación)

3. **Crear la base de datos**:
   ```sql
   CREATE DATABASE sistema_pedidos;
   \q
   ```

4. **Navegar a la carpeta del proyecto**:
   ```cmd
   cd "C:\Users\Jose\Downloads\proyecto bd"
   ```

5. **Ejecutar los scripts SQL en orden**:
   ```cmd
   psql -U postgres -d sistema_pedidos -f sql\01_crear_tablas.sql
   psql -U postgres -d sistema_pedidos -f sql\02_procedimientos_almacenados.sql
   psql -U postgres -d sistema_pedidos -f sql\03_triggers.sql
   psql -U postgres -d sistema_pedidos -f sql\04_vistas.sql
   psql -U postgres -d sistema_pedidos -f sql\05_datos_prueba.sql
   ```

6. **Verificar que todo se creó correctamente**:
   ```cmd
   psql -U postgres -d sistema_pedidos
   ```
   ```sql
   \dt  -- Listar tablas (debe mostrar 5 tablas)
   SELECT COUNT(*) FROM clientes;  -- Debe mostrar 15
   SELECT COUNT(*) FROM productos;  -- Debe mostrar 25
   \q
   ```

### 5.2 Configurar MongoDB

1. **Abrir terminal/cmd**

2. **Conectar a MongoDB**:
   ```cmd
   mongosh
   ```

3. **Seleccionar base de datos**:
   ```javascript
   use sistema_pedidos
   ```

4. **Ejecutar los scripts de MongoDB**:
   
   En Windows:
   ```cmd
   mongosh sistema_pedidos --file "C:\Users\Jose\Downloads\proyecto bd\mongodb\01_crear_coleccion.js"
   ```

5. **Verificar la creación**:
   ```cmd
   mongosh
   ```
   ```javascript
   use sistema_pedidos
   db.clientes_info.countDocuments()  // Debe mostrar 11
   db.clientes_info.findOne()
   ```

---

## 📋 Paso 6: Configurar la Aplicación Java

1. **Navegar al directorio Java**:
   ```cmd
   cd "C:\Users\Jose\Downloads\proyecto bd\java"
   ```

2. **Editar configuración de conexión** (solo si usas credenciales diferentes):
   
   Archivo: `src\main\java\com\sistema\pedidos\config\DatabaseConfig.java`
   
   ```java
   // Cambiar si es necesario
   public static final String PG_PASSWORD = "tu_password";
   ```

3. **Compilar el proyecto**:
   ```cmd
   mvn clean compile
   ```
   
   **Nota**: La primera vez descargará todas las dependencias (puede tomar varios minutos)

4. **Verificar que no hay errores**:
   - Debe terminar con: `BUILD SUCCESS`

---

## 📋 Paso 7: Ejecutar la Aplicación

### Opción 1: Ejecutar directamente con Maven

```cmd
cd "C:\Users\Jose\Downloads\proyecto bd\java"
mvn exec:java -Dexec.mainClass="com.sistema.pedidos.Main"
```

### Opción 2: Generar JAR y ejecutar

1. **Generar el JAR**:
   ```cmd
   mvn clean package
   ```

2. **Ejecutar el JAR**:
   ```cmd
   java -jar target\sistema-pedidos-1.0-SNAPSHOT.jar
   ```

---

## ✅ Verificación de Instalación

Si todo está correcto, debes ver:

```
[INFO] === SISTEMA DE GESTIÓN DE PEDIDOS ===
[INFO] Iniciando aplicación...
[INFO] ✓ Conexión a PostgreSQL establecida
[INFO] ✓ Conexión a MongoDB establecida

╔═══════════════════════════════════════════════╗
║     SISTEMA DE GESTIÓN DE PEDIDOS             ║
╠═══════════════════════════════════════════════╣
║  1. Gestión de Clientes (PostgreSQL)         ║
║  2. Comentarios y Preferencias (MongoDB)      ║
║  3. Vista Integrada de Datos                  ║
║  4. Reportes y Estadísticas                   ║
║  0. Salir                                     ║
╚═══════════════════════════════════════════════╝
Seleccione una opción:
```

---

## 🐛 Solución de Problemas Comunes

### Error: "psql: error: connection to server failed"

**Causa**: PostgreSQL no está ejecutándose

**Solución**:
```cmd
# Windows
net start postgresql-x64-14

# Linux
sudo systemctl start postgresql
```

### Error: "MongoTimeoutException"

**Causa**: MongoDB no está ejecutándose

**Solución**:
```cmd
# Windows
net start MongoDB

# Linux
sudo systemctl start mongod
```

### Error: "Authentication failed"

**Causa**: Contraseña incorrecta en la configuración

**Solución**: Editar `DatabaseConfig.java` y actualizar las credenciales

### Error: "Could not find or load main class"

**Causa**: Proyecto no compilado correctamente

**Solución**:
```cmd
mvn clean compile
mvn package
```

### Error: "Port 5432 is already in use"

**Causa**: Otra instancia de PostgreSQL está ejecutándose

**Solución**: Cambiar el puerto en `DatabaseConfig.java` o detener la otra instancia

---

## 🎯 Próximos Pasos

Una vez instalado todo:

1. **Explorar los datos de prueba**:
   - Opción 1 en el menú → Listar clientes
   - Opción 3 → Ver información integrada de un cliente

2. **Probar funcionalidades**:
   - Agregar un nuevo cliente
   - Agregar comentarios
   - Ver vistas y reportes

3. **Revisar los logs**:
   - Los logs se guardan en: `java/logs/sistema-pedidos.log`

---

## 📞 ¿Necesitas Ayuda?

Si encuentras problemas durante la instalación:

1. Verifica que todos los servicios estén ejecutándose
2. Revisa los logs en `java/logs/`
3. Verifica las credenciales en `DatabaseConfig.java`
4. Asegúrate de que los puertos 5432 y 27017 estén libres

---

**¡Instalación Completada!** 🎉

Ahora puedes usar el sistema completo de gestión de pedidos.
