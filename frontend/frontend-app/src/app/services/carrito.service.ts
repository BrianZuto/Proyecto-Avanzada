import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CarritoItem } from '../models/carrito-item';
import { Producto } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private carritoItems: CarritoItem[] = [];
  private carritoSubject = new BehaviorSubject<CarritoItem[]>([]);
  public carrito$: Observable<CarritoItem[]> = this.carritoSubject.asObservable();

  constructor() {
    // Cargar carrito desde localStorage si existe
    this.cargarCarritoDesdeStorage();
  }

  private cargarCarritoDesdeStorage(): void {
    const carritoGuardado = localStorage.getItem('carrito');
    if (carritoGuardado) {
      try {
        this.carritoItems = JSON.parse(carritoGuardado);
        this.actualizarSubject();
      } catch (e) {
        console.error('Error al cargar carrito desde localStorage:', e);
        this.carritoItems = [];
      }
    }
  }

  private guardarCarritoEnStorage(): void {
    localStorage.setItem('carrito', JSON.stringify(this.carritoItems));
  }

  private actualizarSubject(): void {
    this.carritoSubject.next([...this.carritoItems]);
  }

  agregarProducto(producto: Producto, cantidad: number = 1): void {
    const precioUnitario = this.obtenerPrecioConDescuento(producto);
    const itemExistente = this.carritoItems.find(
      item => item.producto.id === producto.id
    );

    if (itemExistente) {
      itemExistente.cantidad += cantidad;
      itemExistente.subtotal = itemExistente.cantidad * itemExistente.precioUnitario;
    } else {
      const nuevoItem: CarritoItem = {
        producto,
        cantidad,
        precioUnitario,
        subtotal: cantidad * precioUnitario
      };
      this.carritoItems.push(nuevoItem);
    }

    this.guardarCarritoEnStorage();
    this.actualizarSubject();
  }

  eliminarProducto(productoId: number | undefined): void {
    if (productoId) {
      this.carritoItems = this.carritoItems.filter(
        item => item.producto.id !== productoId
      );
      this.guardarCarritoEnStorage();
      this.actualizarSubject();
    }
  }

  actualizarCantidad(productoId: number | undefined, cantidad: number): void {
    if (productoId && cantidad > 0) {
      const item = this.carritoItems.find(
        item => item.producto.id === productoId
      );
      if (item) {
        item.cantidad = cantidad;
        item.subtotal = item.cantidad * item.precioUnitario;
        this.guardarCarritoEnStorage();
        this.actualizarSubject();
      }
    }
  }

  obtenerItems(): CarritoItem[] {
    return [...this.carritoItems];
  }

  obtenerTotal(): number {
    return this.carritoItems.reduce((total, item) => total + item.subtotal, 0);
  }

  obtenerCantidadTotal(): number {
    return this.carritoItems.reduce((total, item) => total + item.cantidad, 0);
  }

  limpiarCarrito(): void {
    this.carritoItems = [];
    localStorage.removeItem('carrito');
    this.actualizarSubject();
  }

  private obtenerPrecioConDescuento(producto: Producto): number {
    if (producto.descuentoPorcentaje && producto.descuentoPorcentaje > 0) {
      return producto.precioVenta * (1 - producto.descuentoPorcentaje / 100);
    }
    return producto.precioVenta;
  }
}

