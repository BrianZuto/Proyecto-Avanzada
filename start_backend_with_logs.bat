@echo off
echo ========================================
echo    INICIANDO BACKEND SPRING BOOT
echo ========================================
echo.
echo Configuracion:
echo - Puerto: 8080
echo - Base de datos: MySQL (proyecto_avanzada)
echo - Creacion automatica de tablas: ACTIVADA
echo.
echo Presiona Ctrl+C para detener el servidor
echo ========================================
echo.

cd backend
echo Compilando y ejecutando Spring Boot backend...
echo.
mvn spring-boot:run

echo.
echo ========================================
echo    BACKEND DETENIDO
echo ========================================
pause
