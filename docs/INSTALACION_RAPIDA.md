# 🚀 Instalación Rápida - Proyecto Avanzada

## Pasos para ejecutar el proyecto completo

### 1. Preparar el entorno

```bash
# Verificar que XAMPP esté ejecutándose
# MySQL debe estar activo en el puerto 3306
```

### 2. Configurar la base de datos

```sql
-- Ejecutar en phpMyAdmin (http://localhost/phpmyadmin)
-- Copiar y pegar el contenido de database/01_create_database.sql
```

### 3. Ejecutar el Backend

```bash
# Terminal 1 - Backend
cd backend
mvn spring-boot:run
```

**✅ Backend ejecutándose en:** http://localhost:8080/api

### 4. Ejecutar el Frontend

```bash
# Terminal 2 - Frontend
cd frontend/frontend-app
ng serve
```

**✅ Frontend ejecutándose en:** http://localhost:4200

### 5. Verificar la aplicación

1. Abrir navegador en: http://localhost:4200
2. Deberías ver la aplicación funcionando
3. Probar crear, editar y eliminar usuarios

## 🔧 Comandos útiles

### Backend
```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Generar JAR
mvn clean package
```

### Frontend
```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
ng serve

# Compilar para producción
ng build --prod

# Ejecutar tests
ng test
```

## 🐛 Solución rápida de problemas

### Error: "Cannot connect to database"
- ✅ Verificar que XAMPP esté ejecutándose
- ✅ Verificar que MySQL esté activo
- ✅ Revisar credenciales en `application.properties`

### Error: "CORS policy"
- ✅ Verificar que el frontend esté en puerto 4200
- ✅ Verificar que el backend esté en puerto 8080

### Error: "ng command not found"
```bash
npm install -g @angular/cli
```

### Error: "Maven not found"
- ✅ Instalar Maven o usar el wrapper: `./mvnw spring-boot:run`

## 📱 URLs importantes

- **Frontend:** http://localhost:4200
- **Backend API:** http://localhost:8080/api
- **phpMyAdmin:** http://localhost/phpmyadmin
- **API Docs:** http://localhost:8080/api/usuarios

## 🎯 Próximos pasos

1. ✅ Probar todas las funcionalidades
2. ✅ Revisar el código fuente
3. ✅ Personalizar según necesidades
4. ✅ Agregar nuevas funcionalidades

---

**¡Listo! Tu aplicación full stack está funcionando** 🎉
