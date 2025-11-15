import { Producto } from './producto';

export interface DetalleVenta {
  id?: number;
  ventaId?: number;
  productoId: number;
  producto?: Producto;
  presentacionId?: number;
  cantidad: number;
  precioUnitario: number;
  descuento?: number;
  subtotal?: number;
  observaciones?: string;
}

export interface Venta {
  id?: number;
  numeroVenta?: string;
  clienteId?: number;
  usuarioId: number;
  fechaVenta?: string;
  subtotal?: number;
  descuento?: number;
  impuesto?: number;
  total?: number;
  estado?: string; // COMPLETADA, CANCELADA, DEVUELTA
  metodoPago?: string;
  numeroComprobante?: string;
  observaciones?: string;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  activo?: boolean;
  puntosOtorgados?: number;
  puntosUsados?: number;
  descuentoPuntos?: number;
  detallesVenta?: DetalleVenta[];
}

