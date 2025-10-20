-- Script para insertar datos de prueba en la base de datos sneakershop

USE sneakershop;

-- Insertar usuarios de prueba
INSERT INTO usuarios (nombre, email, password, rol, telefono, fecha_nacimiento) VALUES
('Admin Sistema', 'admin@sneakershop.com', '$2a$10$encoded_password', 'ADMIN', '1234567890', '1990-01-01'),
('Vendedor 1', 'vendedor1@sneakershop.com', '$2a$10$encoded_password', 'VENDEDOR', '0987654321', '1992-05-15'),
('Vendedor 2', 'vendedor2@sneakershop.com', '$2a$10$encoded_password', 'VENDEDOR', '1122334455', '1988-12-20');

-- Insertar categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Zapatillas Deportivas', 'Zapatillas para deportes y actividades físicas'),
('Zapatillas Casual', 'Zapatillas para uso diario y casual'),
('Zapatillas de Running', 'Zapatillas especializadas para correr'),
('Zapatillas de Baloncesto', 'Zapatillas para jugar baloncesto'),
('Zapatillas de Fútbol', 'Zapatillas para jugar fútbol');

-- Insertar marcas
INSERT INTO marcas (nombre, descripcion, logo_url) VALUES
('Nike', 'Just Do It - Innovación en calzado deportivo', 'nike_logo.png'),
('Adidas', 'Impossible is Nothing - Calzado deportivo premium', 'adidas_logo.png'),
('Puma', 'Forever Faster - Calzado deportivo de alto rendimiento', 'puma_logo.png'),
('Jordan', 'Air Jordan - Calzado de baloncesto icónico', 'jordan_logo.png'),
('Converse', 'All Star - Calzado casual clásico', 'converse_logo.png');

-- Insertar productos
INSERT INTO productos (nombre, descripcion, codigo_producto, categoria_id, marca_id, precio_venta, precio_compra, genero, edad_target, material_principal, tipo_suela, tecnologia, stock_minimo) VALUES
('Air Max 270', 'Zapatillas de running con tecnología Air Max', 'NIKE-AM270-001', 3, 1, 150.00, 100.00, 'Unisex', 'Adulto', 'Mesh', 'Goma', 'Air Max', 5),
('Ultraboost 22', 'Zapatillas de running con tecnología Boost', 'ADIDAS-UB22-001', 3, 2, 180.00, 120.00, 'Unisex', 'Adulto', 'Primeknit', 'Boost', 'Boost', 5),
('Air Jordan 1', 'Zapatillas de baloncesto icónicas', 'JORDAN-AJ1-001', 4, 4, 170.00, 110.00, 'Unisex', 'Adulto', 'Cuero', 'Goma', 'Air', 5),
('Chuck Taylor All Star', 'Zapatillas casuales clásicas', 'CONVERSE-CT-001', 2, 5, 60.00, 40.00, 'Unisex', 'Adulto', 'Lona', 'Goma', 'N/A', 5),
('RS-X Reinvention', 'Zapatillas deportivas futuristas', 'PUMA-RSX-001', 1, 3, 120.00, 80.00, 'Unisex', 'Adulto', 'Mesh', 'Goma', 'RS', 5);

-- Insertar presentaciones
INSERT INTO presentaciones (producto_id, talla, color, precio_venta, precio_compra, stock_actual, stock_minimo) VALUES
(1, '40', 'Negro/Blanco', 150.00, 100.00, 10, 5),
(1, '41', 'Negro/Blanco', 150.00, 100.00, 8, 5),
(1, '42', 'Negro/Blanco', 150.00, 100.00, 12, 5),
(2, '40', 'Blanco/Negro', 180.00, 120.00, 15, 5),
(2, '41', 'Blanco/Negro', 180.00, 120.00, 10, 5),
(3, '40', 'Rojo/Blanco', 170.00, 110.00, 6, 5),
(3, '41', 'Rojo/Blanco', 170.00, 110.00, 8, 5),
(4, '40', 'Negro', 60.00, 40.00, 20, 5),
(4, '41', 'Negro', 60.00, 40.00, 18, 5),
(5, '40', 'Azul/Blanco', 120.00, 80.00, 7, 5);

