import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuario-form.html',
  styleUrl: './usuario-form.css'
})
export class UsuarioFormComponent implements OnInit {
  @Input() usuario: Usuario | null = null;
  @Input() modoEdicion = false;
  @Output() usuarioGuardado = new EventEmitter<Usuario>();
  @Output() cancelar = new EventEmitter<void>();

  formUsuario: Usuario = {
    nombre: '',
    email: '',
    password: '',
    activo: true
  };

  ngOnInit(): void {
    console.log('UsuarioFormComponent ngOnInit - usuario:', this.usuario, 'modoEdicion:', this.modoEdicion);
    if (this.usuario && this.modoEdicion) {
      this.formUsuario = { ...this.usuario };
      console.log('Formulario inicializado para edición:', this.formUsuario);
    } else {
      // Resetear formulario para nuevo usuario
      this.formUsuario = {
        nombre: '',
        email: '',
        password: '',
        activo: true
      };
      console.log('Formulario inicializado para nuevo usuario:', this.formUsuario);
    }
  }

  onSubmit(): void {
    console.log('Formulario enviado - modoEdicion:', this.modoEdicion, 'datos:', this.formUsuario);
    if (this.validarFormulario()) {
      console.log('Formulario válido, emitiendo evento usuarioGuardado');
      this.usuarioGuardado.emit(this.formUsuario);
    } else {
      console.log('Formulario inválido');
    }
  }

  onCancel(): void {
    this.cancelar.emit();
  }

  private validarFormulario(): boolean {
    if (!this.formUsuario.nombre.trim()) {
      alert('El nombre es obligatorio');
      return false;
    }

    if (!this.formUsuario.email.trim()) {
      alert('El email es obligatorio');
      return false;
    }

    if (!this.validarEmail(this.formUsuario.email)) {
      alert('El email no tiene un formato válido');
      return false;
    }

    if (!this.modoEdicion && !this.formUsuario.password?.trim()) {
      alert('La contraseña es obligatoria');
      return false;
    }

    return true;
  }

  private validarEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
}
