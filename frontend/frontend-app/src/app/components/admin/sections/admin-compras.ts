import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CompraService } from '../../../services/compra.service';
import { ProveedorService } from '../../../services/proveedor.service';
import { ProductoService } from '../../../services/producto.service';
import { AuthService } from '../../../services/auth';
import { Compra, DetalleCompra } from '../../../models/compra';
import { Proveedor } from '../../../models/proveedor';
import { Producto } from '../../../models/producto';

@Component({
  selector: 'app-admin-compras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-compras.html',
  styleUrls: ['./admin-compras.css']
})
export class AdminComprasComponent implements OnInit {
  compras: Compra[] = [];
  proveedores: Proveedor[] = [];
  productos: Producto[] = [];
  
  mostrarFormulario: boolean = false;
  editando: boolean = false;
  compraForm: Compra = {
    proveedorId: 0,
    usuarioId: 0,
    estado: 'PENDIENTE',
    metodoPago: 'EFECTIVO',
    activo: true
  };
  
  detallesCompra: DetalleCompra[] = [];
  detalleForm: DetalleCompra = {
    productoId: 0,
    cantidad: 1,
    precioUnitario: 0
  };
  
  constructor(
    private compraService: CompraService,
    private proveedorService: ProveedorService,
    private productoService: ProductoService,
    private authService: AuthService
  ) {}
  
