-- Script para agregar campos de perfil a la tabla usuarios
-- Ejecutar este script DESPUÉS de que el backend haya iniciado al menos una vez
-- y las tablas se hayan creado automáticamente.

USE sneakershop;

-- Agregar columna fecha_nacimiento si no existe
SET @dbname = DATABASE();
SET @tablename = "usuarios";
SET @columnname = "fecha_nacimiento";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 1",
  CONCAT("ALTER TABLE ", @tablename, " ADD COLUMN ", @columnname, " DATE NULL")
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Agregar columna telefono si no existe
SET @columnname = "telefono";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 1",
  CONCAT("ALTER TABLE ", @tablename, " ADD COLUMN ", @columnname, " VARCHAR(20) NULL")
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Verificar que las columnas se agregaron correctamente
DESCRIBE usuarios;

-- Mostrar algunos registros para verificar
SELECT id, nombre, email, telefono, fecha_nacimiento, rol, activo 
FROM usuarios 
LIMIT 5;
