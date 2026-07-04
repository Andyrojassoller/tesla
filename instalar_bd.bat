@echo off
REM Script para instalar las bases de datos del Sistema de Gestión de Pedidos
REM Este script ejecuta todos los scripts SQL y MongoDB necesarios

echo =====================================================
echo   INSTALACION DE BASES DE DATOS
echo   Sistema de Gestion de Pedidos
echo =====================================================
echo.

REM Solicitar credenciales de PostgreSQL
set /p PG_USER="Ingrese el usuario de PostgreSQL (por defecto: postgres): " || set PG_USER=postgres
set /p PG_PASSWORD="Ingrese la contrasena de PostgreSQL: "

echo.
echo [PASO 1] Creando base de datos en PostgreSQL...
echo.

REM Verificar si la base de datos existe
psql -U %PG_USER% -lqt | find "sistema_pedidos" >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [ADVERTENCIA] La base de datos 'sistema_pedidos' ya existe
    set /p RECREAR="¿Desea eliminarla y recrearla? (S/N): "
    if /i "%RECREAR%"=="S" (
        echo Eliminando base de datos existente...
        psql -U %PG_USER% -c "DROP DATABASE sistema_pedidos;"
    ) else (
        echo Continuando sin recrear la base de datos...
        goto SKIP_CREATE_DB
    )
)

echo Creando base de datos...
psql -U %PG_USER% -c "CREATE DATABASE sistema_pedidos;"
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] No se pudo crear la base de datos
    pause
    exit /b 1
)
echo [OK] Base de datos creada

:SKIP_CREATE_DB

echo.
echo [PASO 2] Ejecutando scripts SQL...
echo.

echo [2.1] Creando tablas...
psql -U %PG_USER% -d sistema_pedidos -f sql\01_crear_tablas.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Error al crear tablas
    pause
    exit /b 1
)
echo [OK] Tablas creadas

echo [2.2] Creando procedimientos almacenados...
psql -U %PG_USER% -d sistema_pedidos -f sql\02_procedimientos_almacenados.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Error al crear procedimientos
    pause
    exit /b 1
)
echo [OK] Procedimientos creados

echo [2.3] Creando triggers...
psql -U %PG_USER% -d sistema_pedidos -f sql\03_triggers.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Error al crear triggers
    pause
    exit /b 1
)
echo [OK] Triggers creados

echo [2.4] Creando vistas...
psql -U %PG_USER% -d sistema_pedidos -f sql\04_vistas.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Error al crear vistas
    pause
    exit /b 1
)
echo [OK] Vistas creadas

echo [2.5] Insertando datos de prueba...
psql -U %PG_USER% -d sistema_pedidos -f sql\05_datos_prueba.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Error al insertar datos de prueba
    pause
    exit /b 1
)
echo [OK] Datos de prueba insertados

echo.
echo [PASO 3] Configurando MongoDB...
echo.

echo [3.1] Creando coleccion y datos...
mongosh sistema_pedidos --file mongodb\01_crear_coleccion.js
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Error al crear coleccion en MongoDB
    pause
    exit /b 1
)
echo [OK] Coleccion creada en MongoDB

echo.
echo =====================================================
echo   INSTALACION COMPLETADA EXITOSAMENTE
echo =====================================================
echo.
echo PostgreSQL:
echo   - Base de datos: sistema_pedidos
echo   - Tablas: 5
echo   - Procedimientos: 5
echo   - Triggers: 7
echo   - Vistas: 7
echo   - Clientes de prueba: 15
echo   - Productos de prueba: 25
echo.
echo MongoDB:
echo   - Base de datos: sistema_pedidos
echo   - Coleccion: clientes_info
echo   - Documentos de prueba: 11
echo.
echo Puede ejecutar la aplicacion con: ejecutar.bat
echo.

pause
