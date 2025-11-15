import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VentaService } from '../../../services/venta.service';
import { ProductoService } from '../../../services/producto.service';
import { UsuarioService } from '../../../services/usuario';
import { CompraService } from '../../../services/compra.service';
import { Venta } from '../../../models/venta';
import { Producto } from '../../../models/producto';
import { Usuario } from '../../../models/usuario';
import { Compra } from '../../../models/compra';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboardComponent implements OnInit {
  metrics = { 
    ventas: 0, 
    ordenes: 0, 
    productos: 0, 
    usuarios: 0 
  };
  
  ordenes: Array<{
    id: string;
    cliente: string;
    producto: string;
    monto: number;
    estado: string;
    fecha: string;
  }> = [];
  
  cargando = true;

  constructor(
    private ventaService: VentaService,
    private productoService: ProductoService,
    private usuarioService: UsuarioService,
    private compraService: CompraService
  ) {}

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.cargando = true;
    
    // Cargar todas las estadísticas en paralelo
    Promise.all([
      this.cargarVentas(),
      this.cargarProductos(),
      this.cargarUsuarios(),
      this.cargarCompras()
    ]).finally(() => {
      this.cargando = false;
    });
  }

  cargarVentas(): Promise<void> {
    return new Promise((resolve) => {
      this.ventaService.obtenerVentasActivas().subscribe({
        next: (response) => {
          if (response.success && response.data) {
            const ventas = response.data.filter(v => v.activo !== false);
            this.metrics.ordenes = ventas.length;
            
            // Calcular total de ventas
            this.metrics.ventas = ventas.reduce((total, venta) => {
              return total + (venta.total || 0);
            }, 0);
            
            // Obtener órdenes recientes (últimas 5)
            const ventasOrdenadas = [...ventas].sort((a, b) => {
              const fechaA = a.fechaVenta || a.fechaCreacion || '';
              const fechaB = b.fechaVenta || b.fechaCreacion || '';
              return fechaB.localeCompare(fechaA);
            }).slice(0, 5);
            
            this.ordenes = ventasOrdenadas.map(venta => ({
              id: `#${venta.numeroVenta || venta.id}`,
              cliente: this.obtenerNombreCliente(venta),
              producto: this.obtenerProductosVenta(venta),
              monto: venta.total || 0,
              estado: venta.estado || 'PENDIENTE',
              fecha: this.formatearFecha(venta.fechaVenta || venta.fechaCreacion)
            }));
          }
          resolve();
        },
        error: (error) => {
          console.error('Error al cargar ventas:', error);
          resolve();
        }
      });
    });
  }

  cargarProductos(): Promise<void> {
    return new Promise((resolve) => {
      this.productoService.obtenerProductosActivos().subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.metrics.productos = response.data.filter(p => p.activo !== false).length;
          }
          resolve();
        },
        error: (error) => {
          console.error('Error al cargar productos:', error);
          resolve();
        }
      });
    });
  }

  cargarUsuarios(): Promise<void> {
    return new Promise((resolve) => {
      this.usuarioService.obtenerUsuarios().subscribe({
        next: (usuarios) => {
          this.metrics.usuarios = usuarios.filter(u => u.activo !== false).length;
          resolve();
        },
        error: (error) => {
          console.error('Error al cargar usuarios:', error);
          resolve();
        }
      });
    });
  }

  cargarCompras(): Promise<void> {
    return new Promise((resolve) => {
      this.compraService.obtenerComprasActivas().subscribe({
        next: (response) => {
          // Las compras ya están cargadas, solo necesitamos resolver
          resolve();
        },
        error: (error) => {
          console.error('Error al cargar compras:', error);
          resolve();
        }
      });
    });
  }

  obtenerNombreCliente(venta: Venta): string {
    if (venta.clienteId) {
      return `Cliente #${venta.clienteId}`;
    }
    return `Usuario #${venta.usuarioId}`;
  }

  obtenerProductosVenta(venta: Venta): string {
    if (!venta.detallesVenta || venta.detallesVenta.length === 0) {
      return 'Sin productos';
    }
    if (venta.detallesVenta.length === 1) {
      return venta.detallesVenta[0].producto?.nombre || 'Producto desconocido';
    }
    return `${venta.detallesVenta.length} productos`;
  }

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'N/A';
    try {
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit' 
      });
    } catch {
      return fecha;
    }
  }

  obtenerClaseEstado(estado: string): string {
    const estadoUpper = (estado || 'PENDIENTE').toUpperCase();
    if (estadoUpper === 'COMPLETADA' || estadoUpper === 'COMPLETADO') return 'ok';
    if (estadoUpper === 'CANCELADA' || estadoUpper === 'CANCELADO') return 'danger';
    if (estadoUpper === 'PENDIENTE') return 'warn';
    return 'info';
  }
}


