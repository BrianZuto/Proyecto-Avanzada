export interface Producto {
  id?: number;
  nombre: string;
  descripcion?: string;
  codigoProducto?: string;
  categoriaId: number;
  marcaId: number;
  precioVenta: number;
  precioCompra?: number;
  imagenPrincipal?: string;
  imagenesAdicionales?: string;
  genero?: string;
  edadTarget?: string;
  materialPrincipal?: string;
  tipoSuela?: string;
  tecnologia?: string;
  pesoGramos?: number;
  garantiaMeses?: number;
  stockMinimo?: number;
  esDestacado?: boolean;
  esNuevo?: boolean;
  descuentoPorcentaje?: number;
  activo?: boolean;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  // Campos calculados para la vista
  stock?: number;
  ventas?: number;
  estado?: 'Activo' | 'Agotado';
  marca?: { id: number; nombre: string };
  categoria?: { id: number; nombre: string };
}

