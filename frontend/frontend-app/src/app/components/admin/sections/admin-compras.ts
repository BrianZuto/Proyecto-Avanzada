import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface PurchaseRow {
  id: number;
  proveedor: string;
  producto: string;
  cantidad: string;
  precioTotal: string;
  fecha: string;
  estado: 'Recibido' | 'En tránsito' | 'Pendiente';
}

@Component({
  selector: 'app-admin-compras',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-compras.html',
  styleUrls: ['./admin-compras.css']
})
export class AdminComprasComponent {
  purchases: PurchaseRow[] = [
    { id: 1, proveedor: 'Nike Distribuidora', producto: 'Air Jordan 1 Retro', cantidad: '50 unidades', precioTotal: '$7.500.000', fecha: '2024-01-10', estado: 'Recibido' },
    { id: 2, proveedor: 'Adidas Colombia', producto: 'Ultraboost 22', cantidad: '30 unidades', precioTotal: '$5.400.000', fecha: '2024-01-12', estado: 'En tránsito' },
    { id: 3, proveedor: 'Vans Distribution', producto: 'Old Skool Classic', cantidad: '40 unidades', precioTotal: '$2.800.000', fecha: '2024-01-13', estado: 'Recibido' },
    { id: 4, proveedor: 'Puma Importaciones', producto: 'RS-X Collection', cantidad: '25 unidades', precioTotal: '$2.625.000', fecha: '2024-01-14', estado: 'Pendiente' },
    { id: 5, proveedor: 'Converse SA', producto: 'Chuck Taylor', cantidad: '60 unidades', precioTotal: '$3.600.000', fecha: '2024-01-15', estado: 'Recibido' }
  ];
}


