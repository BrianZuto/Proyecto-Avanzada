# Documentación de Swagger/OpenAPI

## Descripción
Este proyecto ahora incluye documentación automática de la API usando SpringDoc OpenAPI (Swagger). La documentación se genera automáticamente basada en las anotaciones de los controladores.

## Configuración Implementada

### 1. Dependencias Agregadas
- **SpringDoc OpenAPI Starter WebMVC UI**: `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0`

### 2. Configuración de OpenAPI
- **Archivo**: `src/main/java/com/proyectoavanzada/backend/config/OpenApiConfig.java`
- **Funcionalidades**:
  - Información general de la API
  - Configuración de servidores (desarrollo y producción)
  - Configuración de autenticación JWT
  - Metadatos del proyecto

### 3. Configuración de Swagger UI
- **Archivo**: `src/main/resources/application.properties`
- **Configuraciones**:
  - Ruta de documentación: `/api-docs`
  - Interfaz Swagger UI: `/swagger-ui.html`
  - Ordenamiento por método y tags
  - Filtros habilitados
  - Duración de requests visible

## Acceso a la Documentación

### URLs Disponibles
Una vez que el backend esté ejecutándose:

1. **Swagger UI (Interfaz Interactiva)**:
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Documentación JSON de OpenAPI**:
   ```
   http://localhost:8080/api-docs
   ```

3. **Documentación YAML de OpenAPI**:
   ```
   http://localhost:8080/api-docs.yaml
   ```

## Controladores Documentados

### 1. Autenticación (`/api/auth`)
- **Tag**: "Autenticación"
- **Endpoints**:
  - `POST /login` - Iniciar sesión
  - `POST /register` - Registrar usuario
  - `POST /generate-hash` - Generar hash de contraseña
  - `POST /check-email` - Verificar email
  - `GET /profile/{userId}` - Obtener perfil
  - `PUT /update-profile` - Actualizar perfil

### 2. Gestión de Usuarios (`/api/usuarios`)
- **Tag**: "Gestión de Usuarios"
- **Endpoints**:
  - `GET /` - Obtener todos los usuarios
  - `GET /{id}` - Obtener usuario por ID
  - `POST /` - Crear usuario
  - `PUT /{id}` - Actualizar usuario
  - `DELETE /{id}` - Eliminar usuario
  - `GET /activos` - Obtener usuarios activos

### 3. Gestión de Productos (`/api/productos`)
- **Tag**: "Gestión de Productos"
- **Endpoints**:
  - `GET /` - Obtener todos los productos
  - `GET /{id}` - Obtener producto por ID
  - `POST /` - Crear producto
  - `PUT /{id}` - Actualizar producto
  - `DELETE /{id}` - Eliminar producto
  - Y muchos más endpoints especializados

### 4. Gestión de Clientes (`/api/clientes`)
- **Tag**: "Gestión de Clientes"

### 5. Gestión de Categorías (`/api/categorias`)
- **Tag**: "Gestión de Categorías"

### 6. Gestión de Marcas (`/api/marcas`)
- **Tag**: "Gestión de Marcas"

### 7. Gestión de Ventas (`/api/ventas`)
- **Tag**: "Gestión de Ventas"

### 8. Gestión de Compras (`/api/compras`)
- **Tag**: "Gestión de Compras"

### 9. Gestión de Reportes (`/api/reportes`)
- **Tag**: "Gestión de Reportes"

### 10. Endpoints de Prueba (`/api`)
- **Tag**: "Endpoints de Prueba"
- **Endpoints**:
  - `GET /test` - Prueba del sistema
  - `GET /health` - Estado de salud

## Características de la Documentación

### 1. Información Detallada
- **Título**: "Sistema de Venta de Tenis (Sneakers) API"
- **Descripción**: API REST completa para un sistema de venta de tenis
- **Versión**: 1.0.0
- **Contacto**: Equipo de Desarrollo
- **Licencia**: MIT

### 2. Ejemplos de Respuesta
- Cada endpoint incluye ejemplos de respuesta exitosa y de error
- Códigos de estado HTTP documentados
- Esquemas de datos detallados

### 3. Autenticación
- Configuración para autenticación JWT
- Esquema de seguridad "Bearer Authentication"
- Documentación de cómo usar tokens

### 4. Interfaz Interactiva
- Prueba de endpoints directamente desde el navegador
- Autenticación integrada
- Filtros y búsqueda
- Ordenamiento por método y tags

## Cómo Usar Swagger UI

### 1. Acceder a la Interfaz
1. Ejecuta el backend: `mvn spring-boot:run`
2. Abre el navegador en: `http://localhost:8080/swagger-ui.html`

### 2. Probar Endpoints
1. Expande la sección del controlador que desees probar
2. Haz clic en "Try it out" en el endpoint deseado
3. Completa los parámetros requeridos
4. Haz clic en "Execute"
5. Revisa la respuesta

### 3. Autenticación
1. Primero usa el endpoint `/api/auth/login` para obtener credenciales
2. Copia el token de respuesta (si se implementa JWT)
3. Haz clic en "Authorize" en la parte superior de Swagger UI
4. Ingresa "Bearer {tu-token}" en el campo
5. Ahora puedes probar endpoints protegidos

## Beneficios de la Implementación

### 1. Documentación Automática
- Se actualiza automáticamente con cambios en el código
- No requiere mantenimiento manual
- Siempre sincronizada con la implementación

### 2. Pruebas Integradas
- Interfaz para probar endpoints sin herramientas externas
- Validación de esquemas automática
- Ejemplos de uso incluidos

### 3. Colaboración Mejorada
- Frontend developers pueden entender la API fácilmente
- Documentación clara para nuevos desarrolladores
- Especificación OpenAPI estándar

### 4. Desarrollo Eficiente
- Reducción de tiempo en documentación manual
- Menos errores en integración
- Mejor comunicación entre equipos

## Próximos Pasos

### 1. Implementar JWT
- Agregar generación de tokens JWT en el login
- Implementar validación de tokens en endpoints protegidos
- Configurar Swagger para usar JWT automáticamente

### 2. Mejorar Documentación
- Agregar más ejemplos de request/response
- Documentar códigos de error específicos
- Agregar descripciones más detalladas

### 3. Configuración de Producción
- Deshabilitar Swagger UI en producción
- Configurar diferentes URLs para diferentes entornos
- Agregar autenticación adicional si es necesario

## Comandos Útiles

### Ejecutar Backend con Swagger
```bash
# Desde la carpeta backend
mvn spring-boot:run
```

### Acceder a Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Ver documentación JSON
```bash
curl http://localhost:8080/api-docs
```

## Notas Importantes

1. **Desarrollo vs Producción**: Swagger UI está habilitado para desarrollo. En producción, considera deshabilitarlo por seguridad.

2. **CORS**: La configuración CORS permite acceso desde `http://localhost:4200` (Angular frontend).

3. **Autenticación**: Actualmente la autenticación está configurada pero no implementada completamente. Los endpoints están marcados como protegidos pero no validan tokens JWT aún.

4. **Actualizaciones**: La documentación se actualiza automáticamente cuando cambias las anotaciones en los controladores.

¡La implementación de Swagger está completa y lista para usar! 🚀
