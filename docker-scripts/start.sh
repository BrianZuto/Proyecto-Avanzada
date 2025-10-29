#!/bin/bash

# Script para iniciar la aplicación con Docker Compose
echo "🚀 Iniciando SneakerShop con Docker Compose..."

# Verificar si Docker está ejecutándose
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker no está ejecutándose. Por favor, inicia Docker primero."
    exit 1
fi

# Iniciar los servicios
echo "📦 Iniciando servicios..."
docker-compose up -d

# Esperar a que los servicios estén listos
echo "⏳ Esperando a que los servicios estén listos..."
sleep 10

# Mostrar estado de los contenedores
echo "📊 Estado de los contenedores:"
docker-compose ps

echo ""
echo "✅ Aplicación iniciada!"
echo "🌐 Frontend: http://localhost:4200"
echo "🔧 Backend API: http://localhost:8080"
echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "🗄️  Adminer (DB): http://localhost:8081"
echo ""
echo "📝 Para ver logs: docker-compose logs -f"
echo "🛑 Para detener: docker-compose down"
