@echo off
echo === COMPILANDO FRONTEND PARA PRODUCCION ===

cd frontend\frontend-app

echo Instalando dependencias...
npm install

echo Compilando frontend para produccion (optimizado)...
npm run build --prod

if %ERRORLEVEL% EQU 0 (
    echo ✅ Frontend compilado exitosamente!
    echo 📁 Archivos generados en: frontend\frontend-app\dist\
    echo.
    echo Los archivos estan listos para ser servidos por Apache
    echo Copia el contenido de 'dist\' al directorio web de Apache
) else (
    echo ❌ Error al compilar el frontend
    exit /b 1
)
