import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductItemComponent, AdminProduct } from '../../product/product';

@Component({
  selector: 'app-admin-productos',
  standalone: true,
  imports: [CommonModule, ProductItemComponent],
  templateUrl: './admin-productos.html',
  styleUrl: './admin-productos.css'
})
export class AdminProductosComponent {
  productos: AdminProduct[] = [
    { id: 1, nombre: 'Air Jordan 1 Retro High', marca: 'Nike', precio: 180000, stock: 25, ventas: 145, estado: 'Activo' },
    { id: 2, nombre: 'Ultraboost 22', marca: 'Adidas', precio: 200000, stock: 15, ventas: 87, estado: 'Activo' }
  ];
}


