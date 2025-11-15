import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Producto } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private apiUrl = `${environment.apiUrl}/productos`;

  constructor(private http: HttpClient) { }

  obtenerTodosLosProductos(): Observable<{ success: boolean; data: Producto[]; total: number }> {
    return this.http.get<{ success: boolean; data: Producto[]; total: number }>(this.apiUrl);
  }

  obtenerProductosActivos(): Observable<{ success: boolean; data: Producto[]; total: number }> {
    return this.http.get<{ success: boolean; data: Producto[]; total: number }>(`${this.apiUrl}/activos`);
  }

  obtenerProductoPorId(id: number): Observable<{ success: boolean; data: Producto }> {
    return this.http.get<{ success: boolean; data: Producto }>(`${this.apiUrl}/${id}`);
  }

  crearProducto(producto: Producto): Observable<{ success: boolean; message: string; data: Producto }> {
    return this.http.post<{ success: boolean; message: string; data: Producto }>(this.apiUrl, producto);
  }

  actualizarProducto(id: number, producto: Producto): Observable<{ success: boolean; message: string; data: Producto }> {
    return this.http.put<{ success: boolean; message: string; data: Producto }>(`${this.apiUrl}/${id}`, producto);
  }

  eliminarProducto(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiUrl}/${id}`);
  }
}

