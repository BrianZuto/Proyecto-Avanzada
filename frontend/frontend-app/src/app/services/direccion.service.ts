import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Direccion } from '../models/direccion';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DireccionService {
  private apiUrl = `${environment.apiUrl}/direcciones`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todas las direcciones de un usuario
   */
  obtenerDireccionesPorUsuario(usuarioId: number): Observable<{ success: boolean; direcciones: Direccion[] }> {
    return this.http.get<{ success: boolean; direcciones: Direccion[] }>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  /**
   * Obtiene una dirección por ID
   */
  obtenerDireccionPorId(id: number): Observable<{ success: boolean; direccion: Direccion }> {
    return this.http.get<{ success: boolean; direccion: Direccion }>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crea una nueva dirección
   */
  crearDireccion(direccion: Direccion): Observable<{ success: boolean; message: string; direccion: Direccion }> {
    return this.http.post<{ success: boolean; message: string; direccion: Direccion }>(this.apiUrl, direccion);
  }

  /**
   * Actualiza una dirección existente
   */
  actualizarDireccion(id: number, direccion: Direccion): Observable<{ success: boolean; message: string; direccion: Direccion }> {
    return this.http.put<{ success: boolean; message: string; direccion: Direccion }>(`${this.apiUrl}/${id}`, direccion);
  }

  /**
   * Elimina una dirección
   */
  eliminarDireccion(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiUrl}/${id}`);
  }
}

