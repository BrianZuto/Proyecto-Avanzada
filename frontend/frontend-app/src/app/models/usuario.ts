export interface Usuario {
  id?: number;
  nombre: string;
  email: string;
  password?: string;
  fechaCreacion?: string;
  activo?: boolean;
}
