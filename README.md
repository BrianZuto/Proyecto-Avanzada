# 🏪 SneakerZone - Sistema de Venta de Sneakers

## 📚 Información del Proyecto

**Asignatura:** Programación Avanzada  
**Estudiantes:** 
- Brian Zuleta Tobón
- Luis Torres

**Versión:** 2.0.0  
**Estado:** ✅ **Proyecto Completo - Backend y Frontend 100% Funcionales**

Sistema completo de gestión de ventas de sneakers desarrollado con **Angular 20+** (Frontend) y **Spring Boot 3.x** (Backend), completamente containerizado con **Docker** y desplegado en producción.

## 🚀 Tecnologías Utilizadas

### Backend
- **Java 17+** - Lenguaje de programación
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticación y autorización
- **JWT (JSON Web Tokens)** - Autenticación stateless
- **Spring Data JPA** - Persistencia de datos
- **MySQL 8.0** - Base de datos relacional
- **Maven** - Gestión de dependencias
- **JUnit 5** - Testing (107 tests implementados)
- **Swagger/OpenAPI** - Documentación de API

### Frontend
- **Angular 20+** - Framework frontend
- **TypeScript** - Lenguaje de programación
- **RxJS** - Programación reactiva
- **Bootstrap Icons** - Iconografía
- **SweetAlert2** - Alertas y notificaciones
- **jsPDF** - Generación de PDFs
- **Angular Material** - Componentes UI

### DevOps & Infraestructura
- **Docker** - Containerización
- **Docker Compose** - Orquestación de servicios
- **Nginx** - Servidor web para frontend
- **MySQL** - Base de datos
- **phpMyAdmin** - Administración de BD

## 📋 Características Principales

### 🔐 Sistema de Autenticación
- Registro de usuarios con validación
- Login con JWT tokens
- Roles de usuario (Admin, Empleado, Usuario)
- Protección de rutas basada en roles
- Gestión de sesiones
- Recuperación de contraseña (estructura preparada)

### 🛍️ Gestión de Productos
- Catálogo completo de productos
- Visualización pública (sin necesidad de login)
- Filtros por marca y categoría
- Búsqueda de productos
- Gestión de stock en tiempo real
- Imágenes de productos
- Descuentos y ofertas
- Productos destacados y nuevos

### 🛒 Carrito de Compras
- Agregar productos al carrito (requiere login)
- Modificar cantidades
- Eliminar productos
- Cálculo automático de totales
- Persistencia en sesión

### 💳 Proceso de Compra
- Selección de dirección de envío
- Selección de método de pago
- Gestión de múltiples direcciones
- Gestión de métodos de pago
- Cálculo de totales
- Generación de comprobantes PDF
- Historial de compras

### 👤 Perfil de Usuario
- Edición de información personal
- Gestión de direcciones
- Gestión de métodos de pago
- Historial de compras
- Visualización de pedidos

### 👨‍💼 Panel de Administración
- **Dashboard** con estadísticas en tiempo real
- **Gestión de Productos**: CRUD completo
- **Gestión de Ventas**: Visualización y detalles
- **Gestión de Compras**: Control de inventario
- **Gestión de Usuarios**: Administración de cuentas
- **Reportes**: Generación de reportes de ventas
- **Top Productos**: Productos más vendidos
- **Exportación PDF**: Comprobantes de venta

### 📊 Reportes y Estadísticas
- Reportes de ventas
- Estadísticas de productos
- Análisis de compras
- Top productos más vendidos
- Métricas del dashboard

## 🗄️ Base de Datos

### Estructura Principal
- **11 tablas principales** con relaciones bien definidas
- **Scripts SQL organizados** en el directorio `database/`
- **Documentación completa** en `database/README_SQL.txt`
- **Datos de ejemplo** para desarrollo y testing

### Tablas Principales
- `usuarios` - Gestión de usuarios del sistema
- `clientes` - Información de clientes
- `productos` - Catálogo de productos
- `categorias` - Categorías de productos
- `marcas` - Marcas de productos
- `inventario` - Control de stock
- `compras` - Registro de compras a proveedores
- `ventas` - Registro de ventas
- `detalles_venta` - Detalles de productos en ventas
- `direcciones` - Direcciones de envío
- `metodos_pago` - Métodos de pago de usuarios

> 📖 **Nota:** Para más información sobre la base de datos, consulta `database/README_SQL.txt`

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

## 🎯 Funcionalidades Detalladas

### ✅ Backend (100% Completo)
- ✅ **107 tests JUnit** ejecutándose exitosamente
- ✅ **API REST** con 100+ endpoints documentados
- ✅ **CRUD completo** de todas las entidades:
  - Usuarios y autenticación
  - Clientes
  - Productos con inventario
  - Compras y proveedores
  - Ventas y detalles
  - Direcciones
  - Métodos de pago
- ✅ **Sistema de autenticación JWT** completo
- ✅ **Autorización basada en roles** (Admin, Empleado, Usuario)
- ✅ **Validación de datos** con Bean Validation
- ✅ **Manejo de errores** centralizado
- ✅ **CORS configurado** para desarrollo y producción
- ✅ **Swagger/OpenAPI** para documentación interactiva
- ✅ **Endpoints públicos** para visualización de productos
- ✅ **Endpoints protegidos** para operaciones sensibles

