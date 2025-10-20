-- Script de inicialización de la base de datos para la aplicación de venta de tenis
-- Ejecutar este script DESPUÉS de que el backend haya iniciado al menos una vez
-- y las tablas se hayan creado automáticamente.

USE sneakershop;

-- Insertar categorías de ejemplo
INSERT INTO categorias (nombre, descripcion, activo, fecha_creacion, color_hex) VALUES
('Deportivos', 'Zapatillas para deportes y actividades físicas', true, NOW(), '#FF6B6B'),
('Casual', 'Zapatillas para uso diario y casual', true, NOW(), '#4ECDC4'),
('Running', 'Zapatillas especializadas para correr', true, NOW(), '#45B7D1'),
('Basketball', 'Zapatillas para baloncesto', true, NOW(), '#96CEB4'),
('Skateboarding', 'Zapatillas para skateboarding', true, NOW(), '#FFEAA7'),
('Lifestyle', 'Zapatillas de moda y estilo de vida', true, NOW(), '#DDA0DD');

-- Insertar marcas de ejemplo
INSERT INTO marcas (nombre, descripcion, pais_origen, fecha_fundacion, activo, fecha_creacion) VALUES
('Nike', 'Just Do It - Innovación en calzado deportivo', 'Estados Unidos', 1964, true, NOW()),
('Adidas', 'Impossible is Nothing - Tres rayas icónicas', 'Alemania', 1949, true, NOW()),
('Jordan', 'Air Jordan - Leyenda del baloncesto', 'Estados Unidos', 1984, true, NOW()),
('Puma', 'Forever Faster - Rendimiento y estilo', 'Alemania', 1948, true, NOW()),
('Converse', 'All Star - Clásico atemporal', 'Estados Unidos', 1908, true, NOW()),
('Vans', 'Off The Wall - Cultura del skate', 'Estados Unidos', 1966, true, NOW()),
('New Balance', 'Made in USA - Calidad y comodidad', 'Estados Unidos', 1906, true, NOW()),
('Reebok', 'Be More Human - Fitness y lifestyle', 'Reino Unido', 1958, true, NOW());

-- Insertar proveedores de ejemplo
INSERT INTO proveedores (nombre, ruc, email, telefono, direccion, ciudad, contacto_nombre, contacto_telefono, contacto_email, activo, fecha_registro, credito_dias, limite_credito) VALUES
('Distribuidora Nike Perú', '20123456789', 'ventas@nikeperu.com', '01-234-5678', 'Av. Javier Prado 1234', 'Lima', 'Carlos Mendoza', '987-654-321', 'carlos@nikeperu.com', true, NOW(), 30, 50000.00),
('Adidas Distribución', '20987654321', 'pedidos@adidasperu.com', '01-876-5432', 'Av. Arequipa 5678', 'Lima', 'Ana García', '987-123-456', 'ana@adidasperu.com', true, NOW(), 45, 75000.00),
('Jordan Sports Perú', '20111222333', 'info@jordansports.com', '01-111-2222', 'Av. Brasil 9999', 'Lima', 'Miguel Torres', '987-999-888', 'miguel@jordansports.com', true, NOW(), 30, 40000.00),
('Puma Importaciones', '20444555666', 'ventas@pumaimport.com', '01-444-5555', 'Av. Tacna 1111', 'Lima', 'Laura Rodríguez', '987-777-666', 'laura@pumaimport.com', true, NOW(), 30, 35000.00);

-- Insertar usuarios de ejemplo
INSERT INTO usuarios (nombre, email, password, activo, fecha_creacion, rol, telefono) VALUES
('Admin Sistema', 'admin@sneakershop.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', true, NOW(), 'Administrador', '999-888-777'),
('Vendedor Principal', 'vendedor@sneakershop.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', true, NOW(), 'Vendedor', '999-777-666'),
('Gerente Tienda', 'gerente@sneakershop.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', true, NOW(), 'Gerente', '999-666-555');

-- Insertar clientes de ejemplo
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, codigo_postal, tipo_documento, numero_documento, activo, fecha_registro, puntos_fidelidad) VALUES
('Juan', 'Pérez', 'juan.perez@email.com', '987-654-321', 'Av. Larco 123', 'Miraflores', '15074', 'DNI', '12345678', true, NOW(), 150),
('María', 'González', 'maria.gonzalez@email.com', '987-123-456', 'Av. Arequipa 456', 'San Isidro', '15036', 'DNI', '87654321', true, NOW(), 200),
('Carlos', 'López', 'carlos.lopez@email.com', '987-789-012', 'Av. Javier Prado 789', 'La Molina', '15024', 'DNI', '11223344', true, NOW(), 75),
('Ana', 'Martínez', 'ana.martinez@email.com', '987-456-789', 'Av. Brasil 321', 'Magdalena', '15076', 'DNI', '55667788', true, NOW(), 300),
('Luis', 'Rodríguez', 'luis.rodriguez@email.com', '987-321-654', 'Av. Tacna 654', 'Lima', '15001', 'DNI', '99887766', true, NOW(), 50);

