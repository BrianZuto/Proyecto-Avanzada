# ⚙️ Configuración del Entorno de Desarrollo

## Requisitos del Sistema

### Software Necesario
- **Java 17+** - [Descargar](https://adoptium.net/)
- **Node.js 18+** - [Descargar](https://nodejs.org/)
- **XAMPP** - [Descargar](https://www.apachefriends.org/)
- **Maven 3.6+** (opcional, se incluye wrapper)
- **Git** - [Descargar](https://git-scm.com/)

### Herramientas de Desarrollo Recomendadas
- **IDE:** IntelliJ IDEA, Eclipse, o VS Code
- **Base de Datos:** MySQL Workbench (opcional)
- **API Testing:** Postman o Insomnia

## Configuración de XAMPP

### 1. Instalación
1. Descargar XAMPP desde el sitio oficial
2. Instalar en la ubicación por defecto
3. Iniciar XAMPP Control Panel

### 2. Configuración de MySQL
```bash
# Puerto por defecto: 3306
# Usuario: root
# Contraseña: (vacía por defecto)
```

### 3. Verificar instalación
- Abrir http://localhost/phpmyadmin
- Deberías ver la interfaz de phpMyAdmin

## Configuración de Java

### 1. Verificar instalación
```bash
java -version
javac -version
```

### 2. Configurar JAVA_HOME (si es necesario)
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

## Configuración de Node.js

### 1. Verificar instalación
```bash
node --version
npm --version
```

### 2. Instalar Angular CLI
```bash
npm install -g @angular/cli
```

### 3. Verificar Angular CLI
```bash
ng version
```

## Configuración del Proyecto

### 1. Clonar/Descargar el proyecto
```bash
# Si usas Git
git clone <repository-url>
cd ProyectoAvanzada

# O simplemente extraer el ZIP en la ubicación deseada
```

### 2. Configurar variables de entorno

#### Backend (application.properties)
```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_avanzada
spring.datasource.username=root
spring.datasource.password=

# Servidor
server.port=8080
server.servlet.context-path=/api

# CORS
spring.web.cors.allowed-origins=http://localhost:4200
```

#### Frontend (environment.ts)
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## Configuración de la Base de Datos

### 1. Crear la base de datos
```sql
-- Ejecutar en phpMyAdmin
CREATE DATABASE proyecto_avanzada 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### 2. Ejecutar scripts de inicialización
```bash
# Ejecutar en orden:
# 1. database/01_create_database.sql
# 2. database/02_database_config.sql
```

### 3. Verificar datos
```sql
USE proyecto_avanzada;
SELECT * FROM usuarios;
```

## Configuración del IDE

### VS Code (Recomendado)
```json
// .vscode/settings.json
{
  "java.home": "C:\\Program Files\\Java\\jdk-17",
  "java.configuration.updateBuildConfiguration": "automatic",
  "typescript.preferences.importModuleSpecifier": "relative"
}
```

### Extensiones recomendadas para VS Code
- **Java Extension Pack**
- **Angular Language Service**
- **MySQL**
- **REST Client**

### IntelliJ IDEA
1. Importar proyecto como Maven
2. Configurar SDK de Java 17
3. Instalar plugin de Angular

## Configuración de Git (Opcional)

### 1. Configurar usuario
```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu.email@ejemplo.com"
```

### 2. Crear .gitignore
```gitignore
# Backend
backend/target/
backend/.mvn/
backend/mvnw
backend/mvnw.cmd

# Frontend
frontend/frontend-app/node_modules/
frontend/frontend-app/dist/
frontend/frontend-app/.angular/

# IDE
.idea/
.vscode/
*.iml

# OS
.DS_Store
Thumbs.db
```

## Verificación de la Configuración

### 1. Script de verificación
```bash
# Verificar Java
java -version

# Verificar Node.js
node --version
npm --version

# Verificar Angular CLI
ng version

# Verificar Maven
mvn --version

# Verificar MySQL (desde XAMPP)
mysql --version
```

### 2. Prueba de conectividad
```bash
# Probar conexión a MySQL
mysql -u root -p -h localhost

# Probar puerto 8080 (después de iniciar backend)
curl http://localhost:8080/api/usuarios

# Probar puerto 4200 (después de iniciar frontend)
curl http://localhost:4200
```

## Solución de Problemas Comunes

### Error: "Port 3306 already in use"
```bash
# Cambiar puerto en XAMPP o detener servicio MySQL
net stop mysql
```

### Error: "Port 8080 already in use"
```bash
# Cambiar puerto en application.properties
server.port=8081
```

### Error: "Port 4200 already in use"
```bash
# Angular usará automáticamente el siguiente puerto disponible
# O especificar puerto manualmente:
ng serve --port 4201
```

### Error: "Java version mismatch"
```bash
# Verificar versión de Java
java -version

# Actualizar JAVA_HOME si es necesario
```

---

**¡Configuración completada!** 🎉

Ahora puedes proceder con la [Instalación Rápida](INSTALACION_RAPIDA.md).