  ngOnInit(): void {
    this.cargarCompras();
    this.cargarProveedores();
    this.cargarProductos();
    
    // Obtener ID del usuario actual
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.compraForm.usuarioId = currentUser.id;
    }
  }
  
  cargarCompras(): void {
    this.compraService.obtenerComprasActivas().subscribe({
      next: (response) => {
        if (response.success) {
          this.compras = response.data;
        }
      },
      error: (error) => {
        console.error('Error al cargar compras:', error);
      }
    });
  }
  
  cargarProveedores(): void {
    this.proveedorService.obtenerProveedoresActivos().subscribe({
      next: (response) => {
        if (response.success) {
          this.proveedores = response.data || [];
          console.log('Proveedores cargados:', this.proveedores.length);
        } else {
          console.warn('Respuesta de proveedores sin éxito:', response);
          this.proveedores = [];
        }
      },
      error: (error) => {
        console.error('Error al cargar proveedores:', error);
        this.proveedores = [];
      }
    });
  }
  
  cargarProductos(): void {
    this.productoService.obtenerTodosLosProductos().subscribe({
      next: (response) => {
        if (response.success) {
          this.productos = response.data || [];
          console.log('Productos cargados:', this.productos.length);
        } else {
          console.warn('Respuesta de productos sin éxito:', response);
          this.productos = [];
        }
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
        this.productos = [];
      }
    });
  }
  
  abrirFormulario(compra?: Compra): void {
    if (compra) {
      this.editando = true;
      this.compraForm = { ...compra };
      this.cargarDetallesCompra(compra.id!);
    } else {
      this.editando = false;
      this.compraForm = {
        proveedorId: 0,
        usuarioId: this.authService.getCurrentUser()?.id || 0,
        estado: 'PENDIENTE',
        metodoPago: 'EFECTIVO',
        activo: true
      };
      this.detallesCompra = [];
    }
    this.mostrarFormulario = true;
  }
  
  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.editando = false;
    this.compraForm = {
      proveedorId: 0,
      usuarioId: this.authService.getCurrentUser()?.id || 0,
      estado: 'PENDIENTE',
      metodoPago: 'EFECTIVO',
      activo: true
    };
    this.detallesCompra = [];
    this.detalleForm = {
      productoId: 0,
      cantidad: 1,
      precioUnitario: 0
    };
  }
  
  agregarDetalle(): void {
    // Convertir productoId a número si viene como string
    const productoId = typeof this.detalleForm.productoId === 'string' 
      ? parseInt(this.detalleForm.productoId, 10) 
      : this.detalleForm.productoId;
    
    if (!productoId || productoId <= 0 || !this.detalleForm.cantidad || this.detalleForm.cantidad <= 0 || !this.detalleForm.precioUnitario || this.detalleForm.precioUnitario <= 0) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor completa todos los campos del detalle con valores válidos',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    const producto = this.productos.find(p => p.id === productoId);
    if (!producto) {
      console.error('Producto no encontrado. ID buscado:', productoId, 'Productos disponibles:', this.productos.map(p => ({ id: p.id, nombre: p.nombre })));
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Producto no encontrado. Por favor recarga la página o selecciona otro producto.',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    const subtotal = this.detalleForm.precioUnitario * this.detalleForm.cantidad;
    const nuevoDetalle: DetalleCompra = {
      ...this.detalleForm,
      productoId: productoId, // Usar el ID convertido
      subtotal: subtotal,
      producto: { id: producto.id!, nombre: producto.nombre, precioVenta: producto.precioVenta }
    };
    
    this.detallesCompra.push(nuevoDetalle);
    
    // Resetear formulario de detalle
    this.detalleForm = {
      productoId: 0,
      cantidad: 1,
      precioUnitario: 0
    };
  }
  
  eliminarDetalle(index: number): void {
    this.detallesCompra.splice(index, 1);
  }
  
  calcularTotal(): number {
    return this.detallesCompra.reduce((total, detalle) => {
      return total + (detalle.subtotal || 0);
    }, 0);
  }
  
  guardarCompra(): void {
    // Convertir proveedorId a número si viene como string
    const proveedorId = typeof this.compraForm.proveedorId === 'string' 
      ? parseInt(this.compraForm.proveedorId, 10) 
      : this.compraForm.proveedorId;
    
    if (!proveedorId || proveedorId <= 0) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor selecciona un proveedor',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    if (this.detallesCompra.length === 0) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor agrega al menos un producto a la compra',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    // Calcular subtotal (suma de todos los detalles)
    const subtotal = this.calcularTotal();
    const compraParaEnviar: any = {
      proveedor: { id: proveedorId }, // Usar el ID convertido
      usuario: { id: this.compraForm.usuarioId },
      subtotal: subtotal, // Enviar el subtotal calculado
      descuento: this.compraForm.descuento || 0,
      impuesto: this.compraForm.impuesto || 0,
      estado: this.compraForm.estado || 'PENDIENTE',
      metodoPago: this.compraForm.metodoPago || 'EFECTIVO',
      observaciones: this.compraForm.observaciones || null,
      detallesCompra: this.detallesCompra.map(d => ({
        producto: { id: d.productoId },
        cantidad: d.cantidad,
        precioUnitario: d.precioUnitario,
        descuento: d.descuento || 0,
        observaciones: d.observaciones || null
      }))
    };
    
    if (this.editando && this.compraForm.id) {
      this.compraService.actualizarCompra(this.compraForm.id, compraParaEnviar).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Compra actualizada correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarCompras();
            this.cerrarFormulario();
          }
        },
        error: (error) => {
          console.error('Error al actualizar compra:', error);
          const errorMessage = error.error?.message || 'Error al actualizar la compra';
          (window as any).Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    } else {
      this.compraService.crearCompra(compraParaEnviar).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Compra creada correctamente. El stock de los productos ha sido actualizado.',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarCompras();
            this.cargarProductos(); // Recargar productos para ver el stock actualizado
            this.cerrarFormulario();
          }
        },
        error: (error) => {
          console.error('Error al crear compra:', error);
          
          // Si el status es 201, significa que la compra se creó exitosamente
          // pero hubo un problema al parsear la respuesta JSON (probablemente por tamaño)
          if (error.status === 201) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Compra creada correctamente. El stock de los productos ha sido actualizado.',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarCompras();
            this.cargarProductos(); // Recargar productos para ver el stock actualizado
            this.cerrarFormulario();
            return;
          }
          
          const errorMessage = error.error?.message || 'Error al crear la compra';
          (window as any).Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    }
  }
  
  completarCompra(id: number | undefined): void {
    if (!id) return;
    
    (window as any).Swal.fire({
      title: '¿Completar compra?',
      text: 'Esta acción marcará la compra como completada',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#10b981',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, completar',
      cancelButtonText: 'Cancelar'
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.compraService.marcarComoPagada(id).subscribe({
          next: (response) => {
            if (response.success) {
              (window as any).Swal.fire({
                title: '¡Completada!',
                text: 'La compra ha sido marcada como completada',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                timer: 3000
              });
              this.cargarCompras();
            }
          },
          error: (error) => {
            console.error('Error al completar compra:', error);
            const errorMessage = error.error?.message || 'Error al completar la compra';
            (window as any).Swal.fire({
              title: 'Error',
              text: errorMessage,
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        });
      }
    });
  }
  
  cancelarCompra(id: number | undefined): void {
    if (!id) return;
    
    (window as any).Swal.fire({
      title: '¿Cancelar compra?',
      text: 'Esta acción marcará la compra como cancelada',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'No'
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.compraService.marcarComoCancelada(id).subscribe({
          next: (response) => {
            if (response.success) {
              (window as any).Swal.fire({
                title: '¡Cancelada!',
                text: 'La compra ha sido cancelada',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                timer: 3000
              });
              this.cargarCompras();
            }
          },
          error: (error) => {
            console.error('Error al cancelar compra:', error);
            const errorMessage = error.error?.message || 'Error al cancelar la compra';
            (window as any).Swal.fire({
              title: 'Error',
              text: errorMessage,
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        });
      }
    });
  }
  
  eliminarCompra(id: number | undefined): void {
    if (!id) return;
    
    (window as any).Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción revertirá el stock de los productos',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d32f2f',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.compraService.eliminarCompra(id).subscribe({
          next: (response) => {
            if (response.success) {
              (window as any).Swal.fire({
                title: '¡Eliminada!',
                text: 'La compra ha sido eliminada y el stock revertido',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                timer: 3000
              });
              this.cargarCompras();
              this.cargarProductos();
            }
          },
          error: (error) => {
            console.error('Error al eliminar compra:', error);
            const errorMessage = error.error?.message || 'Error al eliminar la compra';
            (window as any).Swal.fire({
              title: 'Error',
              text: errorMessage,
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        });
      }
    });
  }
  
  cargarDetallesCompra(compraId: number): void {
    this.compraService.obtenerDetallesCompra(compraId).subscribe({
      next: (response) => {
        if (response.success) {
          this.detallesCompra = response.data;
        }
      },
      error: (error) => {
        console.error('Error al cargar detalles:', error);
      }
    });
  }
  
  formatearPrecio(precio: number): string {
    return precio.toLocaleString('es-CO');
  }
  
  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return '';
    return new Date(fecha).toLocaleDateString('es-CO');
  }
  
  obtenerNombreProveedor(proveedorId: number | undefined): string {
    if (!proveedorId) return 'N/A';
    const proveedor = this.proveedores.find(p => p.id === proveedorId);
    return proveedor ? proveedor.nombre : 'N/A';
  }
  
  obtenerNombreProducto(productoId: number | undefined): string {
    if (!productoId) return 'N/A';
    const producto = this.productos.find(p => p.id === productoId);
    return producto ? producto.nombre : 'N/A';
  }
  
  onProductoSeleccionado(): void {
    // Convertir productoId a número si viene como string
    const productoId = typeof this.detalleForm.productoId === 'string' 
      ? parseInt(this.detalleForm.productoId, 10) 
      : this.detalleForm.productoId;
    
    const producto = this.productos.find(p => p.id === productoId);
    if (producto) {
      // Establecer el precio de compra si existe, o el precio de venta como referencia
      this.detalleForm.precioUnitario = producto.precioCompra || producto.precioVenta || 0;
      this.detalleForm.productoId = productoId; // Asegurar que el ID sea numérico
    }
  }
}
