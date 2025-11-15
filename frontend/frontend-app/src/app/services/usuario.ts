import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Usuario } from '../models/usuario';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los usuarios
   */
  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<{ success: boolean; data: Usuario[]; total: number }>(this.apiUrl).pipe(
      map(response => response.data || [])
    );
  }

  /**
   * Obtiene un usuario por ID
   */
  obtenerUsuarioPorId(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crea un nuevo usuario
   */
  crearUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }

  /**
   * Verifica si un email está disponible
   */
  verificarEmail(email: string): Observable<{ disponible: boolean }> {
    return this.http.get<{ disponible: boolean }>(`${this.apiUrl}/check-email?email=${email}`);
  }

  /**
   * Actualiza un usuario existente
   */
  actualizarUsuario(id: number, usuario: Usuario): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, usuario);
  }

  /**
   * Elimina un usuario
   */
  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtiene usuarios activos
   */
  obtenerUsuariosActivos(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/activos`);
  }
}
