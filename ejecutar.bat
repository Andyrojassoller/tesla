@echo off
REM Script para ejecutar el Sistema de Gestión de Pedidos (Spring Boot Web)
REM Autor: Sistema de Gestión de Pedidos
REM Fecha: 2024

echo =====================================================
echo   SISTEMA DE GESTION DE PEDIDOS - WEB APP
echo =====================================================
echo.

REM Verificar que Java está instalado
echo [1/4] Verificando Java...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java no esta instalado o no esta en el PATH
    echo Por favor instale Java 17 o superior
    pause
    exit /b 1
)
echo [OK] Java encontrado

REM Verificar que Maven está instalado
echo [2/4] Verificando Maven...
mvn -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven no esta instalado o no esta en el PATH
    echo Por favor instale Maven 3.6 o superior
    pause
    exit /b 1
)
echo [OK] Maven encontrado

REM Verificar que PostgreSQL está ejecutándose
echo [3/4] Verificando PostgreSQL...
pg_isready >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] No se puede conectar a PostgreSQL
    echo Asegurese de que PostgreSQL este ejecutandose en el puerto 5432
    pause
)
echo [OK] PostgreSQL ejecutandose

REM Verificar que MongoDB está ejecutándose
echo [4/4] Verificando MongoDB...
mongosh --eval "db.adminCommand('ping')" >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] No se puede conectar a MongoDB
    echo Asegurese de que MongoDB este ejecutandose en el puerto 27017
    pause
)
echo [OK] MongoDB ejecutandose

echo.
echo =====================================================
echo   Iniciando aplicacion web...
echo =====================================================
echo.
echo La aplicacion se abrira en: http://localhost:8080
echo Presiona Ctrl+C para detener el servidor
echo.

cd java
mvn spring-boot:run

pause
