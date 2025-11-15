export interface Direccion {
  id?: number;
  usuarioId: number;
  direccion: string;
  ciudad: string;
  departamento: string;
  codigoPostal?: string;
  pais: string;
  fechaCreacion?: string;
  activo?: boolean;
}

