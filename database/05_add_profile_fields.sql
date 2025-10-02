-- Script para agregar campos de perfil a la tabla usuarios
-- Ejecutar este script DESPUÉS de que el backend haya iniciado al menos una vez
-- y las tablas se hayan creado automáticamente.

USE proyecto_avanzada;

-- Agregar columna fecha_nacimiento si no existe
ALTER TABLE usuarios 
ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE NULL;

-- Agregar columna telefono si no existe
ALTER TABLE usuarios 
ADD COLUMN IF NOT EXISTS telefono VARCHAR(20) NULL;

-- Verificar que las columnas se agregaron correctamente
DESCRIBE usuarios;

-- Mostrar algunos registros para verificar
SELECT id, nombre, email, telefono, fecha_nacimiento, rol, activo 
FROM usuarios 
LIMIT 5;
