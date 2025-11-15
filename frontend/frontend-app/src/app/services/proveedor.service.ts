import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Proveedor } from '../models/proveedor';

@Injectable({
  providedIn: 'root'
})
export class ProveedorService {
  private apiUrl = `${environment.apiUrl}/proveedores`;

  constructor(private http: HttpClient) { }

  obtenerTodosLosProveedores(): Observable<{ success: boolean; data: Proveedor[]; total: number }> {
    return this.http.get<{ success: boolean; data: Proveedor[]; total: number }>(this.apiUrl);
  }

  obtenerProveedoresActivos(): Observable<{ success: boolean; data: Proveedor[] }> {
    return this.http.get<{ success: boolean; data: Proveedor[] }>(`${this.apiUrl}/activos`);
  }

  obtenerProveedorPorId(id: number): Observable<{ success: boolean; data: Proveedor }> {
    return this.http.get<{ success: boolean; data: Proveedor }>(`${this.apiUrl}/${id}`);
  }

  crearProveedor(proveedor: Proveedor): Observable<{ success: boolean; message: string; data: Proveedor }> {
    return this.http.post<{ success: boolean; message: string; data: Proveedor }>(this.apiUrl, proveedor);
  }
}

