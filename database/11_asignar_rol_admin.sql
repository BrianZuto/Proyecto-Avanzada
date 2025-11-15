-- Script para asignar rol de administrador a un usuario existente
-- Uso: Cambia el email del usuario por el que quieras hacer administrador

USE sneakershop;

-- Opción 1: Asignar rol "Admin" (formato del frontend)
-- Cambia 'tu_email@ejemplo.com' por el email del usuario que quieres hacer administrador
UPDATE usuarios 
SET rol = 'Admin' 
WHERE email = 'tu_email@ejemplo.com';

-- Opción 2: Asignar rol "Administrador" (formato del backend)
-- El frontend ahora normaliza automáticamente "Administrador" a "Admin"
UPDATE usuarios 
SET rol = 'Administrador' 
WHERE email = 'tu_email@ejemplo.com';

-- Verificar que el cambio se aplicó correctamente
SELECT id, nombre, email, rol, activo 
FROM usuarios 
WHERE email = 'tu_email@ejemplo.com';

-- NOTA: Después de ejecutar este script, el usuario debe:
-- 1. Cerrar sesión en el frontend (si está logueado)
-- 2. Iniciar sesión nuevamente para que el nuevo rol se cargue
