#!/bin/bash

echo "========================================"
echo "   EJECUTANDO PRUEBAS JUNIT - BACKEND"
echo "========================================"
echo

echo "Limpiando y compilando el proyecto..."
mvn clean compile test-compile

if [ $? -ne 0 ]; then
    echo "ERROR: Fallo en la compilación"
    exit 1
fi

echo
echo "Ejecutando todas las pruebas..."
mvn test

if [ $? -ne 0 ]; then
    echo
    echo "========================================"
    echo "   ALGUNAS PRUEBAS FALLARON"
    echo "========================================"
    echo
    echo "Revisa los logs arriba para ver qué pruebas fallaron."
    echo
else
    echo
    echo "========================================"
    echo "   TODAS LAS PRUEBAS PASARON EXITOSAMENTE"
    echo "========================================"
    echo
fi

echo
echo "Generando reporte de cobertura..."
mvn jacoco:report

echo
echo "Reporte de cobertura generado en: target/site/jacoco/index.html"
echo
