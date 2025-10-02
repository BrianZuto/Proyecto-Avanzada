#!/bin/bash

echo "=== COMPILANDO FRONTEND PARA PRODUCCIÓN ==="

cd frontend/frontend-app

echo "Instalando dependencias..."
npm install

echo "Compilando frontend para producción (optimizado)..."
npm run build --prod

if [ $? -eq 0 ]; then
    echo "✅ Frontend compilado exitosamente!"
    echo "📁 Archivos generados en: frontend/frontend-app/dist/"
    echo ""
    echo "Los archivos están listos para ser servidos por Apache"
    echo "Copia el contenido de 'dist/' al directorio web de Apache"
    echo ""
    echo "Ejemplo:"
    echo "  sudo cp -r frontend/frontend-app/dist/* /var/www/html/"
else
    echo "❌ Error al compilar el frontend"
    exit 1
fi
