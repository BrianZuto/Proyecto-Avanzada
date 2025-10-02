#!/bin/bash

echo "=== COMPILANDO BACKEND PARA PRODUCCIÓN ==="
echo "Configurando variables de entorno para producción..."

cd backend

# Configurar variables de entorno para producción
export SPRING_PROFILES_ACTIVE=prod
export CORS_ORIGINS=http://13.61.162.154:4200,http://localhost:4200
export LOG_LEVEL=INFO
export SECURITY_LOG_LEVEL=WARN
export SHOW_SQL=false
export FORMAT_SQL=false

echo "Limpiando compilaciones anteriores..."
mvn clean

echo "Compilando JAR para producción..."
mvn package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Backend compilado exitosamente!"
    echo "📁 JAR generado en: backend/target/"
    echo ""
    echo "Para ejecutar en producción:"
    echo "  java -jar backend/target/backend-0.0.1-SNAPSHOT.jar"
    echo ""
    echo "O usar el servicio systemd (recomendado):"
    echo "  sudo systemctl start backend-app"
    echo "  sudo systemctl enable backend-app"
else
    echo "❌ Error al compilar el backend"
    exit 1
fi
