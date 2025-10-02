# Instrucciones para Ubuntu/Linux

## Scripts creados para Linux

He creado los siguientes scripts `.sh` para tu VPS Ubuntu:

### Desarrollo:
- `start_backend_dev.sh` - Backend en modo desarrollo (mvn spring-boot:run)
- `start_frontend_dev.sh` - Frontend en modo desarrollo (ng serve)

### Producción:
- `build_backend_prod.sh` - Compilar backend a JAR para producción
- `build_frontend_prod.sh` - Compilar frontend optimizado para Apache
- `make_executable.sh` - Script para hacer ejecutables los otros scripts

## Pasos para ejecutar en tu VPS Ubuntu:

### 1. Conectarte por SSH
```bash
ssh tu_usuario@13.61.162.154
```

### 2. Navegar al directorio del proyecto
```bash
cd /ruta/a/tu/proyecto/ProyectoAvanzada
```

### 3. Hacer ejecutables los scripts
```bash
chmod +x *.sh
```

O ejecutar el script específico:
```bash
chmod +x make_executable.sh
./make_executable.sh
```

### 4. Ejecutar los servicios

#### Para desarrollo:
```bash
# Terminal 1 - Backend
./start_backend_dev.sh

# Terminal 2 - Frontend  
./start_frontend_dev.sh
```

#### Para producción (CORRECTO):
```bash
# 1. Compilar backend a JAR
./build_backend_prod.sh

# 2. Compilar frontend optimizado
./build_frontend_prod.sh

# 3. Ejecutar backend como servicio (recomendado)
sudo systemctl start backend-app
sudo systemctl enable backend-app

# 4. El frontend se sirve automáticamente por Apache
```

## Comandos individuales (alternativa a los scripts):

### Backend en desarrollo:
```bash
cd backend
export SPRING_PROFILES_ACTIVE=dev
export CORS_ORIGINS=http://localhost:4200
export LOG_LEVEL=DEBUG
export SECURITY_LOG_LEVEL=DEBUG
export SHOW_SQL=true
export FORMAT_SQL=true
mvn spring-boot:run
```

### Backend en producción (CORRECTO):
```bash
# Compilar a JAR
cd backend
export SPRING_PROFILES_ACTIVE=prod
export CORS_ORIGINS=http://13.61.162.154:4200,http://localhost:4200
export LOG_LEVEL=INFO
export SECURITY_LOG_LEVEL=WARN
export SHOW_SQL=false
export FORMAT_SQL=false
mvn clean package -DskipTests

# Ejecutar JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend en desarrollo:
```bash
cd frontend/frontend-app
ng serve --host 0.0.0.0 --port 4200
```

### Frontend en producción (CORRECTO):
```bash
# Compilar optimizado
cd frontend/frontend-app
npm install
npm run build --prod

# Los archivos se generan en dist/ y se sirven por Apache
```

## Ejecutar en segundo plano (recomendado para VPS):

### Backend como servicio systemd (RECOMENDADO):
```bash
# El backend ya está configurado como servicio
sudo systemctl start backend-app
sudo systemctl enable backend-app

# Ver estado
sudo systemctl status backend-app

# Ver logs
sudo journalctl -u backend-app -f
```

### Backend manual en segundo plano:
```bash
# Compilar primero
./build_backend_prod.sh

# Ejecutar en segundo plano
nohup java -jar backend/target/backend-0.0.1-SNAPSHOT.jar > backend.log 2>&1 &
```

### Frontend (se sirve por Apache):
```bash
# Compilar y copiar a Apache
./build_frontend_prod.sh
sudo cp -r frontend/frontend-app/dist/* /var/www/html/
```

### Detener procesos:
```bash
# Si usas systemd
sudo systemctl stop backend-app

# Si usas manual
ps aux | grep java
kill PID
```

## URLs de acceso:

- **Frontend**: http://13.61.162.154 (servido por Apache)
- **Backend API**: http://13.61.162.154:8080/api

## Notas importantes:

1. Asegúrate de que Java, Maven y Node.js estén instalados en tu VPS
2. Asegúrate de que MySQL esté corriendo
3. El puerto 8080 debe estar abierto en el firewall (para el backend)
4. El puerto 80/443 debe estar abierto en el firewall (para Apache)
5. El backend está configurado para escuchar en `0.0.0.0:8080` para permitir acceso externo
6. El frontend se sirve por Apache en el puerto 80 (HTTP) o 443 (HTTPS)
7. **NO uses `ng serve` en producción** - solo para desarrollo
8. **NO uses `mvn spring-boot:run` en producción** - compila a JAR y usa systemd
