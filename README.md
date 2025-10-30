# 🏪 Proyecto Avanzada - Sistema de Venta de Sneakers

## 📚 Información Académica

**Asignatura:** Programación Avanzada  
**Estudiantes:** 
- Brian Zuleta Tobón
- Luis Torres

Sistema completo de gestión de ventas de sneakers con **Angular** (Frontend) y **Spring Boot** (Backend), containerizado con **Docker**.

## 🚀 Formas de Ejecutar el Proyecto

### Opción 1: Con Docker (Recomendado)

#### Prerrequisitos
- Docker Desktop instalado y ejecutándose
- Git

#### Ejecutar con Docker Compose

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd ProyectoAvanzada

# Ejecutar con Docker Compose
docker-compose up -d

# Verificar que todos los servicios estén funcionando
docker-compose ps
```

#### Acceder a la Aplicación

- **Frontend (Angular):** http://localhost:4200
- **Backend API:** http://localhost:8081/api
- **Base de Datos (phpMyAdmin):** http://localhost:8083
- **Swagger API Docs:** http://localhost:8081/swagger-ui.html

### Opción 2: Ejecución Individual (Desarrollo)

#### Prerrequisitos
- Java 17 o superior
- Node.js 18+ y npm
- MySQL 8.0 (XAMPP recomendado)
- Maven
- Angular CLI

#### 1. Configurar la Base de Datos

```bash
# Iniciar XAMPP y MySQL
# Crear la base de datos
mysql -u root -p
CREATE DATABASE sneakershop;

# Ejecutar scripts SQL
mysql -u root -p sneakershop < database/02_create_tables.sql
mysql -u root -p sneakershop < database/03_insert_sample_data.sql
```

#### 2. Ejecutar el Backend (Spring Boot)

```bash
# Navegar al directorio del backend
cd backend

# Instalar dependencias y compilar
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

# El backend estará disponible en: http://localhost:8080/api
```

#### 3. Ejecutar el Frontend (Angular)

```bash
# Navegar al directorio del frontend
cd frontend/frontend-app

# Instalar dependencias
npm install

# Ejecutar el servidor de desarrollo
ng serve

# El frontend estará disponible en: http://localhost:4200
```

#### 4. Verificar la Aplicación

- **Frontend:** http://localhost:4200
- **Backend API:** http://localhost:8080/api
- **Swagger API Docs:** http://localhost:8080/swagger-ui.html

### ¿Cuándo usar cada opción?

#### 🐳 Usar Docker cuando:
- Quieres una configuración rápida y sin complicaciones
- No tienes las dependencias instaladas localmente
- Quieres un entorno de producción idéntico
- Trabajas en equipo y necesitas consistencia
- Quieres probar la aplicación completa rápidamente

#### 💻 Usar ejecución individual cuando:
- Estás desarrollando activamente el código
- Necesitas debugging avanzado
- Quieres modificar el código en tiempo real
- Tienes las dependencias ya instaladas
- Quieres ejecutar tests individuales

### 🔧 Configuración de Desarrollo

#### Para desarrollo con Docker:
```bash
# Usar configuración de desarrollo
docker-compose -f docker-compose.dev.yml up -d

# Ver logs en tiempo real
docker-compose logs -f backend
docker-compose logs -f frontend
```

#### Para desarrollo individual:
```bash
# Backend con hot reload (si está configurado)
cd backend
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"

# Frontend con hot reload
cd frontend/frontend-app
ng serve --host 0.0.0.0 --port 4200
```

## 📋 Servicios Incluidos

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| Frontend | 4200 | Aplicación Angular |
| Backend | 8080 | API Spring Boot |
| MySQL | 3307 | Base de datos |
| Adminer | 8081 | Gestión de BD |

## 🔧 Comandos Útiles

### Comandos Docker

```bash
# Iniciar todos los servicios
docker-compose up -d

# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend

# Detener todos los servicios
docker-compose down

# Reconstruir y reiniciar
docker-compose up -d --build

# Limpiar volúmenes (eliminar datos de BD)
docker-compose down -v

# Ejecutar comandos dentro del contenedor
docker-compose exec backend bash
docker-compose exec frontend sh
```

### Comandos de Desarrollo Individual

```bash
# Backend - Ejecutar tests
cd backend
mvn test

# Backend - Compilar sin ejecutar
mvn clean compile

# Backend - Crear JAR ejecutable
mvn clean package

# Frontend - Instalar dependencias
cd frontend/frontend-app
npm install

# Frontend - Ejecutar tests
ng test

# Frontend - Compilar para producción
ng build --prod

