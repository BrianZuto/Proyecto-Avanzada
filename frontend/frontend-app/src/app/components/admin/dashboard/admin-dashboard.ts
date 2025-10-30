import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboardComponent {
  metrics = { ventas: 145280, ordenes: 1234, productos: 89, usuarios: 456 };
  ordenes = [
    { id: '#12345', cliente: 'Juan Pérez', producto: 'Air Jordan 1', monto: 180000, estado: 'Completado', fecha: '2024-01-15' },
    { id: '#12346', cliente: 'María García', producto: 'Nike Air Max', monto: 150000, estado: 'Pendiente', fecha: '2024-01-15' }
  ];
}


