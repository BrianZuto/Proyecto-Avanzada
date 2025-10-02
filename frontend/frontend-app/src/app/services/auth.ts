import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface User {
  id: number;
  nombre: string;
  email: string;
  telefono?: string;
  fechaNacimiento?: string;
  activo: boolean;
  rol: 'Admin' | 'Empleado' | 'Usuario';
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {
    // Verificar si hay un usuario guardado en localStorage
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  login(email: string, password: string): Observable<boolean> {
    return new Observable(observer => {
      this.http.post<any>(`${this.apiUrl}/login`, { email, password }).subscribe({
        next: (response) => {
          if (response.success) {
            const user: User = {
              id: response.user.id,
              nombre: response.user.nombre,
              email: response.user.email,
              activo: response.user.activo,
              rol: response.user.rol
            };
            this.currentUserSubject.next(user);
            localStorage.setItem('currentUser', JSON.stringify(user));
            observer.next(true);
          } else {
            observer.next(false);
          }
          observer.complete();
        },
        error: () => {
          observer.next(false);
          observer.complete();
        }
      });
    });
  }

  register(nombre: string, email: string, password: string): Observable<boolean> {
    return new Observable(observer => {
      this.http.post<any>(`${this.apiUrl}/register`, { nombre, email, password }).subscribe({
        next: (response) => {
          if (response.success) {
            const user: User = {
              id: response.user.id,
              nombre: response.user.nombre,
              email: response.user.email,
              activo: response.user.activo,
              rol: response.user.rol
            };
            this.currentUserSubject.next(user);
            localStorage.setItem('currentUser', JSON.stringify(user));
            observer.next(true);
          } else {
            observer.next(false);
          }
          observer.complete();
        },
        error: () => {
          observer.next(false);
          observer.complete();
        }
      });
    });
  }

  logout(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  hasRole(role: 'Admin' | 'Empleado' | 'Usuario'): boolean {
    const user = this.getCurrentUser();
    return user ? user.rol === role : false;
  }

  isAdmin(): boolean {
    return this.hasRole('Admin');
  }

  isEmpleado(): boolean {
    return this.hasRole('Empleado');
  }

  isUsuario(): boolean {
    return this.hasRole('Usuario');
  }

  // Método para verificar si un email ya existe
  checkEmailExists(email: string): Observable<boolean> {
    return new Observable(observer => {
      this.http.post<any>(`${this.apiUrl}/check-email`, { email }).subscribe({
        next: (response) => {
          observer.next(response.exists);
          observer.complete();
        },
        error: () => {
          observer.next(false);
          observer.complete();
        }
      });
    });
  }

  // Método para obtener el perfil completo del usuario
  getFullProfile(userId: number): Observable<User | null> {
    return new Observable(observer => {
      this.http.get<any>(`${this.apiUrl}/profile/${userId}`).subscribe({
        next: (response) => {
          if (response.success) {
            const user: User = {
              id: response.user.id,
              nombre: response.user.nombre,
              email: response.user.email,
              telefono: response.user.telefono,
              fechaNacimiento: response.user.fechaNacimiento,
              activo: response.user.activo,
              rol: response.user.rol
            };
            observer.next(user);
          } else {
            observer.next(null);
          }
          observer.complete();
        },
        error: () => {
          observer.next(null);
          observer.complete();
        }
      });
    });
  }

  // Método para actualizar el perfil del usuario
  updateProfile(userData: { id: number; nombre?: string; telefono?: string; fechaNacimiento?: string }): Observable<boolean> {
    return new Observable(observer => {
      this.http.put<any>(`${this.apiUrl}/update-profile`, userData).subscribe({
        next: (response) => {
          if (response.success) {
            // Actualizar el usuario actual con los nuevos datos
            const currentUser = this.currentUserSubject.value;
            if (currentUser) {
              const updatedUser: User = {
                ...currentUser,
                nombre: response.user.nombre,
                telefono: response.user.telefono,
                fechaNacimiento: response.user.fechaNacimiento
              };
              this.currentUserSubject.next(updatedUser);
              localStorage.setItem('currentUser', JSON.stringify(updatedUser));
            }
            observer.next(true);
          } else {
            observer.next(false);
          }
          observer.complete();
        },
        error: () => {
          observer.next(false);
          observer.complete();
        }
      });
    });
  }
}
