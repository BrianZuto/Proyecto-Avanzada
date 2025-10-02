#!/bin/bash

echo "Iniciando Backend en modo DESARROLLO (localhost)..."
cd backend

export SPRING_PROFILES_ACTIVE=dev
export CORS_ORIGINS=http://localhost:4200
export LOG_LEVEL=DEBUG
export SECURITY_LOG_LEVEL=DEBUG
export SHOW_SQL=true
export FORMAT_SQL=true

mvn spring-boot:run
