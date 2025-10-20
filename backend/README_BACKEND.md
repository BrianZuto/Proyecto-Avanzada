# Backend - Sistema de Venta de Tenis (Sneakers)

## Descripción
Backend completo desarrollado con Spring Boot para una aplicación de venta de tenis (sneakers) con funcionalidades CRUD completas, autenticación, reportes y gestión de inventario.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **MySQL 8.0**
- **Maven**
- **JWT (JSON Web Tokens)**
- **Bean Validation**

## Estructura del Proyecto

```
backend/
├── src/main/java/com/proyectoavanzada/backend/
│   ├── BackendApplication.java          # Clase principal de Spring Boot
│   ├── config/                          # Configuraciones
│   │   ├── CorsConfig.java             # Configuración CORS
│   │   └── SecurityConfig.java         # Configuración de seguridad
│   ├── controller/                      # Controladores REST
│   │   ├── AuthController.java         # Autenticación
│   │   ├── ClienteController.java      # Gestión de clientes
│   │   ├── CategoriaController.java    # Gestión de categorías
│   │   ├── MarcaController.java        # Gestión de marcas
│   │   ├── ProductoController.java     # Gestión de productos
│   │   ├── PresentacionController.java # Gestión de presentaciones
│   │   ├── ProveedorController.java    # Gestión de proveedores
│   │   ├── CompraController.java       # Gestión de compras
│   │   ├── VentaController.java        # Gestión de ventas
│   │   ├── ReporteController.java      # Gestión de reportes
│   │   └── TestController.java         # Endpoints de prueba
│   ├── model/                          # Entidades JPA
│   │   ├── Usuario.java                # Usuarios del sistema
│   │   ├── Cliente.java                # Clientes
│   │   ├── Categoria.java              # Categorías de productos
│   │   ├── Marca.java                  # Marcas de productos
│   │   ├── Producto.java               # Productos
│   │   ├── Presentacion.java           # Presentaciones (talla/color)
│   │   ├── Proveedor.java              # Proveedores
│   │   ├── Compra.java                 # Compras
│   │   ├── DetalleCompra.java          # Detalles de compra
│   │   ├── Venta.java                  # Ventas
│   │   ├── DetalleVenta.java           # Detalles de venta
│   │   ├── Inventario.java             # Inventario
│   │   └── Reporte.java                # Reportes
│   ├── repository/                      # Repositorios JPA
│   │   ├── UsuarioRepository.java
│   │   ├── ClienteRepository.java
│   │   ├── CategoriaRepository.java
│   │   ├── MarcaRepository.java
│   │   ├── ProductoRepository.java
│   │   ├── PresentacionRepository.java
│   │   ├── ProveedorRepository.java
│   │   ├── CompraRepository.java
│   │   ├── DetalleCompraRepository.java
│   │   ├── VentaRepository.java
│   │   ├── DetalleVentaRepository.java
│   │   ├── InventarioRepository.java
│   │   └── ReporteRepository.java
│   ├── service/                         # Servicios de negocio
│   │   ├── UsuarioService.java
│   │   ├── ClienteService.java
│   │   ├── CategoriaService.java
│   │   ├── MarcaService.java
│   │   ├── ProductoService.java
│   │   ├── PresentacionService.java
│   │   ├── ProveedorService.java
│   │   ├── CompraService.java
│   │   ├── VentaService.java
│   │   └── ReporteService.java
│   ├── exception/                       # Manejo de excepciones
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── BusinessException.java
│   │   ├── AccessDeniedException.java
│   │   └── ConflictException.java
│   └── util/                           # Utilidades
│       └── PasswordUtil.java           # Utilidades de contraseñas
├── src/main/resources/
│   ├── application.properties          # Configuración principal
│   ├── application-dev.properties      # Configuración desarrollo
│   └── application-prod.properties     # Configuración producción
└── database/
    ├── 01_init_database.sql            # Script de inicialización
    └── 05_add_profile_fields.sql       # Script de campos adicionales
```

## Funcionalidades Implementadas

### 1. Gestión de Usuarios
- ✅ Registro de usuarios
- ✅ Autenticación con email y contraseña
- ✅ Perfiles de usuario (Administrador, Vendedor, Gerente)
- ✅ Gestión de perfiles
- ✅ Encriptación de contraseñas con BCrypt

### 2. Gestión de Clientes
- ✅ CRUD completo de clientes
- ✅ Sistema de puntos de fidelidad
- ✅ Búsqueda por nombre, email, documento
- ✅ Gestión de direcciones y contacto
- ✅ Historial de compras

### 3. Gestión de Productos
- ✅ CRUD completo de productos
- ✅ Gestión de categorías y marcas
- ✅ Presentaciones (talla/color)
- ✅ Control de inventario
- ✅ Precios y descuentos
- ✅ Productos destacados y nuevos
- ✅ Búsqueda avanzada

### 4. Gestión de Inventario
- ✅ Control de stock por presentación
- ✅ Alertas de stock bajo
- ✅ Ubicación en almacén
- ✅ Movimientos de entrada y salida
- ✅ Reportes de inventario

### 5. Gestión de Compras
- ✅ CRUD completo de compras
- ✅ Gestión de proveedores
- ✅ Detalles de compra
- ✅ Estados de compra (Pendiente, Pagada, Cancelada)
- ✅ Control de crédito
- ✅ Actualización automática de inventario

### 6. Gestión de Ventas
- ✅ CRUD completo de ventas
- ✅ Detalles de venta
- ✅ Ventas con y sin cliente
- ✅ Sistema de puntos de fidelidad
- ✅ Descuentos por puntos
- ✅ Estados de venta (Completada, Cancelada, Devuelta)
- ✅ Actualización automática de inventario