-- Insertar clientes
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, codigo_postal, fecha_nacimiento, puntos_fidelidad, numero_documento, tipo_documento) VALUES
('Juan', 'Pérez', 'juan.perez@email.com', '1234567890', 'Calle Principal 123', 'Madrid', '28001', '1990-05-15', 100, '12345678A', 'DNI'),
('María', 'García', 'maria.garcia@email.com', '0987654321', 'Avenida Central 456', 'Barcelona', '08001', '1988-12-20', 50, '87654321B', 'DNI'),
('Carlos', 'López', 'carlos.lopez@email.com', '1122334455', 'Plaza Mayor 789', 'Valencia', '46001', '1992-08-10', 0, '11223344C', 'DNI'),
('Ana', 'Martín', 'ana.martin@email.com', '5566778899', 'Calle Secundaria 321', 'Sevilla', '41001', '1995-03-25', 75, '55667788D', 'DNI');

-- Insertar proveedores
INSERT INTO proveedores (nombre, ruc, email, telefono, direccion, ciudad, contacto_nombre, contacto_telefono, contacto_email, limite_credito, credito_dias) VALUES
('Nike España', 'A12345678', 'contacto@nike.es', '900123456', 'Calle Nike 1', 'Madrid', 'Pedro González', '600123456', 'pedro@nike.es', 50000.00, 30),
('Adidas España', 'B87654321', 'contacto@adidas.es', '900654321', 'Avenida Adidas 2', 'Barcelona', 'Laura Sánchez', '600654321', 'laura@adidas.es', 45000.00, 30),
('Puma España', 'C11223344', 'contacto@puma.es', '900789012', 'Plaza Puma 3', 'Valencia', 'Miguel Torres', '600789012', 'miguel@puma.es', 30000.00, 15);

-- Insertar inventario
INSERT INTO inventario (producto_id, presentacion_id, stock_actual, stock_minimo, ubicacion_almacen) VALUES
(1, 1, 10, 5, 'A-01-01'),
(1, 2, 8, 5, 'A-01-02'),
(1, 3, 12, 5, 'A-01-03'),
(2, 4, 15, 5, 'A-02-01'),
(2, 5, 10, 5, 'A-02-02'),
(3, 6, 6, 5, 'A-03-01'),
(3, 7, 8, 5, 'A-03-02'),
(4, 8, 20, 5, 'A-04-01'),
(4, 9, 18, 5, 'A-04-02'),
(5, 10, 7, 5, 'A-05-01');

-- Insertar algunas ventas de ejemplo
INSERT INTO ventas (numero_venta, cliente_id, usuario_id, subtotal, impuesto, total, metodo_pago, puntos_otorgados, puntos_usados) VALUES
('V-2024-001', 1, 2, 150.00, 31.50, 181.50, 'TARJETA', 15, 0),
('V-2024-002', 2, 2, 180.00, 37.80, 217.80, 'EFECTIVO', 18, 0),
('V-2024-003', 3, 3, 60.00, 12.60, 72.60, 'TARJETA', 6, 0);

-- Insertar detalles de venta
INSERT INTO detalles_venta (venta_id, producto_id, presentacion_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 1, 150.00, 150.00),
(2, 2, 4, 1, 180.00, 180.00),
(3, 4, 8, 1, 60.00, 60.00);

-- Insertar algunos reportes de ejemplo
INSERT INTO reportes (nombre, descripcion, tipo_reporte, fecha_inicio, fecha_fin, formato, estado, usuario_id) VALUES
('Reporte de Ventas Diarias', 'Reporte de ventas del día actual', 'VENTAS_DIARIAS', CURDATE(), CURDATE(), 'PDF', 'GENERADO', 1),
('Reporte de Inventario', 'Estado actual del inventario', 'INVENTARIO', NULL, NULL, 'EXCEL', 'GENERADO', 1),
('Reporte de Clientes', 'Listado de clientes activos', 'CLIENTES', NULL, NULL, 'PDF', 'GENERADO', 1);
