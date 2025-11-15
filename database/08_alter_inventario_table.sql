-- Script para modificar la tabla inventario
-- CAMBIO CRÍTICO: Hacer que presentacion_id sea nullable
-- Esto permite crear inventario para productos sin presentación específica

USE sneakershop;

-- Hacer que presentacion_id sea nullable (permite inventario sin presentación específica)
-- Esto es CRÍTICO para que funcione el sistema de inventario
ALTER TABLE inventario 
MODIFY COLUMN presentacion_id BIGINT NULL;
