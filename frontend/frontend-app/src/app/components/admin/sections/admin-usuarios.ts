import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../../services/usuario';
import { ProveedorService } from '../../../services/proveedor.service';
import { Usuario } from '../../../models/usuario';
import { Proveedor } from '../../../models/proveedor';

interface ItemLista {
  id?: number;
  nombre: string;
  email?: string;
  telefono?: string;
  rol?: string;
  tipo: 'usuario' | 'proveedor';
  fechaCreacion?: string;
  fechaRegistro?: string;
  activo?: boolean;
  ruc?: string; // Solo para proveedores
}

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-usuarios.html',
  styleUrls: ['./admin-usuarios.css']
})
export class AdminUsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  proveedores: Proveedor[] = [];
  itemsLista: ItemLista[] = [];
  
  usuariosCargados: boolean = false;
  proveedoresCargados: boolean = false;
  
  mostrarFormularioUsuario: boolean = false;
  mostrarFormularioProveedor: boolean = false;
  editandoUsuario: boolean = false;
  
  usuarioForm: Usuario = {
    nombre: '',
    email: '',
    password: '',
    rol: 'Usuario',
    activo: true
  };
  
  proveedorForm: Proveedor = {
    nombre: '',
    ruc: '',
    email: '',
    telefono: '',
    activo: true
  };
  
  constructor(
    private usuarioService: UsuarioService,
    private proveedorService: ProveedorService
  ) {}
  
  ngOnInit(): void {
    this.cargarUsuarios();
    this.cargarProveedores();
  }
  
  cargarUsuarios(): void {
    this.usuariosCargados = false;
    this.usuarioService.obtenerUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.usuariosCargados = true;
        this.combinarListas();
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
        this.usuariosCargados = true; // Marcar como cargado incluso si hay error
        this.combinarListas();
        (window as any).Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar los usuarios',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }
  
  cargarProveedores(): void {
    this.proveedoresCargados = false;
    this.proveedorService.obtenerTodosLosProveedores().subscribe({
      next: (response) => {
        console.log('Respuesta de proveedores:', response);
        if (response.success) {
          this.proveedores = response.data || [];
          console.log('Proveedores cargados:', this.proveedores.length, this.proveedores);
        } else {
          this.proveedores = [];
          console.log('Respuesta sin éxito:', response);
        }
        this.proveedoresCargados = true;
        this.combinarListas();
      },
      error: (error) => {
        console.error('Error al cargar proveedores:', error);
        console.error('Detalles del error:', error.error);
        this.proveedores = [];
        this.proveedoresCargados = true; // Marcar como cargado incluso si hay error
        this.combinarListas();
      }
    });
  }
  
  combinarListas(): void {
    // Solo combinar si ambas listas están cargadas
    if (!this.usuariosCargados || !this.proveedoresCargados) {
      return;
    }
    
    console.log('Combinando listas - Usuarios:', this.usuarios.length, 'Proveedores:', this.proveedores.length);
    
    this.itemsLista = [];
    
    // Agregar usuarios
    this.usuarios.forEach(usuario => {
      this.itemsLista.push({
        id: usuario.id,
        nombre: usuario.nombre,
        email: usuario.email,
        telefono: usuario.telefono,
        rol: usuario.rol || 'Usuario',
        tipo: 'usuario',
        fechaCreacion: usuario.fechaCreacion,
        activo: usuario.activo
      });
    });
    
    // Agregar proveedores
    this.proveedores.forEach(proveedor => {
      this.itemsLista.push({
        id: proveedor.id,
        nombre: proveedor.nombre,
        email: proveedor.email,
        telefono: proveedor.telefono,
        rol: 'Proveedor',
        tipo: 'proveedor',
        fechaRegistro: proveedor.fechaRegistro,
        activo: proveedor.activo,
        ruc: proveedor.ruc
      });
    });
    
    console.log('Items lista combinados:', this.itemsLista.length);
    
    // Ordenar por fecha (más recientes primero)
    this.itemsLista.sort((a, b) => {
      const fechaA = a.fechaCreacion || a.fechaRegistro || '';
      const fechaB = b.fechaCreacion || b.fechaRegistro || '';
      return fechaB.localeCompare(fechaA);
    });
  }
  
  abrirFormularioUsuario(usuario?: Usuario): void {
    if (usuario) {
      this.editandoUsuario = true;
      this.usuarioForm = { ...usuario };
      this.usuarioForm.password = ''; // No mostrar la contraseña
    } else {
      this.editandoUsuario = false;
      this.usuarioForm = {
        nombre: '',
        email: '',
        password: '',
        rol: 'Usuario',
        activo: true
      };
    }
    this.mostrarFormularioUsuario = true;
  }
  
  cerrarFormularioUsuario(): void {
    this.mostrarFormularioUsuario = false;
    this.editandoUsuario = false;
    this.usuarioForm = {
      nombre: '',
      email: '',
      password: '',
      rol: 'Usuario',
      activo: true
    };
  }
  
  abrirFormularioProveedor(): void {
    this.proveedorForm = {
      nombre: '',
      ruc: '',
      email: '',
      telefono: '',
      activo: true
    };
    this.mostrarFormularioProveedor = true;
  }
  
  cerrarFormularioProveedor(): void {
    this.mostrarFormularioProveedor = false;
    this.proveedorForm = {
      nombre: '',
      ruc: '',
      email: '',
      telefono: '',
      activo: true
    };
  }
  
  guardarUsuario(): void {
    if (!this.usuarioForm.nombre || !this.usuarioForm.email) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor completa todos los campos obligatorios',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    // Validar que si es nuevo, tenga contraseña
    if (!this.editandoUsuario && !this.usuarioForm.password) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'La contraseña es obligatoria para nuevos usuarios',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    // Si está editando y no hay contraseña, no enviarla
    const usuarioParaEnviar = { ...this.usuarioForm };
    if (this.editandoUsuario && !usuarioParaEnviar.password) {
      delete usuarioParaEnviar.password;
    }
    
    if (this.editandoUsuario && this.usuarioForm.id) {
      this.usuarioService.actualizarUsuario(this.usuarioForm.id, usuarioParaEnviar).subscribe({
        next: (response: any) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: response.message || 'Usuario actualizado correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarUsuarios();
            this.cargarProveedores();
            this.cerrarFormularioUsuario();
          } else {
            (window as any).Swal.fire({
              title: 'Error',
              text: response.message || 'Error al actualizar el usuario',
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        },
        error: (error) => {
          console.error('Error al actualizar usuario:', error);
          let errorMessage = 'Error al actualizar el usuario';
          if (error.status === 409) {
            errorMessage = 'El email ya está registrado';
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          }
          (window as any).Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    } else {
      this.usuarioService.crearUsuario(usuarioParaEnviar).subscribe({
        next: (response: any) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: response.message || 'Usuario creado correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarUsuarios();
            this.cargarProveedores();
            this.cerrarFormularioUsuario();
          } else {
            (window as any).Swal.fire({
              title: 'Error',
              text: response.message || 'Error al crear el usuario',
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        },
        error: (error) => {
          console.error('Error al crear usuario:', error);
          let errorMessage = 'Error al crear el usuario';
          if (error.status === 409) {
            errorMessage = 'El email ya está registrado';
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          }
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
  
  guardarProveedor(): void {
    if (!this.proveedorForm.nombre || !this.proveedorForm.ruc) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor completa el nombre y RUC del proveedor',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
    
    this.proveedorService.crearProveedor(this.proveedorForm).subscribe({
      next: (response) => {
        if (response.success) {
          (window as any).Swal.fire({
            title: '¡Éxito!',
            text: 'Proveedor creado correctamente',
            icon: 'success',
            confirmButtonText: 'Aceptar',
            timer: 3000
          });
          this.cargarProveedores();
          this.cerrarFormularioProveedor();
        }
      },
      error: (error) => {
        console.error('Error al crear proveedor:', error);
        let errorMessage = 'Error al crear el proveedor';
        
        // Si hay errores de validación específicos
        if (error.error?.errors) {
          const errors = error.error.errors;
          const errorMessages = Object.values(errors).join(', ');
          errorMessage = `Errores de validación: ${errorMessages}`;
        } else if (error.error?.message) {
          errorMessage = error.error.message;
        }
        
        (window as any).Swal.fire({
          title: 'Error',
          text: errorMessage,
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }
  
  eliminarUsuario(id: number | undefined): void {
    if (!id) return;
    
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
        this.usuarioService.eliminarUsuario(id).subscribe({
          next: (response: any) => {
            if (response && response.success !== false) {
              (window as any).Swal.fire({
                title: '¡Eliminado!',
                text: response?.message || 'El usuario ha sido eliminado',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                timer: 3000
              });
              this.cargarUsuarios();
              this.cargarProveedores();
            } else {
              (window as any).Swal.fire({
                title: 'Error',
                text: response?.message || 'Error al eliminar el usuario',
                icon: 'error',
                confirmButtonText: 'Aceptar'
              });
            }
          },
          error: (error) => {
            console.error('Error al eliminar usuario:', error);
            const errorMessage = error.error?.message || 'Error al eliminar el usuario';
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
  
  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'N/A';
    return new Date(fecha).toLocaleDateString('es-CO');
  }
  
  obtenerEstado(item: ItemLista): string {
    return item.activo ? 'Activo' : 'Inactivo';
  }
  
  obtenerFechaCreacion(item: ItemLista): string {
    return this.formatearFecha(item.fechaCreacion || item.fechaRegistro);
  }
  
  esProveedor(item: ItemLista): boolean {
    return item.tipo === 'proveedor';
  }
  
  abrirFormularioDesdeLista(item: ItemLista): void {
    if (item.tipo === 'proveedor') {
      // Para proveedores, por ahora no hay edición, solo creación
      this.abrirFormularioProveedor();
    } else {
      // Buscar el usuario en la lista
      const usuario = this.usuarios.find(u => u.id === item.id);
      if (usuario) {
        this.abrirFormularioUsuario(usuario);
      }
    }
  }
  
  eliminarItem(item: ItemLista): void {
    if (item.tipo === 'proveedor') {
      // Por ahora no hay eliminación de proveedores desde aquí
      (window as any).Swal.fire({
        title: 'Info',
        text: 'La eliminación de proveedores no está disponible desde esta vista',
        icon: 'info',
        confirmButtonText: 'Aceptar'
      });
    } else {
      this.eliminarUsuario(item.id);
    }
  }
}
