@echo off
REM Script para iniciar la aplicación con Docker Compose en Windows
echo 🚀 Iniciando SneakerShop con Docker Compose...

REM Verificar si Docker está ejecutándose
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker no está ejecutándose. Por favor, inicia Docker primero.
    pause
    exit /b 1
)

REM Iniciar los servicios
echo 📦 Iniciando servicios...
docker-compose up -d

REM Esperar a que los servicios estén listos
echo ⏳ Esperando a que los servicios estén listos...
timeout /t 10 /nobreak >nul

REM Mostrar estado de los contenedores
echo 📊 Estado de los contenedores:
docker-compose ps

echo.
echo ✅ Aplicación iniciada!
echo 🌐 Frontend: http://localhost:4200
echo 🔧 Backend API: http://localhost:8080
echo 📚 Swagger UI: http://localhost:8080/swagger-ui.html
echo 🗄️  Adminer (DB): http://localhost:8081
echo.
echo 📝 Para ver logs: docker-compose logs -f
echo 🛑 Para detener: docker-compose down
pause
