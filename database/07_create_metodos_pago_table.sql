USE sneakershop;

CREATE TABLE IF NOT EXISTS metodos_pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_tarjeta VARCHAR(50) NOT NULL,
    numero_tarjeta VARCHAR(19) NOT NULL,
    nombre_titular VARCHAR(100) NOT NULL,
    fecha_expiracion VARCHAR(7) NOT NULL,
    principal BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

