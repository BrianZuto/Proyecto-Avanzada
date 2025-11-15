# 🔄 Comandos para Actualizar el Proyecto en VPS

## 📥 Paso 1: Hacer Pull del Repositorio

```bash
# Navegar al directorio del proyecto (ajusta la ruta según tu configuración)
cd /ruta/a/ProyectoAvanzada

# Verificar que estás en la rama correcta
git branch

# Cambiar a tu rama (si no estás en ella)
git checkout rama_brian

# Hacer pull de los últimos cambios
git pull origin rama_brian
```

## 🛑 Paso 2: Detener los Contenedores

```bash
# Detener todos los servicios (sin eliminar volúmenes)
docker-compose down
```

## 🔨 Paso 3: Reconstruir las Imágenes

```bash
# Opción 1: Reconstruir solo el frontend (más rápido)
docker-compose build --no-cache frontend

# Opción 2: Reconstruir todo desde cero (si hay cambios en backend también)
docker-compose build --no-cache

# Verificar que las imágenes se construyeron correctamente
docker images | grep sneakershop
```

## 🚀 Paso 4: Iniciar los Servicios

```bash
# Iniciar todos los servicios en segundo plano
docker-compose up -d

# Verificar que todos los servicios estén corriendo
docker-compose ps
```

## 📊 Paso 5: Verificar los Logs

```bash
# Ver logs del frontend para verificar que se construyó correctamente
docker-compose logs -f frontend

# Ver logs del backend
docker-compose logs -f backend

# Presiona Ctrl+C para salir de los logs
```

## 🔍 Verificación Final

```bash
# Ver estado de todos los contenedores
docker-compose ps

# Todos deben estar en estado "Up" y saludables
# - sneakershop_mysql: debe mostrar "(healthy)"
# - sneakershop_backend: debe estar "Up"
# - sneakershop_frontend: debe estar "Up"
# - sneakershop_phpmyadmin: debe estar "Up"
```

## 🧹 Comandos de Limpieza (Opcional)

Si necesitas limpiar completamente y reconstruir desde cero:

```bash
# Detener y eliminar contenedores, redes y volúmenes
docker-compose down -v

# Eliminar imágenes antiguas (opcional)
docker image prune -a

# Reconstruir todo desde cero
docker-compose build --no-cache

# Iniciar servicios
docker-compose up -d
```

## ⚠️ Nota Importante

- **NO ejecutes `docker-compose down -v`** a menos que quieras eliminar todos los datos de la base de datos
- Si solo modificaste el frontend, reconstruir solo el frontend es suficiente
- Los cambios en el frontend se reflejarán inmediatamente después de reconstruir

## 🐛 Solución de Problemas

### Si el build falla:

```bash
# Ver logs detallados del build
docker-compose build --no-cache frontend 2>&1 | tee build.log

# Verificar espacio en disco
df -h

# Limpiar caché de Docker
docker system prune -a
```

### Si los contenedores no inician:

```bash
# Ver logs de errores
docker-compose logs

# Verificar que los puertos no estén en uso
netstat -tulpn | grep -E '4200|8081|3308|8083'

# Reiniciar Docker (si es necesario)
sudo systemctl restart docker
```

