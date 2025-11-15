import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Venta, DetalleVenta } from '../models/venta';

@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private apiUrl = `${environment.apiUrl}/ventas`;

  constructor(private http: HttpClient) { }

  obtenerTodasLasVentas(): Observable<{ success: boolean; data: Venta[]; total: number }> {
    return this.http.get<{ success: boolean; data: Venta[]; total: number }>(this.apiUrl);
  }

  obtenerVentasActivas(): Observable<{ success: boolean; data: Venta[]; total: number }> {
    return this.http.get<{ success: boolean; data: Venta[]; total: number }>(`${this.apiUrl}/activas`);
  }

  obtenerVentasPorUsuario(usuarioId: number): Observable<{ success: boolean; data: Venta[]; total: number }> {
    return this.http.get<{ success: boolean; data: Venta[]; total: number }>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  obtenerVentaPorId(id: number): Observable<{ success: boolean; data: Venta }> {
    return this.http.get<{ success: boolean; data: Venta }>(`${this.apiUrl}/${id}`);
  }

  crearVenta(venta: Venta): Observable<{ success: boolean; message: string; data: Venta }> {
    return this.http.post<{ success: boolean; message: string; data: Venta }>(this.apiUrl, venta);
  }

  actualizarVenta(id: number, venta: Venta): Observable<{ success: boolean; message: string; data: Venta }> {
    return this.http.put<{ success: boolean; message: string; data: Venta }>(`${this.apiUrl}/${id}`, venta);
  }

  eliminarVenta(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiUrl}/${id}`);
  }

  obtenerDetallesVenta(ventaId: number): Observable<{ success: boolean; data: DetalleVenta[]; total: number }> {
    return this.http.get<{ success: boolean; data: DetalleVenta[]; total: number }>(`${this.apiUrl}/${ventaId}/detalles`);
  }
}

