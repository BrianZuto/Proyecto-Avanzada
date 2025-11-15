export interface MetodoPago {
  id?: number;
  usuarioId: number;
  tipoTarjeta: string;
  numeroTarjeta: string;
  nombreTitular: string;
  fechaExpiracion: string;
  principal?: boolean;
  activo?: boolean;
  fechaCreacion?: string;
}

