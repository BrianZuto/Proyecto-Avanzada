import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface UserRow { id: number; nombre: string; email: string; rol: string; ultimoLogin: string; estado: 'Activo' | 'Inactivo'; }

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-usuarios.html',
  styleUrls: ['./admin-usuarios.css']
})
export class AdminUsuariosComponent {
  users: UserRow[] = [
    { id: 1, nombre: 'Ana Martínez', email: 'ana@tienda.com', rol: 'Administrador', ultimoLogin: '2024-01-15', estado: 'Activo' },
    { id: 2, nombre: 'Carlos Ruiz', email: 'carlos@tienda.com', rol: 'Vendedor', ultimoLogin: '2024-01-15', estado: 'Activo' },
    { id: 3, nombre: 'Laura Torres', email: 'laura@tienda.com', rol: 'Vendedor', ultimoLogin: '2024-01-14', estado: 'Activo' },
    { id: 4, nombre: 'Miguel Santos', email: 'miguel@tienda.com', rol: 'Vendedor', ultimoLogin: '2024-01-12', estado: 'Inactivo' }
  ];
}


