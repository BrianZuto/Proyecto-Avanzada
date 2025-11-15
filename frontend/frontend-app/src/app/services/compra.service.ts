import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Compra, DetalleCompra } from '../models/compra';

@Injectable({
  providedIn: 'root'
})
export class CompraService {
  private apiUrl = `${environment.apiUrl}/compras`;

  constructor(private http: HttpClient) { }

  obtenerTodasLasCompras(): Observable<{ success: boolean; data: Compra[]; total: number }> {
    return this.http.get<{ success: boolean; data: Compra[]; total: number }>(this.apiUrl);
  }

  obtenerComprasActivas(): Observable<{ success: boolean; data: Compra[]; total: number }> {
    return this.http.get<{ success: boolean; data: Compra[]; total: number }>(`${this.apiUrl}/activas`);
  }

  obtenerCompraPorId(id: number): Observable<{ success: boolean; data: Compra }> {
    return this.http.get<{ success: boolean; data: Compra }>(`${this.apiUrl}/${id}`);
  }

  crearCompra(compra: Compra): Observable<{ success: boolean; message: string; data: Compra }> {
    return this.http.post<{ success: boolean; message: string; data: Compra }>(this.apiUrl, compra);
  }

  actualizarCompra(id: number, compra: Compra): Observable<{ success: boolean; message: string; data: Compra }> {
    return this.http.put<{ success: boolean; message: string; data: Compra }>(`${this.apiUrl}/${id}`, compra);
  }

  eliminarCompra(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiUrl}/${id}`);
  }

  agregarDetalleCompra(compraId: number, detalle: DetalleCompra): Observable<{ success: boolean; message: string; data: DetalleCompra }> {
    return this.http.post<{ success: boolean; message: string; data: DetalleCompra }>(`${this.apiUrl}/${compraId}/detalles`, detalle);
  }

  obtenerDetallesCompra(compraId: number): Observable<{ success: boolean; data: DetalleCompra[]; total: number }> {
    return this.http.get<{ success: boolean; data: DetalleCompra[]; total: number }>(`${this.apiUrl}/${compraId}/detalles`);
  }

  eliminarDetalleCompra(detalleId: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiUrl}/detalles/${detalleId}`);
  }

  marcarComoPagada(id: number): Observable<{ success: boolean; message: string; data: Compra }> {
    return this.http.put<{ success: boolean; message: string; data: Compra }>(`${this.apiUrl}/${id}/pagar`, {});
  }

  marcarComoCancelada(id: number): Observable<{ success: boolean; message: string; data: Compra }> {
    return this.http.put<{ success: boolean; message: string; data: Compra }>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}

