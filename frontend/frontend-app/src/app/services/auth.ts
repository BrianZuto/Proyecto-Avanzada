import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

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

  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {
    const savedUser = localStorage.getItem('currentUser');
    const token = localStorage.getItem('authToken');
    if (savedUser && token) {
      this.currentUserSubject.next(JSON.parse(savedUser));
      // TEST LOG: Mostrar estado del token al iniciar la app
      this.logTokenStatus(token, 'INIT');
    }
  }

  // TEST: Decodificar JWT y mostrar expiración en consola
  private logTokenStatus(token: string, source: 'LOGIN' | 'REGISTER' | 'INIT') {
    try {
      const payloadPart = token.split('.')[1];
      const payload = JSON.parse(atob(payloadPart));
      const expSec: number = payload.exp; // segundos desde epoch
      const nowMs = Date.now();
      const expMs = expSec * 1000;
      const remainingMs = expMs - nowMs;
      const active = remainingMs > 0;
      const remainingMin = Math.max(0, Math.floor(remainingMs / 60000));
      const expDate = new Date(expMs).toISOString();
      // Mensaje de prueba en consola
      console.log(`[JWT ${source}] Token ${active ? 'ACTIVO' : 'EXPIRADO'} | Expira: ${expDate} | Quedan ~${remainingMin} min`);
    } catch (e) {
      console.warn(`[JWT ${source}] No se pudo decodificar el token para logs de prueba`);
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
            if (response.token) {
              localStorage.setItem('authToken', response.token);
            // TEST LOG
            this.logTokenStatus(response.token, 'LOGIN');
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
            if (response.token) {
              localStorage.setItem('authToken', response.token);
            // TEST LOG
            this.logTokenStatus(response.token, 'REGISTER');
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

  logout(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
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

  updateProfile(userData: { id: number; nombre?: string; telefono?: string; fechaNacimiento?: string }): Observable<boolean> {
    return new Observable(observer => {
      this.http.put<any>(`${this.apiUrl}/update-profile`, userData).subscribe({
        next: (response) => {
          if (response.success) {
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
