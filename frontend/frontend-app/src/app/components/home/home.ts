import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { ProductoService } from '../../services/producto.service';
import { CarritoService } from '../../services/carrito.service';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';
import { Producto } from '../../models/producto';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent implements OnInit {
  title = 'SneakerZone';
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];
  categoriaActiva = 'Todos';
  cargando = true;

  constructor(
    private authService: AuthService,
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
          // Filtrar solo productos activos
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
      // Filtrar por marca
      this.productosFiltrados = this.productos.filter(p => 
        p.marca?.nombre?.toLowerCase() === categoria.toLowerCase()
      );
    }
  }

  obtenerImagenProducto(producto: Producto): string {
    if (producto.imagenPrincipal) {
      return producto.imagenPrincipal;
    }
    // Imagen por defecto si no hay imagen
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

  seleccionarTalla(productoId: number | undefined, talla: number): void {
    if (productoId) {
      console.log(`Talla ${talla} seleccionada para producto ${productoId}`);
    }
  }

  agregarAlCarrito(producto: Producto): void {
    if (!this.isLoggedIn()) {
      (window as any).Swal.fire({
        title: 'Inicia sesión',
        text: 'Debes iniciar sesión para agregar productos al carrito',
        icon: 'info',
        confirmButtonText: 'Iniciar sesión'
      }).then(() => {
        this.router.navigate(['/login']);
      });
      return;
    }

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

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  explorarColeccion(): void {
    this.router.navigate(['/catalogo']);
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