-- Insertar productos de ejemplo
INSERT INTO productos (nombre, descripcion, codigo_producto, categoria_id, marca_id, precio_venta, precio_compra, activo, fecha_creacion, fecha_actualizacion, genero, edad_target, material_principal, tipo_suela, tecnologia, peso_gramos, garantia_meses, stock_minimo, es_destacado, es_nuevo, descuento_porcentaje) VALUES
('Air Max 270', 'Zapatilla deportiva con tecnología Air Max para máximo confort', 'NIKE-AM270-001', 1, 1, 450.00, 280.00, true, NOW(), NOW(), 'Unisex', 'Adulto', 'Mesh y cuero sintético', 'Goma', 'Air Max', 320, 12, 5, true, true, 0.00),
('Ultraboost 22', 'Zapatilla de running con tecnología Boost para energía de retorno', 'ADIDAS-UB22-001', 3, 2, 520.00, 320.00, true, NOW(), NOW(), 'Unisex', 'Adulto', 'Primeknit', 'Boost', 'Boost', 280, 12, 5, true, true, 10.00),
('Air Jordan 1', 'Zapatilla icónica de baloncesto con diseño clásico', 'JORDAN-AJ1-001', 4, 3, 650.00, 400.00, true, NOW(), NOW(), 'Unisex', 'Adulto', 'Cuero premium', 'Goma', 'Air', 350, 12, 3, true, false, 0.00),
('Classic Slip-On', 'Zapatilla casual clásica sin cordones', 'VANS-CSO-001', 2, 6, 180.00, 110.00, true, NOW(), NOW(), 'Unisex', 'Adulto', 'Canvas', 'Goma', 'Waffle', 250, 6, 10, false, false, 0.00),
('Chuck Taylor All Star', 'Zapatilla clásica de canvas con diseño atemporal', 'CONVERSE-CTA-001', 2, 5, 160.00, 95.00, true, NOW(), NOW(), 'Unisex', 'Adulto', 'Canvas', 'Goma', 'Ninguna', 200, 6, 15, false, false, 5.00);

-- Insertar presentaciones de ejemplo
INSERT INTO presentaciones (producto_id, talla, color, codigo_color, stock_disponible, activo, fecha_creacion, fecha_actualizacion, ubicacion_almacen) VALUES
(1, '38', 'Blanco/Negro', '#FFFFFF', 15, true, NOW(), NOW(), 'A1-B2'),
(1, '39', 'Blanco/Negro', '#FFFFFF', 12, true, NOW(), NOW(), 'A1-B3'),
(1, '40', 'Blanco/Negro', '#FFFFFF', 18, true, NOW(), NOW(), 'A1-B4'),
(1, '41', 'Blanco/Negro', '#FFFFFF', 10, true, NOW(), NOW(), 'A1-B5'),
(1, '42', 'Blanco/Negro', '#FFFFFF', 8, true, NOW(), NOW(), 'A1-B6'),
(1, '43', 'Blanco/Negro', '#FFFFFF', 6, true, NOW(), NOW(), 'A1-B7'),
(1, '44', 'Blanco/Negro', '#FFFFFF', 4, true, NOW(), NOW(), 'A1-B8'),
(1, '45', 'Blanco/Negro', '#FFFFFF', 2, true, NOW(), NOW(), 'A1-B9'),
(2, '38', 'Negro/Blanco', '#000000', 20, true, NOW(), NOW(), 'A2-B2'),
(2, '39', 'Negro/Blanco', '#000000', 16, true, NOW(), NOW(), 'A2-B3'),
(2, '40', 'Negro/Blanco', '#000000', 22, true, NOW(), NOW(), 'A2-B4'),
(2, '41', 'Negro/Blanco', '#000000', 14, true, NOW(), NOW(), 'A2-B5'),
(2, '42', 'Negro/Blanco', '#000000', 12, true, NOW(), NOW(), 'A2-B6'),
(2, '43', 'Negro/Blanco', '#000000', 10, true, NOW(), NOW(), 'A2-B7'),
(2, '44', 'Negro/Blanco', '#000000', 8, true, NOW(), NOW(), 'A2-B8'),
(2, '45', 'Negro/Blanco', '#000000', 6, true, NOW(), NOW(), 'A2-B9'),
(3, '38', 'Rojo/Blanco', '#FF0000', 8, true, NOW(), NOW(), 'A3-B2'),
(3, '39', 'Rojo/Blanco', '#FF0000', 6, true, NOW(), NOW(), 'A3-B3'),
(3, '40', 'Rojo/Blanco', '#FF0000', 10, true, NOW(), NOW(), 'A3-B4'),
(3, '41', 'Rojo/Blanco', '#FF0000', 8, true, NOW(), NOW(), 'A3-B5'),
(3, '42', 'Rojo/Blanco', '#FF0000', 6, true, NOW(), NOW(), 'A3-B6'),
(3, '43', 'Rojo/Blanco', '#FF0000', 4, true, NOW(), NOW(), 'A3-B7'),
(3, '44', 'Rojo/Blanco', '#FF0000', 3, true, NOW(), NOW(), 'A3-B8'),
(3, '45', 'Rojo/Blanco', '#FF0000', 2, true, NOW(), NOW(), 'A3-B9');

