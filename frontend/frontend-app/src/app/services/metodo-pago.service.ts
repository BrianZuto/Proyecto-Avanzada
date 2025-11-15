import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MetodoPago } from '../models/metodo-pago';

@Injectable({
  providedIn: 'root'
})
export class MetodoPagoService {
  private apiUrl = `${environment.apiUrl}/metodos-pago`;

  constructor(private http: HttpClient) { }

  obtenerMetodosPagoPorUsuario(usuarioId: number): Observable<{ success: boolean; metodosPago: MetodoPago[] }> {
    return this.http.get<{ success: boolean; metodosPago: MetodoPago[] }>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  obtenerMetodoPagoPorId(id: number): Observable<{ success: boolean; metodoPago: MetodoPago }> {
    return this.http.get<{ success: boolean; metodoPago: MetodoPago }>(`${this.apiUrl}/${id}`);
  }

  crearMetodoPago(metodoPago: MetodoPago): Observable<{ success: boolean; message: string; metodoPago: MetodoPago }> {
    return this.http.post<{ success: boolean; message: string; metodoPago: MetodoPago }>(this.apiUrl, metodoPago);
  }

  actualizarMetodoPago(id: number, metodoPago: MetodoPago): Observable<{ success: boolean; message: string; metodoPago: MetodoPago }> {
    return this.http.put<{ success: boolean; message: string; metodoPago: MetodoPago }>(`${this.apiUrl}/${id}`, metodoPago);
  }

  eliminarMetodoPago(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiUrl}/${id}`);
  }

  establecerComoPrincipal(id: number, usuarioId: number): Observable<{ success: boolean; message: string; metodoPago: MetodoPago }> {
    return this.http.put<{ success: boolean; message: string; metodoPago: MetodoPago }>(`${this.apiUrl}/${id}/principal?usuarioId=${usuarioId}`, {});
  }
}

