export interface Compra {
  id?: number;
  numeroFactura?: string;
  proveedorId: number;
  usuarioId: number;
  fechaCompra?: string;
  fechaVencimiento?: string;
  subtotal?: number;
  descuento?: number;
  impuesto?: number;
  total?: number;
  estado?: string; // PENDIENTE, PAGADA, CANCELADA
  metodoPago?: string;
  numeroComprobante?: string;
  observaciones?: string;
  activo?: boolean;
  detallesCompra?: DetalleCompra[];
  // Para mostrar en el frontend
  proveedor?: { id: number; nombre: string };
  usuario?: { id: number; nombre: string };
}

export interface DetalleCompra {
  id?: number;
  compraId?: number;
  productoId: number;
  presentacionId?: number;
  cantidad: number;
  precioUnitario: number;
  descuento?: number;
  subtotal?: number;
  observaciones?: string;
  // Para mostrar en el frontend
  producto?: { id: number; nombre: string; precioVenta: number };
  presentacion?: { id: number; talla: string; color: string };
}

