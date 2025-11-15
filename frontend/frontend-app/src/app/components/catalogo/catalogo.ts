import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { CarritoService } from '../../services/carrito.service';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';
import { Producto } from '../../models/producto';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css'
})
export class CatalogoComponent implements OnInit {
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];
  categoriaActiva = 'Todos';
  cargando = true;

  constructor(
    private router: Router,
    private productoService: ProductoService,
    private carritoService: CarritoService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.cargando = true;
    this.productoService.obtenerTodosLosProductos().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.productos = response.data.filter(p => p.activo !== false);
          this.productosFiltrados = [...this.productos];
          this.cargando = false;
        } else {
          console.error('Error al cargar productos:', response);
          this.cargando = false;
        }
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
        this.cargando = false;
      }
    });
  }

  cambiarCategoria(categoria: string): void {
    this.categoriaActiva = categoria;
    if (categoria === 'Todos') {
      this.productosFiltrados = [...this.productos];
    } else {
      this.productosFiltrados = this.productos.filter(p => 
        p.marca?.nombre?.toLowerCase() === categoria.toLowerCase()
      );
    }
  }

  obtenerImagenProducto(producto: Producto): string {
    if (producto.imagenPrincipal) {
      return producto.imagenPrincipal;
    }
    return 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=200&fit=crop';
  }

  obtenerBadge(producto: Producto): string | null {
    if (producto.esNuevo) {
      return 'new';
    }
    if (producto.descuentoPorcentaje && producto.descuentoPorcentaje > 0) {
      return 'discount';
    }
    return null;
  }

  obtenerPrecioConDescuento(producto: Producto): number {
    if (producto.descuentoPorcentaje && producto.descuentoPorcentaje > 0) {
      return producto.precioVenta * (1 - producto.descuentoPorcentaje / 100);
    }
    return producto.precioVenta;
  }

  agregarAlCarrito(producto: Producto): void {
    if (producto.stock && producto.stock > 0) {
      this.carritoService.agregarProducto(producto, 1);
      (window as any).Swal.fire({
        title: '¡Agregado!',
        text: `${producto.nombre} agregado al carrito`,
        icon: 'success',
        timer: 2000,
        showConfirmButton: false
      });
    } else {
      (window as any).Swal.fire({
        title: 'Producto agotado',
        text: 'Este producto no está disponible en este momento',
        icon: 'warning',
        confirmButtonText: 'Aceptar'
      });
    }
  }

  obtenerMarcasUnicas(): string[] {
    const marcas = new Set<string>();
    this.productos.forEach(p => {
      if (p.marca?.nombre) {
        marcas.add(p.marca.nombre);
      }
    });
    return Array.from(marcas).sort();
  }
}

