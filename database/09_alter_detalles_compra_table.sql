-- Script para modificar la tabla detalles_compra
-- CAMBIO CRÍTICO: Hacer que presentacion_id sea nullable
-- Esto permite crear detalles de compra para productos sin presentación específica

USE sneakershop;

-- Hacer que presentacion_id sea nullable (permite detalles de compra sin presentación específica)
-- Esto es CRÍTICO para que funcione el sistema de compras
ALTER TABLE detalles_compra 
MODIFY COLUMN presentacion_id BIGINT NULL;

