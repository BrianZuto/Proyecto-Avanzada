export interface Usuario {
  id?: number;
  nombre: string;
  email: string;
  password?: string;
  fechaCreacion?: string;
  activo?: boolean;
  rol?: string; // Admin, Empleado, Usuario
  fechaNacimiento?: string;
  telefono?: string;
}
