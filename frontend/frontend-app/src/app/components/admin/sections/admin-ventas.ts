import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface TopProduct { posicion: number; nombre: string; marca: string; ventas: number; }
interface OrderRow { id: number; cliente: string; producto: string; monto: string; estado: 'Completado' | 'Pendiente' | 'Enviado'; fecha: string; }

@Component({
  selector: 'app-admin-ventas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-ventas.html',
  styleUrls: ['./admin-ventas.css']
})
export class AdminVentasComponent {
  topProducts: TopProduct[] = [
    { posicion: 1, nombre: 'Air Jordan 1 Retro High', marca: 'Nike', ventas: 145 },
    { posicion: 2, nombre: 'Air Max 90', marca: 'Nike', ventas: 98 },
    { posicion: 3, nombre: 'Ultraboost 22', marca: 'Adidas', ventas: 87 },
    { posicion: 4, nombre: 'Old Skool', marca: 'Vans', ventas: 234 },
    { posicion: 5, nombre: 'Chuck Taylor All Star', marca: 'Converse', ventas: 156 }
  ];

  orders: OrderRow[] = [
    { id: 12345, cliente: 'Juan Pérez', producto: 'Air Jordan 1', monto: '$180.000', estado: 'Completado', fecha: '2024-01-15' },
    { id: 12346, cliente: 'María García', producto: 'Nike Air Max', monto: '$150.000', estado: 'Pendiente', fecha: '2024-01-15' },
    { id: 12347, cliente: 'Carlos López', producto: 'Adidas Ultraboost', monto: '$200.000', estado: 'Enviado', fecha: '2024-01-14' },
    { id: 12348, cliente: 'Ana Rodríguez', producto: 'Puma RS-X', monto: '$120.000', estado: 'Completado', fecha: '2024-01-14' }
  ];
}


