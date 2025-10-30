import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-women',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './women.html',
  styleUrl: './women.css'
})
export class WomenComponent {
  title = 'Sneakers para Mujeres';

  productos = [
    {
      id: 201,
      marca: 'Nike',
      modelo: "Nike Air Max 270 'Sunset'",
      precio: 92000,
      precioOriginal: 108000,
      descuento: 15,
      rating: 4.7,
      reviews: 189,
      imagen: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=200&fit=crop',
      tallas: [5.5,6,7,8,9],
      colores: ['black','pink']
    },
    {
      id: 202,
      marca: 'Adidas',
      modelo: "Adidas Ultraboost 22 'Rose Gold'",
      precio: 115000,
      rating: 4.9,
      reviews: 298,
      imagen: 'https://images.unsplash.com/photo-1519741497674-611481863552?w=300&h=200&fit=crop',
      tallas: [5,6,7,8,9],
      colores: ['white','gold'],
      badge: 'new'
    }
  ];
}


