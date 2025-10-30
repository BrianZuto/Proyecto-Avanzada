import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface AdminProduct {
  id: number | string;
  nombre: string;
  marca: string;
  precio: number;
  stock: number;
  ventas: number;
  estado: 'Activo' | 'Agotado';
}

@Component({
  selector: 'app-product-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product.html',
  styleUrl: './product.css'
})
export class ProductItemComponent {
  @Input() product!: AdminProduct;
}


