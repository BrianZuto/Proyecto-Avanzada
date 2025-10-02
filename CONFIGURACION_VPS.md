# Configuración para VPS y Localhost

Este proyecto está configurado para funcionar tanto en localhost (desarrollo) como en tu VPS con IP `13.61.162.154` (producción).

## Configuración del Backend

### Variables de Entorno Disponibles

- `SPRING_PROFILES_ACTIVE`: Perfil activo (dev/prod)
- `CORS_ORIGINS`: URLs permitidas para CORS
- `LOG_LEVEL`: Nivel de logging
- `SECURITY_LOG_LEVEL`: Nivel de logging de seguridad
- `SHOW_SQL`: Mostrar consultas SQL
- `FORMAT_SQL`: Formatear consultas SQL

### Archivos de Configuración

- `application.properties`: Configuración base con variables de entorno
- `application-dev.properties`: Configuración para desarrollo local
- `application-prod.properties`: Configuración para producción (VPS)

### Scripts de Inicio

#### Desarrollo (Localhost)
```bash
start_backend_dev.bat
```

#### Producción (VPS) - CORRECTO
```bash
build_backend_prod.bat  # Compila a JAR
# Luego ejecutar como servicio systemd
```

## Configuración del Frontend

### Archivos de Entorno

- `src/environments/environment.ts`: Configuración para desarrollo
- `src/environments/environment.prod.ts`: Configuración para producción

### URLs Configuradas

#### Desarrollo
- Backend: `http://localhost:8080/api`
- Frontend: `http://localhost:4200`

#### Producción (VPS)
- Backend: `http://13.61.162.154:8080/api`
- Frontend: `http://13.61.162.154` (servido por Apache)

### Scripts de Inicio

#### Desarrollo (Localhost)
```bash
start_frontend_dev.bat
```

#### Producción (VPS) - CORRECTO
```bash
build_frontend_prod.bat  # Compila optimizado
# Los archivos se sirven por Apache
```

## CORS Configurado

El backend está configurado para permitir peticiones desde:
- `http://localhost:4200` (desarrollo)
- `http://13.61.162.154` (producción - Apache)

## Instrucciones de Despliegue

### Para Desarrollo Local
1. Ejecutar `start_backend_dev.bat`
2. Ejecutar `start_frontend_dev.bat`
3. Acceder a `http://localhost:4200`

### Para Producción en VPS (CORRECTO)
1. Ejecutar `build_backend_prod.bat` (compila a JAR)
2. Ejecutar `build_frontend_prod.bat` (compila optimizado)
3. Ejecutar backend como servicio systemd
4. El frontend se sirve automáticamente por Apache
5. Acceder a `http://13.61.162.154`

## Notas Importantes

- **DESARROLLO**: El frontend usa `ng serve` en puerto 4200, backend usa `mvn spring-boot:run` en puerto 8080
- **PRODUCCIÓN**: El frontend se compila y sirve por Apache en puerto 80, backend se compila a JAR y ejecuta como servicio systemd en puerto 8080
- Los servicios del frontend usan automáticamente las URLs correctas según el entorno
- La configuración CORS permite ambas IPs simultáneamente
- **NO uses `ng serve` ni `mvn spring-boot:run` en producción** - solo para desarrollo

## Variables de Entorno para VPS

Si necesitas configurar variables de entorno en tu VPS, puedes usar:

```bash
export SPRING_PROFILES_ACTIVE=prod
export CORS_ORIGINS=http://13.61.162.154,http://localhost:4200
export LOG_LEVEL=INFO
export SECURITY_LOG_LEVEL=WARN
export SHOW_SQL=false
export FORMAT_SQL=false
```
