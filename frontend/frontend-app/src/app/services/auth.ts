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
      const parsedUser = JSON.parse(savedUser);
      // Normalizar el rol al cargar desde localStorage
      if (parsedUser.rol) {
        parsedUser.rol = this.normalizeRole(parsedUser.rol);
      }
      this.currentUserSubject.next(parsedUser);
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

  login(email: string, password: string): Observable<{ success: boolean; message?: string }> {
    return new Observable(observer => {
      this.http.post<any>(`${this.apiUrl}/login`, { email, password }).subscribe({
        next: (response) => {
          console.log('Login response:', response);
          if (response.success) {
            // Normalizar el rol del backend al formato del frontend
            const rolNormalizado = this.normalizeRole(response.user.rol);
            const user: User = {
              id: response.user.id,
              nombre: response.user.nombre,
              email: response.user.email,
              activo: response.user.activo,
              rol: rolNormalizado
            };
            this.currentUserSubject.next(user);
            localStorage.setItem('currentUser', JSON.stringify(user));
            if (response.token) {
              localStorage.setItem('authToken', response.token);
              // TEST LOG
              this.logTokenStatus(response.token, 'LOGIN');
            }
            observer.next({ success: true });
          } else {
            const errorMessage = response.message || 'Credenciales incorrectas';
            console.error('Login failed:', errorMessage);
            observer.next({ success: false, message: errorMessage });
          }
          observer.complete();
        },
        error: (error) => {
          console.error('Login error:', error);
          let errorMessage = 'Error al iniciar sesión';

          // Manejar error de conexión (status: 0)
          if (error.status === 0) {
            errorMessage = 'No se puede conectar al servidor. Verifica que el backend esté corriendo en http://localhost:8080';
            console.error('ERROR DE CONEXIÓN: El backend no está accesible');
            console.error('Verifica que:');
            console.error('1. El backend esté corriendo');
            console.error('2. El backend esté en el puerto 8080');
            console.error('3. No haya firewall bloqueando la conexión');
          } else if (error.error) {
            // Intentar extraer el mensaje de error del backend
            if (error.error.message) {
              errorMessage = error.error.message;
            } else if (typeof error.error === 'string') {
              errorMessage = error.error;
            }
          } else if (error.message) {
            errorMessage = error.message;
          }

          // Log detallado para debugging
          console.error('Error details:', {
            status: error.status,
            statusText: error.statusText,
            url: error.url,
            error: error.error,
            message: errorMessage
          });

          observer.next({ success: false, message: errorMessage });
          observer.complete();
        }
      });
    });
  }

  register(nombre: string, email: string, password: string): Observable<{ success: boolean; message?: string }> {
    return new Observable(observer => {
      this.http.post<any>(`${this.apiUrl}/register`, { nombre, email, password }).subscribe({
        next: (response) => {
          console.log('Register response:', response);
          if (response.success) {
            // Normalizar el rol del backend al formato del frontend
            const rolNormalizado = this.normalizeRole(response.user.rol);
            const user: User = {
              id: response.user.id,
              nombre: response.user.nombre,
              email: response.user.email,
              activo: response.user.activo,
              rol: rolNormalizado
            };
            this.currentUserSubject.next(user);
            localStorage.setItem('currentUser', JSON.stringify(user));
            if (response.token) {
              localStorage.setItem('authToken', response.token);
              // TEST LOG
              this.logTokenStatus(response.token, 'REGISTER');
            }
            observer.next({ success: true });
          } else {
            const errorMessage = response.message || 'Error al registrar usuario';
            console.error('Register failed:', errorMessage);
            observer.next({ success: false, message: errorMessage });
          }
          observer.complete();
        },
        error: (error) => {
          console.error('Register error:', error);
          let errorMessage = 'Error al registrar usuario';

          // Manejar error de conexión (status: 0)
          if (error.status === 0) {
            errorMessage = 'No se puede conectar al servidor. Verifica que el backend esté corriendo en http://localhost:8080';
            console.error('ERROR DE CONEXIÓN: El backend no está accesible');
            console.error('Verifica que:');
            console.error('1. El backend esté corriendo');
            console.error('2. El backend esté en el puerto 8080');
            console.error('3. No haya firewall bloqueando la conexión');
          } else if (error.error) {
            // Intentar extraer el mensaje de error del backend
            if (error.error.message) {
              errorMessage = error.error.message;
            } else if (typeof error.error === 'string') {
              errorMessage = error.error;
            }
          } else if (error.message) {
            errorMessage = error.message;
          }

          // Log detallado para debugging
          console.error('Error details:', {
            status: error.status,
            statusText: error.statusText,
            url: error.url,
            error: error.error,
            message: errorMessage
          });

          observer.next({ success: false, message: errorMessage });
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

  /**
   * Normaliza el rol del backend al formato esperado por el frontend
   */
  private normalizeRole(rol: string | undefined): 'Admin' | 'Empleado' | 'Usuario' {
    if (!rol) return 'Usuario';
    const rolLower = rol.toLowerCase().trim();
    // Mapear variantes del backend al formato del frontend
    if (rolLower === 'admin' || rolLower === 'administrador') {
      return 'Admin';
    }
    if (rolLower === 'empleado' || rolLower === 'vendedor' || rolLower === 'gerente') {
      return 'Empleado';
    }
    return 'Usuario';
  }

  hasRole(role: 'Admin' | 'Empleado' | 'Usuario'): boolean {
    const user = this.getCurrentUser();
    if (!user) return false;
    const normalizedRole = this.normalizeRole(user.rol);
    return normalizedRole === role;
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    if (!user) return false;
    const normalizedRole = this.normalizeRole(user.rol);
    return normalizedRole === 'Admin';
  }

  isEmpleado(): boolean {
    const user = this.getCurrentUser();
    if (!user) return false;
    const normalizedRole = this.normalizeRole(user.rol);
    return normalizedRole === 'Empleado';
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
            // Normalizar el rol del backend al formato del frontend
            const rolNormalizado = this.normalizeRole(response.user.rol);
            const user: User = {
              id: response.user.id,
              nombre: response.user.nombre,
              email: response.user.email,
              telefono: response.user.telefono,
              fechaNacimiento: response.user.fechaNacimiento,
              activo: response.user.activo,
              rol: rolNormalizado
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
