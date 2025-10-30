import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-kids',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './kids.html',
  styleUrl: './kids.css'
})
export class KidsComponent {
  title = 'Sneakers para Niños';

  productos = [
    {
      id: 301,
      marca: 'Nike',
      modelo: "Nike Air Max 90 'Colorful Kids'",
      precio: 72000,
      precioOriginal: 85000,
      descuento: 15,
      imagen: 'https://images.unsplash.com/photo-1519741497674-611481863552?w=300&h=200&fit=crop',
      tallas: ['11C','12C','13C','1Y','2Y','3Y']
    },
    {
      id: 302,
      marca: 'Adidas',
      modelo: "Adidas Superstar 'Stars & Stripes'",
      precio: 58000,
      imagen: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=200&fit=crop',
      tallas: ['10C','11C','12C','13C','1Y','2Y']
    }
  ];
}


