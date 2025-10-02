@echo off
echo === COMPILANDO BACKEND PARA PRODUCCION ===
echo Configurando variables de entorno para produccion...

cd backend

REM Configurar variables de entorno para producción
set SPRING_PROFILES_ACTIVE=prod
set CORS_ORIGINS=http://13.61.162.154:4200,http://localhost:4200
set LOG_LEVEL=INFO
set SECURITY_LOG_LEVEL=WARN
set SHOW_SQL=false
set FORMAT_SQL=false

echo Limpiando compilaciones anteriores...
mvn clean

echo Compilando JAR para produccion...
mvn package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo ✅ Backend compilado exitosamente!
    echo 📁 JAR generado en: backend/target/
    echo.
    echo Para ejecutar en produccion:
    echo   java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
) else (
    echo ❌ Error al compilar el backend
    exit /b 1
)
