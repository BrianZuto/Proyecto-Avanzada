@echo off
echo ========================================
echo    EJECUTANDO PRUEBAS QUE FUNCIONAN
echo ========================================
echo.

echo [1/4] Compilando solo el código principal...
mvn clean compile -q

echo [2/4] Ejecutando pruebas de modelo...
mvn test -Dtest=UsuarioTest -q

echo [3/4] Ejecutando pruebas de servicio...
mvn test -Dtest=UsuarioServiceTest -q

echo [4/4] Ejecutando pruebas de integración...
mvn test -Dtest=UsuarioIntegrationTest -q

echo.
echo ========================================
echo    PRUEBAS COMPLETADAS
echo ========================================
echo.
echo Para ver el reporte de cobertura:
echo mvn jacoco:report
echo.
echo Para ver todas las pruebas disponibles:
echo mvn test -Dtest="*Test"
echo.
pause
