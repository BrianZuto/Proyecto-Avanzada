import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../services/auth';
import { CarritoService } from '../../services/carrito.service';
import { DireccionService } from '../../services/direccion.service';
import { MetodoPagoService } from '../../services/metodo-pago.service';
import { VentaService } from '../../services/venta.service';
import { CarritoItem } from '../../models/carrito-item';
import { Direccion } from '../../models/direccion';
import { MetodoPago } from '../../models/metodo-pago';
import { Venta, DetalleVenta } from '../../models/venta';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css'
})
export class MainLayoutComponent implements OnInit, OnDestroy {
  showUserDropdown = false;
  showCarritoModal = false;
  carritoItems: CarritoItem[] = [];
  cantidadCarrito = 0;
  direcciones: Direccion[] = [];
  metodosPago: MetodoPago[] = [];
  direccionSeleccionada: number | null = null;
  metodoPagoSeleccionado: number | null = null;
  procesandoCompra = false;
  
  private carritoSubscription?: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    private carritoService: CarritoService,
    private direccionService: DireccionService,
    private metodoPagoService: MetodoPagoService,
    private ventaService: VentaService
  ) {}

  ngOnInit(): void {
    this.carritoSubscription = this.carritoService.carrito$.subscribe(items => {
      this.carritoItems = items;
      this.cantidadCarrito = this.carritoService.obtenerCantidadTotal();
    });
    
    if (this.isLoggedIn()) {
      this.cargarDirecciones();
      this.cargarMetodosPago();
    }
  }

  ngOnDestroy(): void {
    if (this.carritoSubscription) {
      this.carritoSubscription.unsubscribe();
    }
  }

  cargarDirecciones(): void {
    const usuario = this.authService.getCurrentUser();
    if (usuario && usuario.id) {
      this.direccionService.obtenerDireccionesPorUsuario(usuario.id).subscribe({
        next: (response) => {
          if (response.success && response.direcciones) {
            this.direcciones = response.direcciones.filter(d => d.activo !== false);
            console.log('Direcciones cargadas:', this.direcciones);
          } else {
            console.log('No se encontraron direcciones o respuesta sin éxito:', response);
          }
        },
        error: (error) => {
          console.error('Error al cargar direcciones:', error);
        }
      });
    } else {
      console.log('No hay usuario logueado para cargar direcciones');
    }
  }

  cargarMetodosPago(): void {
    const usuario = this.authService.getCurrentUser();
    if (usuario && usuario.id) {
      this.metodoPagoService.obtenerMetodosPagoPorUsuario(usuario.id).subscribe({
        next: (response) => {
          if (response.success && response.metodosPago) {
            this.metodosPago = response.metodosPago.filter(m => m.activo !== false);
          }
        },
        error: (error) => {
          console.error('Error al cargar métodos de pago:', error);
        }
      });
    }
  }

  toggleUserDropdown(): void {
    this.showUserDropdown = !this.showUserDropdown;
  }

  toggleCarritoModal(): void {
    if (!this.isLoggedIn()) {
      (window as any).Swal.fire({
        title: 'Inicia sesión',
        text: 'Debes iniciar sesión para ver tu carrito',
        icon: 'info',
        confirmButtonText: 'Iniciar sesión'
      }).then(() => {
        this.router.navigate(['/login']);
      });
      return;
    }
    
    this.showCarritoModal = !this.showCarritoModal;
    if (this.showCarritoModal) {
      // Limpiar selecciones anteriores
      this.direccionSeleccionada = null;
      this.metodoPagoSeleccionado = null;
      // Cargar direcciones y métodos de pago
      this.cargarDirecciones();
      this.cargarMetodosPago();
    }
  }

  eliminarDelCarrito(productoId: number | undefined): void {
    if (productoId) {
      this.carritoService.eliminarProducto(productoId);
    }
  }

  actualizarCantidad(productoId: number | undefined, cantidad: number): void {
    if (productoId && cantidad > 0) {
      this.carritoService.actualizarCantidad(productoId, cantidad);
    }
  }

  obtenerTotal(): number {
    return this.carritoService.obtenerTotal();
  }

  finalizarCompra(): void {
    if (!this.isLoggedIn()) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Debes iniciar sesión para realizar una compra',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    if (this.carritoItems.length === 0) {
      (window as any).Swal.fire({
        title: 'Carrito vacío',
        text: 'Agrega productos al carrito antes de finalizar la compra',
        icon: 'warning',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    if (!this.direccionSeleccionada) {
      (window as any).Swal.fire({
        title: 'Dirección requerida',
        text: 'Por favor selecciona una dirección de envío',
        icon: 'warning',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    if (!this.metodoPagoSeleccionado) {
      (window as any).Swal.fire({
        title: 'Método de pago requerido',
        text: 'Por favor selecciona un método de pago',
        icon: 'warning',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    const usuario = this.authService.getCurrentUser();
    if (!usuario || !usuario.id) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'No se pudo obtener la información del usuario',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.procesandoCompra = true;

    // Calcular subtotal
    const subtotal = this.obtenerTotal();
    
    // Crear detalles de venta
    const detallesVenta: DetalleVenta[] = this.carritoItems.map(item => ({
      productoId: item.producto.id!,
      cantidad: item.cantidad,
      precioUnitario: item.precioUnitario,
      subtotal: item.subtotal
    }));

    // Crear venta
    const venta: Venta = {
      usuarioId: usuario.id,
      subtotal: subtotal,
      descuento: 0,
      impuesto: 0,
      total: subtotal,
      estado: 'COMPLETADA',
      metodoPago: this.metodosPago.find(m => m.id === this.metodoPagoSeleccionado)?.tipoTarjeta || 'TARJETA',
      detallesVenta: detallesVenta
    };

    this.ventaService.crearVenta(venta).subscribe({
      next: (response) => {
        if (response.success) {
          (window as any).Swal.fire({
            title: '¡Compra exitosa!',
            text: 'Tu pedido ha sido procesado correctamente',
            icon: 'success',
            confirmButtonText: 'Aceptar'
          });
          this.carritoService.limpiarCarrito();
          this.showCarritoModal = false;
          this.direccionSeleccionada = null;
          this.metodoPagoSeleccionado = null;
        } else {
          (window as any).Swal.fire({
            title: 'Error',
            text: response.message || 'Error al procesar la compra',
            icon: 'error',
            confirmButtonText: 'Aceptar'
          });
        }
        this.procesandoCompra = false;
      },
      error: (error) => {
        console.error('Error al crear venta:', error);
        console.error('Error details:', error.error);
        let mensaje = 'Error al procesar la compra';
        if (error.error?.message) {
          mensaje = error.error.message;
        }
        // Mostrar errores de validación específicos
        if (error.error?.errors) {
          const errores = error.error.errors;
          const erroresTexto = Object.keys(errores).map(key => `${key}: ${errores[key]}`).join('\n');
          mensaje = `Errores de validación:\n${erroresTexto}`;
        }
        (window as any).Swal.fire({
          title: 'Error',
          text: mensaje,
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
        this.procesandoCompra = false;
      }
    });
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  isEmpleado(): boolean {
    return this.authService.isEmpleado();
  }

  getCurrentUser() {
    return this.authService.getCurrentUser();
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  goToRegister(): void {
    this.router.navigate(['/login']);
  }

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  logout(): void {
    this.authService.logout();
    this.carritoService.limpiarCarrito();
    this.router.navigate(['/']);
  }
}
