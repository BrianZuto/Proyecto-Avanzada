@echo off
REM Script para construir las imágenes Docker en Windows
echo 🏗️  Construyendo imágenes Docker para SneakerShop...

REM Construir imagen del backend
echo 📦 Construyendo backend...
docker build -t sneakershop-backend ./backend

REM Construir imagen del frontend
echo 📦 Construyendo frontend...
docker build -t sneakershop-frontend ./frontend/frontend-app

echo ✅ Construcción completada!
echo 🚀 Para ejecutar la aplicación, usa: docker-compose up -d
pause