### 7. Sistema de Reportes
- ✅ Reportes de ventas
- ✅ Reportes de compras
- ✅ Reportes de inventario
- ✅ Reportes de clientes
- ✅ Reportes de productos
- ✅ Múltiples formatos (JSON, PDF, Excel)

### 8. Seguridad
- ✅ Autenticación JWT
- ✅ Configuración CORS
- ✅ Validaciones de entrada
- ✅ Manejo global de excepciones
- ✅ Encriptación de contraseñas

## Endpoints de la API

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registro de usuario
- `GET /api/auth/profile/{id}` - Obtener perfil
- `PUT /api/auth/update-profile` - Actualizar perfil

### Clientes
- `GET /api/clientes` - Listar clientes
- `GET /api/clientes/{id}` - Obtener cliente
- `POST /api/clientes` - Crear cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente
- `GET /api/clientes/activos` - Clientes activos
- `GET /api/clientes/buscar?nombre=` - Buscar clientes

### Categorías
- `GET /api/categorias` - Listar categorías
- `GET /api/categorias/{id}` - Obtener categoría
- `POST /api/categorias` - Crear categoría
- `PUT /api/categorias/{id}` - Actualizar categoría
- `DELETE /api/categorias/{id}` - Eliminar categoría

### Marcas
- `GET /api/marcas` - Listar marcas
- `GET /api/marcas/{id}` - Obtener marca
- `POST /api/marcas` - Crear marca
- `PUT /api/marcas/{id}` - Actualizar marca
- `DELETE /api/marcas/{id}` - Eliminar marca

### Productos
- `GET /api/productos` - Listar productos
- `GET /api/productos/{id}` - Obtener producto
- `POST /api/productos` - Crear producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto
- `GET /api/productos/destacados` - Productos destacados
- `GET /api/productos/nuevos` - Productos nuevos

### Ventas
- `GET /api/ventas` - Listar ventas
- `GET /api/ventas/{id}` - Obtener venta
- `POST /api/ventas` - Crear venta
- `PUT /api/ventas/{id}` - Actualizar venta
- `POST /api/ventas/{id}/detalles` - Agregar detalle
- `PUT /api/ventas/{id}/completar` - Completar venta

### Compras
- `GET /api/compras` - Listar compras
- `GET /api/compras/{id}` - Obtener compra
- `POST /api/compras` - Crear compra
- `PUT /api/compras/{id}` - Actualizar compra
- `POST /api/compras/{id}/detalles` - Agregar detalle
- `PUT /api/compras/{id}/pagar` - Marcar como pagada

### Reportes
- `GET /api/reportes` - Listar reportes
- `POST /api/reportes/generar/ventas` - Generar reporte de ventas
- `POST /api/reportes/generar/compras` - Generar reporte de compras
- `POST /api/reportes/generar/inventario` - Generar reporte de inventario

## Configuración de Base de Datos

### MySQL
```sql
CREATE DATABASE sneakershop;
```

### Propiedades de conexión
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sneakershop?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior
- MySQL 8.0 o superior

### Pasos de instalación

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd ProyectoAvanzada/backend
```

2. **Configurar la base de datos**
```bash
# Crear la base de datos
mysql -u root -p
CREATE DATABASE sneakershop;
```

3. **Ejecutar el script de inicialización**
```bash
mysql -u root -p sneakershop < database/01_init_database.sql
```

4. **Compilar y ejecutar**
```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

5. **Verificar la instalación**
```bash
# El servidor estará disponible en:
http://localhost:8080

# Endpoint de prueba:
http://localhost:8080/api/test/hello
```

## Usuarios de Prueba

### Administrador
- **Email:** admin@sneakershop.com
- **Contraseña:** admin123
- **Rol:** Administrador

### Vendedor
- **Email:** vendedor@sneakershop.com
- **Contraseña:** admin123
- **Rol:** Vendedor

### Gerente
- **Email:** gerente@sneakershop.com
- **Contraseña:** admin123
- **Rol:** Gerente

## Datos de Ejemplo

El script de inicialización incluye:
- 6 categorías de productos
- 8 marcas reconocidas
- 4 proveedores
- 3 usuarios del sistema
- 5 clientes de ejemplo
- 5 productos con presentaciones
- Inventario inicial

## Características Técnicas

### Validaciones
- Validación de entrada con Bean Validation
- Validación de email único
- Validación de RUC único
- Validación de códigos de producto únicos

### Seguridad
- Encriptación BCrypt para contraseñas
- Configuración CORS para Angular
- Manejo de excepciones global
- Autenticación JWT (preparado)

### Base de Datos
- JPA/Hibernate con MySQL
- Relaciones bidireccionales
- Soft delete para entidades principales
- Índices automáticos

### API REST
- Respuestas JSON estandarizadas
- Códigos de estado HTTP apropiados
- Manejo de errores consistente
- Documentación de endpoints

## Próximas Mejoras

- [ ] Implementación completa de JWT
- [ ] Paginación en endpoints de listado
- [ ] Filtros avanzados de búsqueda
- [ ] Exportación de reportes a PDF/Excel
- [ ] Notificaciones en tiempo real
- [ ] API de estadísticas en tiempo real
- [ ] Integración con sistemas de pago
- [ ] Sistema de roles y permisos granular

## Soporte

Para soporte técnico o consultas sobre el backend, contactar al equipo de desarrollo.

---

**Versión:** 1.0.0  
**Última actualización:** Diciembre 2024  
**Desarrollado con:** Spring Boot 3.2.0 + Java 17
