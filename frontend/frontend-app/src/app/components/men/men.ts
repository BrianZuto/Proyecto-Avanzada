import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-men',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './men.html',
  styleUrl: './men.css'
})
export class MenComponent {
  title = 'Sneakers para Hombres';

  // Lista demo de productos masculinos (mock). En real, vendría de un servicio/endpoint.
  productos = [
    {
      id: 101,
      marca: 'Nike',
      modelo: 'Nike Air Max 270 "Triple Black"',
      precio: 95000,
      precioOriginal: 110000,
      descuento: 14,
      rating: 4.6,
      reviews: 156,
      imagen: 'https://images.unsplash.com/photo-1523381294911-8d3cead13475?w=300&h=200&fit=crop',
      tallas: [7, 8, 9, 10, 11, 12],
      colores: ['black', 'white'],
      badge: 'discount'
    },
    {
      id: 102,
      marca: 'Adidas',
      modelo: 'Adidas Ultraboost 22 "Core Black"',
      precio: 120000,
      rating: 4.7,
      reviews: 289,
      imagen: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=200&fit=crop',
      tallas: [7, 8, 9, 10, 11, 12],
      colores: ['black', 'blue'],
      badge: 'new'
    }
  ];
}


