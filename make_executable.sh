#!/bin/bash

echo "Haciendo ejecutables los scripts de Linux..."

chmod +x start_backend_dev.sh
chmod +x start_frontend_dev.sh
chmod +x build_backend_prod.sh
chmod +x build_frontend_prod.sh

echo "Scripts ejecutables creados exitosamente!"
echo ""
echo "Scripts disponibles:"
echo "  DESARROLLO:"
echo "    ./start_backend_dev.sh    - Backend en desarrollo (mvn spring-boot:run)"
echo "    ./start_frontend_dev.sh   - Frontend en desarrollo (ng serve)"
echo ""
echo "  PRODUCCIÓN:"
echo "    ./build_backend_prod.sh   - Compilar backend a JAR"
echo "    ./build_frontend_prod.sh  - Compilar frontend optimizado"
echo ""
echo "NOTA: En producción, el backend se ejecuta como servicio systemd"
echo "      y el frontend se sirve por Apache"