-- Insertar inventario de ejemplo
INSERT INTO inventario (producto_id, presentacion_id, stock_actual, stock_minimo, stock_maximo, ubicacion_almacen, activo, fecha_creacion, fecha_actualizacion) VALUES
(1, 1, 15, 5, 50, 'A1-B2', true, NOW(), NOW()),
(1, 2, 12, 5, 50, 'A1-B3', true, NOW(), NOW()),
(1, 3, 18, 5, 50, 'A1-B4', true, NOW(), NOW()),
(1, 4, 10, 5, 50, 'A1-B5', true, NOW(), NOW()),
(1, 5, 8, 5, 50, 'A1-B6', true, NOW(), NOW()),
(1, 6, 6, 5, 50, 'A1-B7', true, NOW(), NOW()),
(1, 7, 4, 5, 50, 'A1-B8', true, NOW(), NOW()),
(1, 8, 2, 5, 50, 'A1-B9', true, NOW(), NOW()),
(2, 9, 20, 5, 50, 'A2-B2', true, NOW(), NOW()),
(2, 10, 16, 5, 50, 'A2-B3', true, NOW(), NOW()),
(2, 11, 22, 5, 50, 'A2-B4', true, NOW(), NOW()),
(2, 12, 14, 5, 50, 'A2-B5', true, NOW(), NOW()),
(2, 13, 12, 5, 50, 'A2-B6', true, NOW(), NOW()),
(2, 14, 10, 5, 50, 'A2-B7', true, NOW(), NOW()),
(2, 15, 8, 5, 50, 'A2-B8', true, NOW(), NOW()),
(2, 16, 6, 5, 50, 'A2-B9', true, NOW(), NOW()),
(3, 17, 8, 3, 30, 'A3-B2', true, NOW(), NOW()),
(3, 18, 6, 3, 30, 'A3-B3', true, NOW(), NOW()),
(3, 19, 10, 3, 30, 'A3-B4', true, NOW(), NOW()),
(3, 20, 8, 3, 30, 'A3-B5', true, NOW(), NOW()),
(3, 21, 6, 3, 30, 'A3-B6', true, NOW(), NOW()),
(3, 22, 4, 3, 30, 'A3-B7', true, NOW(), NOW()),
(3, 23, 3, 3, 30, 'A3-B8', true, NOW(), NOW()),
(3, 24, 2, 3, 30, 'A3-B9', true, NOW(), NOW());

-- Mostrar resumen de datos insertados
SELECT 'Categorías insertadas:' as Resumen, COUNT(*) as Cantidad FROM categorias
UNION ALL
SELECT 'Marcas insertadas:', COUNT(*) FROM marcas
UNION ALL
SELECT 'Proveedores insertados:', COUNT(*) FROM proveedores
UNION ALL
SELECT 'Usuarios insertados:', COUNT(*) FROM usuarios
UNION ALL
SELECT 'Clientes insertados:', COUNT(*) FROM clientes
UNION ALL
SELECT 'Productos insertados:', COUNT(*) FROM productos
UNION ALL
SELECT 'Presentaciones insertadas:', COUNT(*) FROM presentaciones
UNION ALL
SELECT 'Inventario insertado:', COUNT(*) FROM inventario;
