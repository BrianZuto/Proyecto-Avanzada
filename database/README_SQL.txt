================================================================================
                    DOCUMENTACIÓN DE SCRIPTS SQL
                    Sistema de Venta de Sneakers
================================================================================

INFORMACIÓN GENERAL
-------------------
Este directorio contiene todos los scripts SQL necesarios para configurar y 
mantener la base de datos del sistema de venta de sneakers.

Los scripts deben ejecutarse en el orden indicado para garantizar la 
integridad de la base de datos.

================================================================================
ORDEN DE EJECUCIÓN DE SCRIPTS
================================================================================

1. 01_init_database.sql
   - Crea la base de datos 'sneakershop'
   - Establece el conjunto de caracteres UTF-8
   - Debe ejecutarse primero

2. 02_create_tables.sql
   - Crea todas las tablas del sistema
   - Define relaciones entre tablas
   - Establece índices y claves foráneas
   - Debe ejecutarse después de crear la base de datos

3. 03_insert_sample_data.sql
   - Inserta datos de ejemplo para pruebas
   - Incluye usuarios, productos, marcas, categorías
   - Facilita el desarrollo y testing

4. 05_add_profile_fields.sql
   - Agrega campos adicionales al perfil de usuario
   - Campos: telefono, fecha_nacimiento
   - Actualización de esquema existente

5. 06_create_direcciones_table.sql
   - Crea la tabla de direcciones de envío
   - Permite a los usuarios gestionar múltiples direcciones
   - Relacionada con la tabla de usuarios

6. 07_create_metodos_pago_table.sql
   - Crea la tabla de métodos de pago
   - Permite almacenar tarjetas y métodos de pago
   - Relacionada con la tabla de usuarios

7. 08_alter_inventario_table.sql
   - Modifica la tabla de inventario
   - Agrega campos adicionales si es necesario
   - Actualización de esquema

8. 09_alter_detalles_compra_table.sql
   - Modifica la tabla de detalles de compra
   - Ajustes en la estructura según necesidades
   - Actualización de esquema

9. 10_alter_detalles_venta_table.sql
   - Modifica la tabla de detalles de venta
   - Ajustes en la estructura según necesidades
   - Actualización de esquema

10. 11_asignar_rol_admin.sql
    - Asigna rol de administrador a usuarios específicos
    - Útil para configurar permisos iniciales
    - Debe ejecutarse después de crear usuarios

================================================================================
ESTRUCTURA DE LA BASE DE DATOS
================================================================================

TABLAS PRINCIPALES:
------------------
- usuarios: Gestión de usuarios del sistema
- clientes: Información de clientes
- productos: Catálogo de productos (sneakers)
- categorias: Categorías de productos
- marcas: Marcas de productos
- inventario: Control de stock de productos
- compras: Registro de compras a proveedores
- ventas: Registro de ventas a clientes
- detalles_compra: Detalles de productos en compras
- detalles_venta: Detalles de productos en ventas
- direcciones: Direcciones de envío de usuarios
- metodos_pago: Métodos de pago de usuarios
- reportes: Generación de reportes del sistema

RELACIONES PRINCIPALES:
----------------------
- Usuarios -> Clientes (1:1)
- Productos -> Categorías (N:1)
- Productos -> Marcas (N:1)
- Ventas -> Usuarios (N:1)
- Ventas -> Clientes (N:1)
- DetallesVenta -> Ventas (N:1)
- DetallesVenta -> Productos (N:1)
- Direcciones -> Usuarios (N:1)
- MetodosPago -> Usuarios (N:1)

================================================================================
INSTRUCCIONES DE USO
================================================================================

PARA INSTALACIÓN INICIAL:
------------------------
1. Ejecutar 01_init_database.sql para crear la base de datos
2. Ejecutar 02_create_tables.sql para crear todas las tablas
3. Ejecutar 03_insert_sample_data.sql para datos de ejemplo
4. Ejecutar scripts de actualización (05-11) según necesidad

PARA ACTUALIZACIÓN:
------------------
Si ya tienes la base de datos instalada, ejecuta solo los scripts de 
actualización (05-11) que necesites según las nuevas funcionalidades.

PARA RESETEAR LA BASE DE DATOS:
-------------------------------
1. Eliminar la base de datos: DROP DATABASE sneakershop;
2. Ejecutar todos los scripts en orden (01, 02, 03, y luego 05-11)

================================================================================
NOTAS IMPORTANTES
================================================================================

- Todos los scripts están diseñados para MySQL 8.0+
- Se recomienda hacer backup antes de ejecutar scripts de actualización
- Los scripts de actualización (05-11) son idempotentes cuando es posible
- Verificar que no haya conflictos antes de ejecutar scripts de alteración
- Los datos de ejemplo en 03_insert_sample_data.sql pueden modificarse

================================================================================
CREDENCIALES POR DEFECTO (DATOS DE EJEMPLO)
================================================================================

ADMINISTRADOR:
------------
Email: admin@sneakershop.com
Password: (verificar en script 03_insert_sample_data.sql)

USUARIO DE PRUEBA:
-----------------
Email: usuario@test.com
Password: (verificar en script 03_insert_sample_data.sql)

NOTA: Las contraseñas están hasheadas con BCrypt. Para crear nuevas 
contraseñas, usar el endpoint /api/usuarios/generate-hash del backend.

================================================================================
MANTENIMIENTO
================================================================================

BACKUP:
------
Se recomienda hacer backup regular de la base de datos:

mysqldump -u root -p sneakershop > backup_sneakershop_YYYYMMDD.sql

RESTAURAR:
---------
mysql -u root -p sneakershop < backup_sneakershop_YYYYMMDD.sql

================================================================================
SOPORTE
================================================================================

Para problemas o consultas sobre los scripts SQL, revisar:
- Logs del servidor MySQL
- Documentación de MySQL 8.0
- Issues en el repositorio del proyecto

================================================================================
FIN DEL DOCUMENTO
================================================================================

