@echo off
echo Iniciando Backend en modo DESARROLLO (localhost)...
cd backend
set SPRING_PROFILES_ACTIVE=dev
set CORS_ORIGINS=http://localhost:4200
set LOG_LEVEL=DEBUG
set SECURITY_LOG_LEVEL=DEBUG
set SHOW_SQL=true
set FORMAT_SQL=true
mvn spring-boot:run
