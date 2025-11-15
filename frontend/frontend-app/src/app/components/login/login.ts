import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  activeTab: 'login' | 'register' = 'login';
  loading = false;
  error = '';

  // Datos del formulario
  loginData = {
    email: '',
    password: ''
  };

  registerData = {
    nombre: '',
    email: '',
    password: '',
    confirmPassword: ''
  };

  emailExists = false;
  emailChecking = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  setActiveTab(tab: 'login' | 'register'): void {
    this.activeTab = tab;
    this.error = '';
  }

  onLogin(): void {
    if (!this.loginData.email || !this.loginData.password) {
      this.error = 'Por favor completa todos los campos';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.login(this.loginData.email, this.loginData.password).subscribe({
      next: (result) => {
        this.loading = false;
        if (result.success) {
          this.router.navigate(['/']);
        } else {
          this.error = result.message || 'Credenciales incorrectas';
        }
      },
      error: (error) => {
        this.loading = false;
        console.error('Login error in component:', error);
        this.error = 'Error al iniciar sesión';
      }
    });
  }

  onRegister(): void {
    if (!this.registerData.nombre || !this.registerData.email ||
        !this.registerData.password || !this.registerData.confirmPassword) {
      this.error = 'Por favor completa todos los campos';
      return;
    }

    if (this.registerData.password !== this.registerData.confirmPassword) {
      this.error = 'Las contraseñas no coinciden';
      return;
    }

    if (this.registerData.password.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }

    this.loading = true;
    this.error = '';

    console.log('Registrando usuario:', {
      nombre: this.registerData.nombre,
      email: this.registerData.email,
      passwordLength: this.registerData.password.length
    });

    this.authService.register(
      this.registerData.nombre,
      this.registerData.email,
      this.registerData.password
    ).subscribe({
      next: (result) => {
        this.loading = false;
        console.log('Register result:', result);
        if (result.success) {
          this.router.navigate(['/']);
        } else {
          this.error = result.message || 'Error al registrar usuario';
          console.error('Register failed:', this.error);
        }
      },
      error: (error) => {
        this.loading = false;
        console.error('Register error in component:', error);
        this.error = 'Error al registrar usuario';
      }
    });
  }

  togglePasswordVisibility(input: HTMLInputElement | null): void {
    if (input) {
      input.type = input.type === 'password' ? 'text' : 'password';
    }
  }

  // Verificar si el email ya existe
  checkEmailExists(): void {
    if (!this.registerData.email || this.registerData.email.length < 5) {
      this.emailExists = false;
      return;
    }

    this.emailChecking = true;
    this.authService.checkEmailExists(this.registerData.email).subscribe({
      next: (exists) => {
        this.emailExists = exists;
        this.emailChecking = false;
      },
      error: () => {
        this.emailExists = false;
        this.emailChecking = false;
      }
    });
  }

  // Verificar si el formulario de registro es válido
  isRegisterFormValid(): boolean {
    return this.registerData.nombre.length >= 2 &&
           this.registerData.email.length >= 5 &&
           !this.emailExists &&
           this.registerData.password.length >= 6 &&
           this.registerData.password === this.registerData.confirmPassword;
  }
}
