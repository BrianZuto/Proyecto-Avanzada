import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent {
  title = 'SneakerZone';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // Datos de productos (en una aplicación real vendrían de un servicio)
  productos = [
    {
      id: 1,
      marca: 'Nike',
      modelo: 'Air Jordan 1 Retro High OG',
      precio: 145000,
      precioOriginal: 180000,
      descuento: 19,
      rating: 4.8,
      reviews: 324,
      imagen: 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=200&fit=crop',
      tallas: [7, 7.5, 8, 8.5, 9, 9.5],
      colores: ['black', 'red'],
      badge: 'discount'
    },
    {
      id: 2,
      marca: 'Adidas',
      modelo: 'Adidas Ultraboost 22',
      precio: 120000,
      rating: 4.6,
      reviews: 189,
      imagen: 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=300&h=200&fit=crop',
      tallas: [6, 7, 8, 9, 10, 11],
      colores: ['black', 'blue'],
      badge: 'new'
    },
    {
      id: 3,
      marca: 'Nike',
      modelo: 'Nike Air Max 270',
      precio: 95000,
      precioOriginal: 110000,
      descuento: 14,
      rating: 4.5,
      reviews: 156,
      imagen: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=300&h=200&fit=crop',
      tallas: [7, 8, 9, 10, 11],
      colores: ['teal', 'orange'],
      badge: 'discount'
    },
    {
      id: 4,
      marca: 'Puma',
      modelo: 'Puma RS-X³ Puzzle',
      precio: 85000,
      rating: 4.3,
      reviews: 98,
      imagen: 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=300&h=200&fit=crop',
      tallas: [7, 8, 9, 10],
      colores: ['orange', 'purple', 'red'],
      badge: 'new'
    }
  ];

  categoriaActiva = 'Todos';

  cambiarCategoria(categoria: string): void {
    this.categoriaActiva = categoria;
    console.log('Categoría seleccionada:', categoria);
  }

  seleccionarTalla(productoId: number, talla: number): void {
    console.log(`Talla ${talla} seleccionada para producto ${productoId}`);
  }

  agregarAlCarrito(productoId: number): void {
    console.log(`Producto ${productoId} agregado al carrito`);
  }

  explorarColeccion(): void {
    console.log('Explorar colección');
  }

  verOfertas(): void {
    console.log('Ver ofertas');
  }

}
