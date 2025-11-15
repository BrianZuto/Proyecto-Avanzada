import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, User } from '../../services/auth';
import { DireccionService } from '../../services/direccion.service';
import { Direccion } from '../../models/direccion';
import { MetodoPagoService } from '../../services/metodo-pago.service';
import { MetodoPago } from '../../models/metodo-pago';
import { VentaService } from '../../services/venta.service';
import { Venta } from '../../models/venta';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, MainLayoutComponent],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  activeTab: string = 'perfil';

  userData = {
    nombre: '',
    email: '',
    telefono: '',
    fechaNacimiento: ''
  };

  // Datos de direcciones
  direcciones: Direccion[] = [];
  direccionForm: Direccion = {
    usuarioId: 0,
    direccion: '',
    ciudad: '',
    departamento: '',
    codigoPostal: '',
    pais: ''
  };
  editandoDireccion: boolean = false;
  direccionEditandoId: number | null = null;

  // Datos de métodos de pago
  metodosPago: MetodoPago[] = [];
  metodoPagoForm: MetodoPago = {
    usuarioId: 0,
    tipoTarjeta: '',
    numeroTarjeta: '',
    nombreTitular: '',
    fechaExpiracion: '',
    principal: false
  };
  editandoMetodoPago: boolean = false;
  metodoPagoEditandoId: number | null = null;
  mostrarFormMetodoPago: boolean = false;

  // Datos de pedidos
  pedidos: Venta[] = [];
  cargandoPedidos = false;

  constructor(
    private authService: AuthService,
    private direccionService: DireccionService,
    private metodoPagoService: MetodoPagoService,
    private ventaService: VentaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();
    if (!this.user) {
      this.router.navigate(['/login']);
    } else {
      // Cargar datos básicos del usuario logueado
      this.userData = {
        nombre: this.user.nombre,
        email: this.user.email,
        telefono: '',
        fechaNacimiento: ''
      };

      // Cargar datos completos del perfil
      this.authService.getFullProfile(this.user.id).subscribe(fullUser => {
        if (fullUser) {
          this.userData.telefono = fullUser.telefono || '';
          this.userData.fechaNacimiento = fullUser.fechaNacimiento || '';
        }
      });

      // Cargar direcciones del usuario
      this.cargarDirecciones();
      // Cargar métodos de pago del usuario
      this.cargarMetodosPago();
      // Cargar pedidos del usuario
      this.cargarPedidos();
    }
  }

  cargarPedidos(): void {
    if (!this.user || !this.user.id) {
      return;
    }
    
    this.cargandoPedidos = true;
    this.ventaService.obtenerVentasPorUsuario(this.user.id).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.pedidos = response.data.filter(v => v.activo !== false);
          this.pedidos.sort((a, b) => {
            const fechaA = a.fechaVenta ? new Date(a.fechaVenta).getTime() : 0;
            const fechaB = b.fechaVenta ? new Date(b.fechaVenta).getTime() : 0;
            return fechaB - fechaA; // Más recientes primero
          });
        }
        this.cargandoPedidos = false;
      },
      error: (error) => {
        console.error('Error al cargar pedidos:', error);
        this.cargandoPedidos = false;
      }
    });
  }

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'N/A';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  obtenerEstadoBadge(estado: string | undefined): string {
    switch (estado) {
      case 'COMPLETADA':
        return 'badge-success';
      case 'CANCELADA':
        return 'badge-danger';
      case 'DEVUELTA':
        return 'badge-warning';
      default:
        return 'badge-secondary';
    }
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    if (tab === 'direcciones') {
      this.cargarDirecciones();
      this.inicializarFormularioDireccion();
    } else if (tab === 'pedidos') {
      this.cargarPedidos();
    } else if (tab === 'pagos') {
      this.cargarMetodosPago();
      this.mostrarFormMetodoPago = false;
      this.inicializarFormularioMetodoPago();
    } else {
      // Ocultar formularios al cambiar a otra pestaña
      this.mostrarFormMetodoPago = false;
    }
  }

  cargarDirecciones(): void {
    if (!this.user) return;
    
    this.direccionService.obtenerDireccionesPorUsuario(this.user.id).subscribe({
      next: (response) => {
        if (response.success && response.direcciones) {
          this.direcciones = response.direcciones.filter(d => d.activo !== false);
          console.log('Direcciones cargadas en perfil:', this.direcciones);
        } else {
          console.log('No se encontraron direcciones o respuesta sin éxito:', response);
          this.direcciones = [];
        }
      },
      error: (error) => {
        console.error('Error al cargar direcciones en perfil:', error);
        this.direcciones = [];
      }
    });
  }

  inicializarFormularioDireccion(): void {
    if (!this.user) return;
    
    this.direccionForm = {
      usuarioId: this.user.id,
      direccion: '',
      ciudad: '',
      departamento: '',
      codigoPostal: '',
      pais: ''
    };
    this.editandoDireccion = false;
    this.direccionEditandoId = null;
  }

  editarDireccion(direccion: Direccion): void {
    this.direccionForm = {
      usuarioId: direccion.usuarioId,
      direccion: direccion.direccion,
      ciudad: direccion.ciudad,
      departamento: direccion.departamento,
      codigoPostal: direccion.codigoPostal || '',
      pais: direccion.pais
    };
    this.editandoDireccion = true;
    this.direccionEditandoId = direccion.id || null;
  }

  guardarDireccion(): void {
    if (!this.user) return;

    // Validar campos requeridos
    if (!this.direccionForm.direccion || !this.direccionForm.ciudad || 
        !this.direccionForm.departamento || !this.direccionForm.pais) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor completa todos los campos obligatorios',
        icon: 'error',
        confirmButtonText: 'Aceptar',
        confirmButtonColor: '#d32f2f'
      });
      return;
    }

    this.direccionForm.usuarioId = this.user.id;

    if (this.editandoDireccion && this.direccionEditandoId) {
      // Actualizar dirección existente
      this.direccionService.actualizarDireccion(this.direccionEditandoId, this.direccionForm).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Dirección actualizada correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#1976d2',
              timer: 3000,
              timerProgressBar: true
            });
            this.cargarDirecciones();
            this.inicializarFormularioDireccion();
          } else {
            (window as any).Swal.fire({
              title: 'Error',
              text: 'No se pudo actualizar la dirección',
              icon: 'error',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#d32f2f'
            });
          }
        },
        error: () => {
          (window as any).Swal.fire({
            title: 'Error',
            text: 'Ocurrió un error al actualizar la dirección',
            icon: 'error',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#d32f2f'
          });
        }
      });
    } else {
      // Crear nueva dirección
      this.direccionService.crearDireccion(this.direccionForm).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Dirección guardada correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#1976d2',
              timer: 3000,
              timerProgressBar: true
            });
            this.cargarDirecciones();
            this.inicializarFormularioDireccion();
          } else {
            (window as any).Swal.fire({
              title: 'Error',
              text: 'No se pudo guardar la dirección',
              icon: 'error',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#d32f2f'
            });
          }
        },
        error: () => {
          (window as any).Swal.fire({
            title: 'Error',
            text: 'Ocurrió un error al guardar la dirección',
            icon: 'error',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#d32f2f'
          });
        }
      });
    }
  }

  eliminarDireccion(id: number): void {
    (window as any).Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d32f2f',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.direccionService.eliminarDireccion(id).subscribe({
          next: (response) => {
            if (response.success) {
              (window as any).Swal.fire({
                title: '¡Eliminado!',
                text: 'La dirección ha sido eliminada',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                confirmButtonColor: '#1976d2',
                timer: 3000,
                timerProgressBar: true
              });
              this.cargarDirecciones();
            } else {
              (window as any).Swal.fire({
                title: 'Error',
                text: 'No se pudo eliminar la dirección',
                icon: 'error',
                confirmButtonText: 'Aceptar',
                confirmButtonColor: '#d32f2f'
              });
            }
          },
          error: () => {
            (window as any).Swal.fire({
              title: 'Error',
              text: 'Ocurrió un error al eliminar la dirección',
              icon: 'error',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#d32f2f'
            });
          }
        });
      }
    });
  }

  saveChanges(): void {
    if (!this.user) return;

    const updateData = {
      id: this.user.id,
      nombre: this.userData.nombre,
      telefono: this.userData.telefono,
      fechaNacimiento: this.userData.fechaNacimiento
    };

    this.authService.updateProfile(updateData).subscribe({
      next: (success) => {
        if (success) {
          // Mostrar SweetAlert de éxito
          (window as any).Swal.fire({
            title: '¡Éxito!',
            text: 'Los cambios se guardaron correctamente',
            icon: 'success',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#1976d2',
            timer: 3000,
            timerProgressBar: true
          });

          // Actualizar los datos del usuario local
          this.user!.nombre = this.userData.nombre;
          this.user!.telefono = this.userData.telefono;
          this.user!.fechaNacimiento = this.userData.fechaNacimiento;
        } else {
          // Mostrar SweetAlert de error
          (window as any).Swal.fire({
            title: 'Error',
            text: 'No se pudieron guardar los cambios',
            icon: 'error',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#d32f2f'
          });
        }
      },
      error: () => {
        // Mostrar SweetAlert de error
        (window as any).Swal.fire({
          title: 'Error',
          text: 'Ocurrió un error al guardar los cambios',
          icon: 'error',
          confirmButtonText: 'Aceptar',
          confirmButtonColor: '#d32f2f'
        });
      }
    });
  }


  getRoleBadgeClass(): string {
    if (!this.user) return '';

    switch (this.user.rol) {
      case 'Admin':
        return 'badge-admin';
      case 'Empleado':
        return 'badge-empleado';
      case 'Usuario':
        return 'badge-usuario';
      default:
        return 'badge-usuario';
    }
  }

  getRoleIcon(): string {
    if (!this.user) return '';

    switch (this.user.rol) {
      case 'Admin':
        return '👑';
      case 'Empleado':
        return '👔';
      case 'Usuario':
        return '👤';
      default:
        return '👤';
    }
  }

  // Métodos para métodos de pago
  cargarMetodosPago(): void {
    if (!this.user) return;
    
    console.log('Cargando métodos de pago para usuario:', this.user.id);
    this.metodoPagoService.obtenerMetodosPagoPorUsuario(this.user.id).subscribe({
      next: (response) => {
        console.log('Respuesta de métodos de pago:', response);
        if (response.success) {
          console.log('Métodos de pago recibidos:', response.metodosPago);
          this.metodosPago = response.metodosPago || [];
          console.log('Métodos de pago asignados:', this.metodosPago);
        } else {
          console.warn('La respuesta no fue exitosa:', response);
          this.metodosPago = [];
        }
      },
      error: (error) => {
        console.error('Error al cargar métodos de pago:', error);
        this.metodosPago = [];
      }
    });
  }

  inicializarFormularioMetodoPago(): void {
    if (!this.user) return;
    
    this.metodoPagoForm = {
      usuarioId: this.user.id,
      tipoTarjeta: '',
      numeroTarjeta: '',
      nombreTitular: '',
      fechaExpiracion: '',
      principal: false
    };
    this.editandoMetodoPago = false;
    this.metodoPagoEditandoId = null;
    this.mostrarFormMetodoPago = true;
  }

  mostrarFormularioMetodoPago(): boolean {
    return this.mostrarFormMetodoPago || this.editandoMetodoPago;
  }

  editarMetodoPago(metodoPago: MetodoPago): void {
    this.metodoPagoForm = {
      usuarioId: metodoPago.usuarioId,
      tipoTarjeta: metodoPago.tipoTarjeta,
      numeroTarjeta: metodoPago.numeroTarjeta,
      nombreTitular: metodoPago.nombreTitular,
      fechaExpiracion: metodoPago.fechaExpiracion,
      principal: metodoPago.principal || false
    };
    this.editandoMetodoPago = true;
    this.metodoPagoEditandoId = metodoPago.id || null;
    this.mostrarFormMetodoPago = true;
  }

  guardarMetodoPago(): void {
    if (!this.user) return;

    // Validar campos requeridos
    if (!this.metodoPagoForm.tipoTarjeta || !this.metodoPagoForm.numeroTarjeta || 
        !this.metodoPagoForm.nombreTitular || !this.metodoPagoForm.fechaExpiracion) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor completa todos los campos obligatorios',
        icon: 'error',
        confirmButtonText: 'Aceptar',
        confirmButtonColor: '#d32f2f'
      });
      return;
    }

    // Validar formato de fecha de expiración (MM/YY)
    const fechaExpRegex = /^(0[1-9]|1[0-2])\/([0-9]{2})$/;
    if (!fechaExpRegex.test(this.metodoPagoForm.fechaExpiracion)) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'La fecha de expiración debe tener el formato MM/YY (ej: 12/26)',
        icon: 'error',
        confirmButtonText: 'Aceptar',
        confirmButtonColor: '#d32f2f'
      });
      return;
    }

    this.metodoPagoForm.usuarioId = this.user.id;

    if (this.editandoMetodoPago && this.metodoPagoEditandoId) {
      // Actualizar método de pago existente
      this.metodoPagoService.actualizarMetodoPago(this.metodoPagoEditandoId, this.metodoPagoForm).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Método de pago actualizado correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#1976d2',
              timer: 3000,
              timerProgressBar: true
            });
            this.cargarMetodosPago();
            this.mostrarFormMetodoPago = false;
            this.inicializarFormularioMetodoPago();
          } else {
            (window as any).Swal.fire({
              title: 'Error',
              text: 'No se pudo actualizar el método de pago',
              icon: 'error',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#d32f2f'
            });
          }
        },
        error: () => {
          (window as any).Swal.fire({
            title: 'Error',
            text: 'Ocurrió un error al actualizar el método de pago',
            icon: 'error',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#d32f2f'
          });
        }
      });
    } else {
      // Crear nuevo método de pago
      this.metodoPagoService.crearMetodoPago(this.metodoPagoForm).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Método de pago guardado correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#1976d2',
              timer: 3000,
              timerProgressBar: true
            });
            this.cargarMetodosPago();
            this.mostrarFormMetodoPago = false;
            this.inicializarFormularioMetodoPago();
          } else {
            (window as any).Swal.fire({
              title: 'Error',
              text: 'No se pudo guardar el método de pago',
              icon: 'error',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#d32f2f'
            });
          }
        },
        error: () => {
          (window as any).Swal.fire({
            title: 'Error',
            text: 'Ocurrió un error al guardar el método de pago',
            icon: 'error',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#d32f2f'
          });
        }
      });
    }
  }

  eliminarMetodoPago(id: number): void {
    (window as any).Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d32f2f',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.metodoPagoService.eliminarMetodoPago(id).subscribe({
          next: (response) => {
            if (response.success) {
              (window as any).Swal.fire({
                title: '¡Eliminado!',
                text: 'El método de pago ha sido eliminado',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                confirmButtonColor: '#1976d2',
                timer: 3000,
                timerProgressBar: true
              });
              this.cargarMetodosPago();
            } else {
              (window as any).Swal.fire({
                title: 'Error',
                text: 'No se pudo eliminar el método de pago',
                icon: 'error',
                confirmButtonText: 'Aceptar',
                confirmButtonColor: '#d32f2f'
              });
            }
          },
          error: () => {
            (window as any).Swal.fire({
              title: 'Error',
              text: 'Ocurrió un error al eliminar el método de pago',
              icon: 'error',
              confirmButtonText: 'Aceptar',
              confirmButtonColor: '#d32f2f'
            });
          }
        });
      }
    });
  }

  formatearNumeroTarjeta(numero: string): string {
    // Si el número tiene 4 dígitos, formatearlo como **** **** **** XXXX
    if (numero && numero.length === 4) {
      return `**** **** **** ${numero}`;
    }
    return numero;
  }

  obtenerColorTipoTarjeta(tipo: string): string {
    const tipoUpper = tipo.toUpperCase();
    switch (tipoUpper) {
      case 'VISA':
        return '#1a1f71';
      case 'MASTERCARD':
        return '#eb001b';
      case 'AMEX':
      case 'AMERICAN EXPRESS':
        return '#006fcf';
      default:
        return '#6c757d';
    }
  }
}
