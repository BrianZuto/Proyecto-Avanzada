# Proyecto Avanzada - Full Stack

Este proyecto consiste en una aplicación web full stack desarrollada con **Angular** (Frontend) y **Spring Boot** (Backend), utilizando **XAMPP** con **MySQL** como base de datos.

## 📋 Estructura del Proyecto

```
ProyectoAvanzada/
├── backend/                 # Aplicación Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/proyectoavanzada/backend/
│   │   │   │       ├── config/          # Configuraciones
│   │   │   │       ├── controller/      # Controladores REST
│   │   │   │       ├── model/           # Entidades JPA
│   │   │   │       ├── repository/      # Repositorios JPA
│   │   │   │       └── service/         # Servicios de negocio
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/               # Aplicación Angular
│   └── frontend-app/
│       ├── src/
│       │   ├── app/
│       │   │   ├── components/          # Componentes Angular
│       │   │   ├── models/              # Interfaces TypeScript
│       │   │   ├── services/            # Servicios Angular
│       │   │   └── ...
│       │   └── ...
│       └── package.json
├── database/              # Scripts SQL
│   ├── 01_create_database.sql
│   └── 02_database_config.sql
└── docs/                  # Documentación adicional
```

## 🚀 Tecnologías Utilizadas

### Backend (Spring Boot)
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Connector**
- **JWT (JSON Web Tokens)**
- **Maven**

### Frontend (Angular)
- **Angular 17+**
- **TypeScript**
- **Angular Material**
- **RxJS**
- **HTTP Client**

### Base de Datos
- **MySQL 8.0**
- **XAMPP**

## 📦 Prerrequisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

1. **Java 17** o superior
2. **Node.js** (versión 18 o superior)
3. **XAMPP** con MySQL
4. **Maven** (para el backend)
5. **Angular CLI** (se instala automáticamente)

## ⚙️ Configuración e Instalación

### 1. Configurar la Base de Datos

1. Inicia **XAMPP** y asegúrate de que **MySQL** esté ejecutándose
2. Abre **phpMyAdmin** (http://localhost/phpmyadmin)
3. Ejecuta los scripts SQL en el directorio `database/`:
   - `01_create_database.sql` - Crea la base de datos y tabla
   - `02_database_config.sql` - Configuraciones adicionales

### 2. Configurar el Backend (Spring Boot)

1. Navega al directorio del backend:
   ```bash
   cd backend
   ```

2. Verifica que la configuración de la base de datos en `src/main/resources/application.properties` sea correcta:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_avanzada?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=
   ```

3. Compila y ejecuta el proyecto:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   El backend estará disponible en: **http://localhost:8080/api**

### 3. Configurar el Frontend (Angular)

1. Navega al directorio del frontend:
   ```bash
   cd frontend/frontend-app
   ```

2. Instala las dependencias:
   ```bash
   npm install
   ```

3. Ejecuta el servidor de desarrollo:
   ```bash
   ng serve
   ```

   El frontend estará disponible en: **http://localhost:4200**

## 🔧 Funcionalidades Implementadas

### Gestión de Usuarios
- ✅ **CRUD completo** de usuarios
- ✅ **Validaciones** en frontend y backend
- ✅ **Lista de usuarios** con paginación
- ✅ **Formulario** para crear/editar usuarios
- ✅ **Eliminación** de usuarios
- ✅ **Estados** activo/inactivo

### API REST Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/usuarios` | Obtener todos los usuarios |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID |
| GET | `/api/usuarios/activos` | Obtener usuarios activos |
| POST | `/api/usuarios` | Crear nuevo usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |

## 🎨 Características del Frontend

- **Diseño responsivo** con CSS moderno
- **Componentes reutilizables** (lista, formulario)
- **Validaciones en tiempo real**
- **Manejo de errores** y estados de carga
- **Interfaz intuitiva** y fácil de usar

## 🔒 Seguridad

- **CORS configurado** para desarrollo
- **Validaciones** en frontend y backend
- **Preparado para JWT** (implementación futura)
- **Sanitización** de datos de entrada

## 🧪 Pruebas

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend/frontend-app
ng test
```

## 📝 Desarrollo

### Agregar nuevas funcionalidades

1. **Backend**: Crear entidad → Repository → Service → Controller
2. **Frontend**: Crear modelo → Service → Componente → Vista

### Estructura de commits
- `feat:` Nueva funcionalidad
- `fix:` Corrección de bugs
- `docs:` Documentación
- `style:` Formato de código
- `refactor:` Refactorización

## 🚨 Solución de Problemas

### Error de conexión a la base de datos
- Verifica que XAMPP esté ejecutándose
- Confirma que MySQL esté activo
- Revisa las credenciales en `application.properties`

### Error de CORS
- Verifica que el frontend esté en `http://localhost:4200`
- Revisa la configuración de CORS en `CorsConfig.java`

### Error de compilación Angular
- Ejecuta `npm install` para instalar dependencias
- Verifica la versión de Node.js

## 📞 Soporte

Para reportar problemas o solicitar nuevas funcionalidades, crea un issue en el repositorio del proyecto.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

**Desarrollado con ❤️ usando Angular + Spring Boot**
