import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-offers',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './offers.html',
  styleUrl: './offers.css'
})
export class OffersComponent {
  title = 'Ofertas Especiales';

  stats = {
    count: 8,
    savings: 263000,
    maxDiscount: 70
  };

  productos = [
    { id: 1, marca: 'Nike', modelo: "Air Jordan 1 Retro High OG 'Bred'", precio: 145000, precioOriginal: 230000, descuento: 34, imagen: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=200&fit=crop', tallas: [7,7.5,8,8.5,9,9.5] },
    { id: 2, marca: 'Puma', modelo: "RS-X³ 'Neon Pack'", precio: 65000, precioOriginal: 95000, descuento: 32, imagen: 'https://images.unsplash.com/photo-1519741497674-611481863552?w=300&h=200&fit=crop', tallas: [7,8,9,10] },
    { id: 3, marca: 'Adidas', modelo: "Ultraboost 22 'Triple White'", precio: 98000, precioOriginal: 140000, descuento: 30, imagen: 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=300&h=200&fit=crop', tallas: [7,8,9,10,11] },
    { id: 4, marca: 'Puma', modelo: "Suede Classic 'Vintage Pack'", precio: 48000, precioOriginal: 60000, descuento: 20, imagen: 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=300&h=200&fit=crop', tallas: [7,8,9,10,11] }
  ];
}


