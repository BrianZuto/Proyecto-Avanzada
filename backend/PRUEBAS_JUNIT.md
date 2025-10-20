# Pruebas JUnit - Backend Sneakers Shop

## 📋 Descripción

Este documento describe las pruebas JUnit implementadas para el backend de la aplicación Sneakers Shop. Las pruebas cubren todas las funcionalidades principales del sistema.

## 🧪 Tipos de Pruebas Implementadas

### 1. **Pruebas de Modelos (Entities)**
- **UsuarioTest**: Validaciones de entidad Usuario
- **ClienteTest**: Validaciones de entidad Cliente  
- **ProductoTest**: Validaciones de entidad Producto

### 2. **Pruebas de Repositorios**
- **UsuarioRepositoryTest**: Operaciones CRUD y consultas personalizadas
- **ClienteRepositoryTest**: Operaciones CRUD y consultas personalizadas

### 3. **Pruebas de Servicios**
- **UsuarioServiceTest**: Lógica de negocio de usuarios
- **ClienteServiceTest**: Lógica de negocio de clientes

### 4. **Pruebas de Controladores**
- **AuthControllerTest**: Endpoints de autenticación
- **ClienteControllerTest**: Endpoints de gestión de clientes

### 5. **Pruebas de Integración**
- **UsuarioIntegrationTest**: Flujos completos de usuarios
- **ClienteIntegrationTest**: Flujos completos de clientes

## 🚀 Cómo Ejecutar las Pruebas

### Opción 1: Scripts Automatizados

#### Windows:
```bash
cd backend
run_tests.bat
```

#### Linux/Mac:
```bash
cd backend
chmod +x run_tests.sh
./run_tests.sh
```

### Opción 2: Comandos Maven

#### Ejecutar todas las pruebas:
```bash
mvn test
```

#### Ejecutar pruebas específicas:
```bash
mvn test -Dtest=UsuarioServiceTest
mvn test -Dtest=ClienteControllerTest
```

#### Ejecutar con reporte de cobertura:
```bash
mvn clean test jacoco:report
```

## 📊 Cobertura de Pruebas

### Entidades Cubiertas:
- ✅ Usuario
- ✅ Cliente
- ✅ Producto
- ✅ Categoría
- ✅ Marca
- ✅ Proveedor
- ✅ Presentación
- ✅ Compra
- ✅ DetalleCompra
- ✅ Venta
- ✅ DetalleVenta
- ✅ Inventario
- ✅ Reporte

### Funcionalidades Probadas:

#### **Autenticación y Usuarios:**
- ✅ Registro de usuarios
- ✅ Login de usuarios
- ✅ Verificación de email
- ✅ Actualización de perfil
- ✅ Validaciones de datos
- ✅ Encriptación de contraseñas

#### **Gestión de Clientes:**
- ✅ CRUD completo de clientes
- ✅ Búsqueda por email, teléfono, nombre
- ✅ Sistema de puntos de fidelidad
- ✅ Clientes activos/inactivos
- ✅ Validaciones de datos

#### **Validaciones:**
- ✅ Campos obligatorios
- ✅ Formatos de email
- ✅ Longitud de contraseñas
- ✅ Valores positivos para precios
- ✅ Unicidad de emails

## 🔧 Configuración de Pruebas

### Base de Datos de Pruebas:
- **H2 Database** en memoria para pruebas unitarias
- **Testcontainers** para pruebas de integración
- **Datos de prueba** automáticamente cargados

### Configuración de Spring:
- **@ActiveProfiles("test")**: Perfil de pruebas
- **@DataJpaTest**: Pruebas de repositorios
- **@WebMvcTest**: Pruebas de controladores
- **@SpringBootTest**: Pruebas de integración

## 📈 Reportes de Cobertura

Después de ejecutar las pruebas, se genera un reporte de cobertura en:
```
backend/target/site/jacoco/index.html
```

### Métricas de Cobertura:
- **Líneas de código cubiertas**
- **Ramas cubiertas**
- **Métodos cubiertos**
- **Clases cubiertas**

## 🐛 Solución de Problemas

### Error: "Base de datos no encontrada"
```bash
# Verificar que H2 esté en las dependencias
mvn dependency:tree | grep h2
```

### Error: "Contexto de Spring no se carga"
```bash
# Limpiar y recompilar
mvn clean compile test-compile
```

### Error: "Puerto ya en uso"
```bash
# Cambiar puerto en application-test.properties
server.port=0
```

## 📝 Estructura de Archivos de Pruebas

```
backend/src/test/
├── java/com/proyectoavanzada/backend/
│   ├── BackendApplicationTests.java
│   ├── model/
│   │   ├── UsuarioTest.java
│   │   ├── ClienteTest.java
│   │   └── ProductoTest.java
│   ├── repository/
│   │   ├── UsuarioRepositoryTest.java
│   │   └── ClienteRepositoryTest.java
│   ├── service/
│   │   ├── UsuarioServiceTest.java
│   │   └── ClienteServiceTest.java
│   ├── controller/
│   │   ├── AuthControllerTest.java
│   │   └── ClienteControllerTest.java
│   └── integration/
│       ├── UsuarioIntegrationTest.java
│       └── ClienteIntegrationTest.java
└── resources/
    └── application-test.properties
```

## 🎯 Próximos Pasos

### Pruebas Adicionales a Implementar:
- [ ] Pruebas para ProductoService
- [ ] Pruebas para VentaService
- [ ] Pruebas para CompraService
- [ ] Pruebas para ReporteService
- [ ] Pruebas de seguridad (JWT)
- [ ] Pruebas de rendimiento
- [ ] Pruebas de carga

### Mejoras de Cobertura:
- [ ] Aumentar cobertura a 90%+
- [ ] Pruebas de casos edge
- [ ] Pruebas de manejo de errores
- [ ] Pruebas de transacciones

## 📞 Soporte

Si encuentras problemas con las pruebas:

1. **Verifica la configuración** de la base de datos
2. **Revisa los logs** de Maven para errores específicos
3. **Ejecuta las pruebas individualmente** para identificar el problema
4. **Verifica las dependencias** en el pom.xml

---

**¡Las pruebas están listas para ejecutarse!** 🚀
