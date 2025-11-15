export interface Proveedor {
  id?: number;
  nombre: string;
  ruc: string;
  email?: string;
  telefono?: string;
  direccion?: string;
  ciudad?: string;
  contactoNombre?: string;
  contactoTelefono?: string;
  contactoEmail?: string;
  creditoDias?: number;
  limiteCredito?: number;
  activo?: boolean;
  notas?: string;
  fechaRegistro?: string;
}