# Frontend - Verificar configuración
ng version
```

### Comandos de Base de Datos

```bash
# Conectar a MySQL (ejecución individual)
mysql -u root -p sneakershop

# Conectar a MySQL (Docker)
docker-compose exec mysql mysql -u root -p sneakershop

# Restaurar base de datos
mysql -u root -p sneakershop < database/02_create_tables.sql
mysql -u root -p sneakershop < database/03_insert_sample_data.sql
```

## 🏗️ Arquitectura del Proyecto

```
ProyectoAvanzada/
├── backend/                 # Spring Boot API
│   ├── src/main/java/      # Código fuente Java
│   ├── Dockerfile          # Imagen Docker del backend
│   └── pom.xml            # Dependencias Maven
├── frontend/frontend-app/  # Angular Frontend
│   ├── src/               # Código fuente Angular
│   ├── Dockerfile         # Imagen Docker del frontend
│   └── nginx.conf         # Configuración Nginx
├── database/              # Scripts SQL
│   ├── 02_create_tables.sql
│   └── 03_insert_sample_data.sql
├── docker-compose.yml     # Orquestación de servicios
├── docker-compose.dev.yml # Configuración de desarrollo
└── docker-scripts/        # Scripts de utilidad
```

## 🎯 Funcionalidades

### ✅ Backend (Completo)
- **107 tests JUnit** ejecutándose exitosamente
- **CRUD completo** de clientes, productos, compras y ventas
- **Sistema de autenticación** con JWT
- **Reportes** en múltiples formatos
- **API REST** con 100+ endpoints

### 🚧 Frontend (En Desarrollo)
- **Angular 20+** con Material Design
- **Componentes base** implementados
- **Integración con backend** en progreso

## 🛠️ Desarrollo

### Modo Desarrollo
```bash
# Usar configuración de desarrollo
docker-compose -f docker-compose.dev.yml up -d
```

### Rebuild de Servicios
```bash
# Reconstruir solo el backend
docker-compose up -d --build backend

# Reconstruir solo el frontend
docker-compose up -d --build frontend
```

## 📊 Estado del Proyecto

- ✅ **Backend:** 100% funcional con 107 tests pasando
- ✅ **Base de Datos:** Configurada con datos de ejemplo
- ✅ **Docker:** Completamente containerizado
- 🚧 **Frontend:** En desarrollo activo

## 🐛 Solución de Problemas

### Problemas con Docker

#### Puerto 3306 en uso
```bash
# Cambiar puerto de MySQL en docker-compose.yml
ports:
  - "3307:3306"  # Usar puerto 3307 en lugar de 3306
```

#### Servicios no inician
```bash
# Verificar logs
docker-compose logs

# Reiniciar Docker Desktop
# Luego ejecutar:
docker-compose up -d
```

#### Frontend muestra página de Nginx
```bash
# Reconstruir frontend
docker-compose up -d --build frontend
```

#### Error de conexión a la base de datos
```bash
# Verificar que MySQL esté funcionando
docker-compose logs mysql

# Reiniciar solo MySQL
docker-compose restart mysql
```

### Problemas con Ejecución Individual

#### Error de conexión a MySQL
```bash
# Verificar que XAMPP esté ejecutándose
# Verificar credenciales en application.properties
# Verificar que la base de datos 'sneakershop' exista
```

#### Error de compilación del backend
```bash
# Limpiar y reinstalar dependencias
cd backend
mvn clean install

# Verificar versión de Java
java -version  # Debe ser Java 17+
```

#### Error de compilación del frontend
```bash
# Limpiar node_modules y reinstalar
cd frontend/frontend-app
rm -rf node_modules package-lock.json
npm install

# Verificar versión de Node.js
node -version  # Debe ser 18+
```

#### Error de CORS
```bash
# Verificar configuración CORS en backend
# Verificar que frontend esté en puerto 4200
# Verificar que backend esté en puerto 8080
```

### Problemas Generales

#### Puerto ya en uso
```bash
# Verificar qué proceso usa el puerto
netstat -ano | findstr :8080
netstat -ano | findstr :4200

# Terminar proceso si es necesario
taskkill /PID <PID> /F
```

#### Error de memoria
```bash
# Aumentar memoria para Docker Desktop
# O usar comandos con menos memoria:
docker-compose up -d --scale backend=1 --scale frontend=1
```

## 📞 Soporte

Para reportar problemas o solicitar nuevas funcionalidades, crea un issue en el repositorio del proyecto.

---

**Desarrollado con ❤️ usando Angular + Spring Boot + Docker**  
**Versión:** 2.0.0 | **Estado:** Backend Completo, Frontend en Desarrollo