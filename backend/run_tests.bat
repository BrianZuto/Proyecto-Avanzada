@echo off
echo ========================================
echo    EJECUTANDO PRUEBAS JUNIT - BACKEND
echo ========================================
echo.

echo Limpiando y compilando el proyecto...
call mvn clean compile test-compile

if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en la compilación
    pause
    exit /b 1
)

echo.
echo Ejecutando todas las pruebas...
call mvn test

if %ERRORLEVEL% neq 0 (
    echo.
    echo ========================================
    echo    ALGUNAS PRUEBAS FALLARON
    echo ========================================
    echo.
    echo Revisa los logs arriba para ver qué pruebas fallaron.
    echo.
) else (
    echo.
    echo ========================================
    echo    TODAS LAS PRUEBAS PASARON EXITOSAMENTE
    echo ========================================
    echo.
)

echo.
echo Generando reporte de cobertura...
call mvn jacoco:report

echo.
echo Reporte de cobertura generado en: target/site/jacoco/index.html
echo.

pause
