# 🏗️ Arquitectura del Proyecto Avanzada

## Visión General

Este proyecto implementa una arquitectura de **3 capas** (3-tier architecture) con separación clara entre:

- **Frontend (Presentación)**: Angular
- **Backend (Lógica de Negocio)**: Spring Boot
- **Base de Datos (Persistencia)**: MySQL

## Diagrama de Arquitectura

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐    JDBC    ┌─────────────────┐
│                 │ ──────────────► │                 │ ─────────► │                 │
│   Frontend      │                 │   Backend       │            │   Base de       │
│   (Angular)     │ ◄────────────── │   (Spring Boot) │ ◄───────── │   Datos         │
│   Port: 4200    │                 │   Port: 8080    │            │   (MySQL)       │
│                 │                 │                 │            │   Port: 3306    │
└─────────────────┘                 └─────────────────┘            └─────────────────┘
```

## Capas del Sistema

### 1. Capa de Presentación (Frontend)

**Tecnología:** Angular 17+

**Estructura:**
```
frontend/frontend-app/src/app/
├── components/           # Componentes reutilizables
│   ├── usuario-list/    # Lista de usuarios
│   └── usuario-form/    # Formulario de usuarios
├── models/              # Interfaces TypeScript
│   └── usuario.ts       # Modelo de Usuario
├── services/            # Servicios para comunicación con API
│   └── usuario.ts       # Servicio de Usuario
└── app.ts              # Componente principal
```

**Responsabilidades:**
- ✅ Interfaz de usuario
- ✅ Validaciones del lado cliente
- ✅ Comunicación con API REST
- ✅ Manejo de estados de la aplicación
- ✅ Enrutamiento

### 2. Capa de Lógica de Negocio (Backend)

**Tecnología:** Spring Boot 3.2.0

**Estructura:**
```
backend/src/main/java/com/proyectoavanzada/backend/
├── config/              # Configuraciones
│   └── CorsConfig.java  # Configuración CORS
├── controller/          # Controladores REST
│   └── UsuarioController.java
├── model/               # Entidades JPA
│   └── Usuario.java
├── repository/          # Repositorios de datos
│   └── UsuarioRepository.java
├── service/             # Lógica de negocio
│   └── UsuarioService.java
└── BackendApplication.java
```

**Responsabilidades:**
- ✅ API REST endpoints
- ✅ Lógica de negocio
- ✅ Validaciones del servidor
- ✅ Comunicación con base de datos
- ✅ Manejo de transacciones
- ✅ Configuración de seguridad

### 3. Capa de Persistencia (Base de Datos)

**Tecnología:** MySQL 8.0

**Estructura:**
```sql
proyecto_avanzada/
└── usuarios/
    ├── id (BIGINT, PK, AUTO_INCREMENT)
    ├── nombre (VARCHAR(50), NOT NULL)
    ├── email (VARCHAR(100), UNIQUE, NOT NULL)
    ├── password (VARCHAR(255), NOT NULL)
    ├── fecha_creacion (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
    └── activo (BOOLEAN, DEFAULT TRUE)
```

**Responsabilidades:**
- ✅ Almacenamiento persistente
- ✅ Integridad de datos
- ✅ Índices para optimización
- ✅ Transacciones ACID

## Flujo de Datos

### 1. Operación de Lectura (GET)
```
Usuario → Frontend → HTTP GET → Backend → JPA → MySQL → Datos
```

### 2. Operación de Escritura (POST/PUT)
```
Usuario → Frontend → HTTP POST/PUT → Backend → Validación → JPA → MySQL → Confirmación
```

### 3. Operación de Eliminación (DELETE)
```
Usuario → Frontend → HTTP DELETE → Backend → JPA → MySQL → Confirmación
```

## Patrones de Diseño Implementados

### 1. MVC (Model-View-Controller)
- **Model:** Entidades JPA y DTOs
- **View:** Componentes Angular
- **Controller:** Controladores REST de Spring Boot

### 2. Repository Pattern
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### 3. Service Layer Pattern
```java
@Service
public class UsuarioService {
    // Lógica de negocio encapsulada
}
```

### 4. Dependency Injection
- Spring Boot: `@Autowired`, `@Service`, `@Repository`
- Angular: Constructor injection

## Configuración de Comunicación

### CORS (Cross-Origin Resource Sharing)
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
```

### HTTP Client (Angular)
```typescript
@Injectable()
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';
  
  constructor(private http: HttpClient) { }
}
```

## Seguridad

### Configuración Actual
- ✅ CORS configurado para desarrollo
- ✅ Validaciones en frontend y backend
- ✅ Sanitización de datos

### Preparado para Futuras Implementaciones
- 🔄 JWT Authentication
- 🔄 Spring Security
- 🔄 HTTPS
- 🔄 Rate Limiting

## Escalabilidad

### Horizontal Scaling
- **Frontend:** CDN, múltiples instancias
- **Backend:** Load balancer, múltiples instancias
- **Base de Datos:** Read replicas, sharding

### Vertical Scaling
- **Backend:** Más CPU/RAM
- **Base de Datos:** Más recursos

## Monitoreo y Logging

### Backend
```properties
# application.properties
logging.level.com.proyectoavanzada.backend=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Frontend
```typescript
// Manejo de errores en servicios
error: (error) => {
  console.error('Error:', error);
  this.error = 'Error al cargar los usuarios';
}
```

## Testing Strategy

### Backend Testing
- **Unit Tests:** JUnit 5
- **Integration Tests:** @SpringBootTest
- **Repository Tests:** @DataJpaTest

### Frontend Testing
- **Unit Tests:** Jasmine + Karma
- **E2E Tests:** Cypress (futuro)

## Deployment

### Desarrollo
- Frontend: `ng serve` (puerto 4200)
- Backend: `mvn spring-boot:run` (puerto 8080)
- Base de Datos: XAMPP MySQL (puerto 3306)

### Producción (Futuro)
- Frontend: Build estático en servidor web
- Backend: JAR ejecutable o contenedor Docker
- Base de Datos: MySQL en servidor dedicado

## Consideraciones de Rendimiento

### Frontend
- ✅ Lazy loading de componentes
- ✅ OnPush change detection strategy
- ✅ Optimización de bundles

### Backend
- ✅ Connection pooling
- ✅ Índices en base de datos
- ✅ Caching (futuro)

### Base de Datos
- ✅ Índices en campos de búsqueda
- ✅ Optimización de consultas
- ✅ Normalización de datos

---

**Esta arquitectura proporciona una base sólida y escalable para el desarrollo de aplicaciones web modernas.** 🚀