### ✅ Frontend (100% Completo)
- ✅ **Angular 20+** con arquitectura moderna
- ✅ **Diseño responsivo** y adaptativo
- ✅ **Sistema de autenticación completo**:
  - Login y registro
  - Gestión de sesión
  - Protección de rutas
  - Guards de autenticación
- ✅ **Catálogo de productos**:
  - Visualización pública (sin login requerido)
  - Filtros por marca
  - Búsqueda de productos
  - Detalles de productos
- ✅ **Carrito de compras**:
  - Agregar productos (requiere login)
  - Modificar cantidades
  - Eliminar productos
  - Cálculo de totales
- ✅ **Proceso de compra completo**:
  - Selección de dirección
  - Selección de método de pago
  - Confirmación de compra
  - Generación de comprobante PDF
- ✅ **Perfil de usuario**:
  - Edición de datos personales
  - Gestión de direcciones
  - Gestión de métodos de pago
  - Historial de compras
- ✅ **Panel de administración**:
  - Dashboard con estadísticas
  - Gestión de productos
  - Gestión de ventas
  - Gestión de compras
  - Gestión de usuarios
  - Exportación de PDFs
- ✅ **Integración completa** con backend API
- ✅ **Manejo de errores** y notificaciones
- ✅ **Loading states** y feedback visual

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

### ✅ Estado General: COMPLETO

| Componente | Estado | Detalles |
|------------|--------|----------|
| **Backend** | ✅ 100% | 107 tests pasando, API completa |
| **Frontend** | ✅ 100% | Todas las funcionalidades implementadas |
| **Base de Datos** | ✅ 100% | Esquema completo con datos de ejemplo |
| **Docker** | ✅ 100% | Containerización completa |
| **Integración** | ✅ 100% | Frontend y Backend completamente integrados |
| **Documentación** | ✅ 100% | README completo y documentación SQL |
| **Despliegue** | ✅ 100% | Configurado para producción en VPS |

### 📈 Métricas del Proyecto

- **Líneas de código Backend:** ~15,000+
- **Líneas de código Frontend:** ~10,000+
- **Endpoints API:** 100+
- **Tests Backend:** 107
- **Componentes Angular:** 20+
- **Tablas de Base de Datos:** 11
- **Scripts SQL:** 11

## 🚀 Despliegue en Producción

El proyecto está configurado para despliegue en VPS con Docker:

### Configuración de Producción
- **Backend:** Configuración para producción en `application-prod.properties`
- **Frontend:** Build optimizado para producción con Nginx
- **Base de Datos:** MySQL 8.0 con persistencia de datos
- **CORS:** Configurado para dominio de producción

### Actualización en VPS
```bash
# Conectarse a la VPS
ssh usuario@vps-ip

# Navegar al proyecto
cd /ruta/a/ProyectoAvanzada

# Actualizar código
git checkout main
git pull origin main

# Reconstruir contenedores
docker-compose down
docker-compose build --no-cache
docker-compose up -d

# Verificar estado
docker-compose ps
```

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

## 📖 Documentación Adicional

### Base de Datos
- **README_SQL.txt**: Documentación completa de scripts SQL
- **Orden de ejecución**: Ver `database/README_SQL.txt`
- **Estructura de tablas**: Documentada en scripts SQL

### API Documentation
- **Swagger UI**: Disponible en `http://localhost:8081/swagger-ui.html` (desarrollo)
- **Endpoints públicos**: Productos visibles sin autenticación
- **Endpoints protegidos**: Requieren JWT token

### Arquitectura
- **Backend**: Arquitectura en capas (Controller, Service, Repository)
- **Frontend**: Arquitectura modular con componentes standalone
- **Base de Datos**: Relaciones bien definidas con integridad referencial

## 🔒 Seguridad

- ✅ Autenticación JWT implementada
- ✅ Contraseñas hasheadas con BCrypt
- ✅ Protección de rutas en frontend y backend
- ✅ Validación de datos en ambos extremos
- ✅ CORS configurado correctamente
- ✅ Endpoints sensibles protegidos
- ✅ Endpoints públicos para productos (sin autenticación)

## 📝 Notas de Desarrollo

### Características Implementadas Recientemente
- ✅ Visualización de productos para usuarios no logueados
- ✅ Botón "Agregar al carrito" solo visible para usuarios logueados
- ✅ Corrección de visualización de productos en ventas
- ✅ Generación de PDFs de comprobantes
- ✅ Panel de administración completo

### Mejores Prácticas Aplicadas
- ✅ Código limpio y bien estructurado
- ✅ Separación de responsabilidades
- ✅ Manejo de errores centralizado
- ✅ Validación de datos
- ✅ Documentación de código
- ✅ Testing del backend
- ✅ Containerización con Docker

## 📞 Soporte y Contacto

Para reportar problemas, solicitar nuevas funcionalidades o consultas:
- Crear un issue en el repositorio del proyecto
- Revisar la documentación en `database/README_SQL.txt`
- Consultar la documentación de API en Swagger UI

## 📄 Licencia

Este proyecto fue desarrollado como parte del curso de Programación Avanzada.

---

**Desarrollado con ❤️ por Brian Zuleta Tobón y Luis Torres**  
**Tecnologías:** Angular 20+ | Spring Boot 3.x | MySQL 8.0 | Docker  
**Versión:** 2.0.0 | **Estado:** ✅ Proyecto Completo y Funcional  
**Fecha:** 2025