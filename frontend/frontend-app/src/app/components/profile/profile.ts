import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, User } from '../../services/auth';
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

  constructor(
    private authService: AuthService,
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
    }
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
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
}
