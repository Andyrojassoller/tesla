@echo off
REM Script para crear la base de datos y las tablas
REM Ejecutar este script ANTES de iniciar la aplicación

echo =====================================================
echo   CREACION DE BASE DE DATOS
echo =====================================================
echo.

REM Verificar PostgreSQL
echo Verificando PostgreSQL...
echo [INFO] Intentando conectar a PostgreSQL...

echo.
echo Ingresa la contraseña de PostgreSQL cuando se solicite
echo.

REM Buscar psql en ubicaciones comunes
set PSQL=psql
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" set PSQL="C:\Program Files\PostgreSQL\16\bin\psql.exe"
if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" set PSQL="C:\Program Files\PostgreSQL\15\bin\psql.exe"
if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" set PSQL="C:\Program Files\PostgreSQL\14\bin\psql.exe"
if exist "C:\Program Files\PostgreSQL\13\bin\psql.exe" set PSQL="C:\Program Files\PostgreSQL\13\bin\psql.exe"
if exist "C:\Program Files\PostgreSQL\12\bin\psql.exe" set PSQL="C:\Program Files\PostgreSQL\12\bin\psql.exe"

echo Usando: %PSQL%
echo.

REM Crear la base de datos (si no existe)
echo [1/6] Creando base de datos sistema_pedidos...
%PSQL% -U postgres -c "CREATE DATABASE sistema_pedidos;" 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] Base de datos creada
) else (
    echo [INFO] Base de datos ya existe
)

REM Ejecutar scripts SQL en orden
echo.
echo [2/6] Creando tablas...
%PSQL% -U postgres -d sistema_pedidos -f sql\01_crear_tablas.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] No se pudieron crear las tablas
    pause
    exit /b 1
)
echo [OK] Tablas creadas

echo.
echo [3/6] Creando procedimientos almacenados...
%PSQL% -U postgres -d sistema_pedidos -f sql\02_procedimientos_almacenados.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] Error al crear procedimientos
)

echo.
echo [4/6] Creando triggers...
%PSQL% -U postgres -d sistema_pedidos -f sql\03_triggers.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] Error al crear triggers
)

echo.
echo [5/6] Creando vistas...
%PSQL% -U postgres -d sistema_pedidos -f sql\04_vistas.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] Error al crear vistas
)

echo.
echo [6/6] Cargando datos de prueba...
%PSQL% -U postgres -d sistema_pedidos -f sql\05_datos_prueba.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] Error al cargar datos de prueba
)

echo.
echo =====================================================
echo   BASE DE DATOS LISTA!
echo =====================================================
echo.
echo Ahora puedes ejecutar la aplicacion con: ejecutar.bat
echo.

pause
