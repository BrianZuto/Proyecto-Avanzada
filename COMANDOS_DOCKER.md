# 🐳 Comandos Docker - Proyecto Avanzada

## 📋 Índice
- [Comandos Básicos de Docker](#comandos-básicos-de-docker)
- [Comandos de Docker Compose](#comandos-de-docker-compose)
- [Comandos de Base de Datos](#comandos-de-base-de-datos)
- [Comandos de Limpieza](#comandos-de-limpieza)
- [Comandos de Diagnóstico](#comandos-de-diagnóstico)
- [Acceso al Proyecto](#acceso-al-proyecto)

---

## 🚀 Comandos Básicos de Docker

### Iniciar Docker Desktop
```bash
# En Windows (si no está ejecutándose automáticamente)
# Abrir Docker Desktop desde el menú inicio
```

### Verificar que Docker esté funcionando
```bash
docker --version
docker-compose --version
```

---

## 🐙 Comandos de Docker Compose

### Iniciar todos los servicios
```bash
# Iniciar en segundo plano (recomendado)
docker-compose up -d

# Iniciar y ver logs en tiempo real
docker-compose up

# Reconstruir imágenes y iniciar
docker-compose up -d --build
```

### Detener servicios
```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (CUIDADO: elimina datos de BD)
docker-compose down -v
```

### Reiniciar servicios
```bash
# Reiniciar todos los servicios
docker-compose restart

# Reiniciar un servicio específico
docker-compose restart backend
docker-compose restart frontend
docker-compose restart mysql
```

### Ver estado de servicios
```bash
# Ver estado de todos los contenedores
docker-compose ps

# Ver logs de un servicio específico
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql

# Ver logs en tiempo real
docker-compose logs -f backend
```

---

## 🗄️ Comandos de Base de Datos

### Acceder a MySQL desde el contenedor
```bash
# Conectar directamente al contenedor MySQL
docker-compose exec mysql mysql -u root -p

# Conectar con usuario específico
docker-compose exec mysql mysql -u sneakershop_user -p sneakershop

# Ver bases de datos
docker-compose exec mysql mysql -u root -prootpassword -e "SHOW DATABASES;"
```

### Verificar conectividad de MySQL
```bash
# Verificar que MySQL esté funcionando
docker-compose exec mysql mysqladmin ping -h localhost -u root -prootpassword

# Ver estado de salud del contenedor
docker-compose exec mysql mysqladmin status -u root -prootpassword
```

### Backup y Restore de Base de Datos
```bash
# Crear backup de la base de datos
docker-compose exec mysql mysqldump -u root -prootpassword sneakershop > backup_sneakershop.sql

# Restaurar backup
docker-compose exec -T mysql mysql -u root -prootpassword sneakershop < backup_sneakershop.sql
```

---

## 🧹 Comandos de Limpieza

### Limpiar contenedores
```bash
# Detener y eliminar contenedores
docker-compose down

# Eliminar contenedores y volúmenes
docker-compose down -v

# Eliminar contenedores, volúmenes y redes
docker-compose down -v --remove-orphans
```

### Limpiar imágenes Docker
```bash
# Ver imágenes del proyecto
docker images | grep proyectoavanzada

# Eliminar imágenes del proyecto
docker rmi proyectoavanzada-backend
docker rmi proyectoavanzada-frontend

# Eliminar todas las imágenes no utilizadas
docker image prune -a
```

### Limpiar volúmenes
```bash
# Ver volúmenes
docker volume ls

# Eliminar volúmenes específicos
docker volume rm proyectoavanzada_mysql_data
docker volume rm proyectoavanzada_backend_logs

# Eliminar todos los volúmenes no utilizados
docker volume prune
```

### Limpieza completa del sistema
```bash
# Eliminar todo lo no utilizado (CUIDADO)
docker system prune -a --volumes
```

---

## 🔍 Comandos de Diagnóstico

### Ver recursos utilizados
```bash
# Ver uso de recursos de contenedores
docker stats

# Ver uso de recursos de un contenedor específico
docker stats sneakershop_backend
```

### Ver información de contenedores
```bash
# Ver información detallada de un contenedor
docker inspect sneakershop_mysql

# Ver IP de un contenedor
docker inspect sneakershop_mysql | findstr IPAddress
```

### Ver logs detallados
```bash
# Ver logs de los últimos 50 registros
docker-compose logs --tail=50 backend

# Ver logs desde una fecha específica
docker-compose logs --since="2025-10-30T00:00:00" backend
```

### Verificar conectividad de red
```bash
# Probar conectividad desde el host
Test-NetConnection -ComputerName localhost -Port 8081
Test-NetConnection -ComputerName localhost -Port 4200
Test-NetConnection -ComputerName localhost -Port 3308
```

---

## 🌐 Acceso al Proyecto

### URLs de Acceso (Con Docker Ejecutándose)

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend** | http://localhost:4200 | Aplicación Angular |
| **Backend API** | http://localhost:8081/api | API REST de Spring Boot |
| **Swagger UI** | http://localhost:8081/swagger-ui.html | Documentación de la API |
| **phpMyAdmin** | http://localhost:8083 | Administrador de Base de Datos |

### Credenciales de Base de Datos

**Para phpMyAdmin (http://localhost:8083):**
- **Usuario:** sneakershop_user
- **Contraseña:** sneakershop_password
- **Base de datos:** sneakershop

**Para acceso como root:**
- **Usuario:** root
- **Contraseña:** rootpassword

**Para conexión directa:**
- **Usuario root:** root
- **Contraseña root:** rootpassword

### Verificar que Todo Funciona

1. **Verificar servicios:**
   ```bash
   docker-compose ps
   ```
   Todos deben estar en estado "Up" y MySQL debe mostrar "(healthy)"

2. **Probar Frontend:**
   - Abrir http://localhost:4200
   - Debe cargar la aplicación Angular

3. **Probar Backend:**
   - Abrir http://localhost:8081/swagger-ui.html
   - Debe mostrar la documentación de la API

4. **Probar Base de Datos:**
   - Abrir http://localhost:8083
   - Iniciar sesión con usuario: `sneakershop_user` y contraseña: `sneakershop_password`
   - Debe mostrar la interfaz de phpMyAdmin con las tablas de la base de datos

---

## 🚨 Solución de Problemas Comunes

### Si el backend se reinicia constantemente
```bash
# Ver logs del backend
docker-compose logs backend

# Verificar que MySQL esté saludable
docker-compose ps

# Reiniciar solo el backend
docker-compose restart backend
```

### Si hay conflictos de puertos
```bash
# Ver qué está usando los puertos
netstat -ano | findstr :8081
netstat -ano | findstr :4200
netstat -ano | findstr :3308

# Detener servicios y reiniciar
docker-compose down
docker-compose up -d
```

### Si hay problemas de conectividad
```bash
# Verificar red de Docker
docker network ls
docker network inspect proyectoavanzada_sneakershop_network

# Probar conectividad entre contenedores
docker-compose exec backend ping mysql
```

---

## 📝 Comandos Útiles Adicionales

### Ejecutar comandos dentro de contenedores
```bash
# Acceder al shell del backend
docker-compose exec backend sh

# Acceder al shell de MySQL
docker-compose exec mysql bash

# Ejecutar comando específico en el backend
docker-compose exec backend java -version
```

### Ver información del proyecto
```bash
# Ver configuración de Docker Compose
docker-compose config

# Ver variables de entorno de un contenedor
docker-compose exec backend env
```

### Monitoreo en tiempo real
```bash
# Ver logs de todos los servicios en tiempo real
docker-compose logs -f

# Ver estadísticas de recursos
docker stats --no-stream
```

---

## ✅ Checklist de Verificación

Antes de usar el proyecto, verifica que:

- [ ] Docker Desktop esté ejecutándose
- [ ] Todos los servicios estén "Up" con `docker-compose ps`
- [ ] MySQL esté "healthy"
- [ ] El frontend cargue en http://localhost:4200
- [ ] El backend responda en http://localhost:8081/swagger-ui.html
- [ ] phpMyAdmin funcione en http://localhost:8083
- [ ] El frontend se conecte correctamente al backend (sin errores de conexión)

---

**¡Listo! Con estos comandos tienes control total sobre tu proyecto Dockerizado.** 🎉
