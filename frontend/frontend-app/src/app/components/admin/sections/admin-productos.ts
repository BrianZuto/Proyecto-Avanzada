import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../../services/producto.service';
import { Producto } from '../../../models/producto';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

interface Categoria {
  id?: number;
  nombre: string;
  descripcion?: string;
  activo?: boolean;
}

interface Marca {
  id?: number;
  nombre: string;
  descripcion?: string;
  activo?: boolean;
}

@Component({
  selector: 'app-admin-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-productos.html',
  styleUrl: './admin-productos.css'
})
export class AdminProductosComponent implements OnInit {
  productos: Producto[] = [];
  categorias: Categoria[] = [];
  marcas: Marca[] = [];
  
  mostrarFormulario: boolean = false;
  mostrarFormularioCategoria: boolean = false;
  mostrarFormularioMarca: boolean = false;
  editando: boolean = false;
  productoForm: Producto = {
    nombre: '',
    categoriaId: 0,
    marcaId: 0,
    precioVenta: 0,
    imagenPrincipal: '',
    activo: true
  };
  categoriaForm: Categoria = {
    nombre: '',
    descripcion: '',
    activo: true
  };
  marcaForm: Marca = {
    nombre: '',
    descripcion: '',
    activo: true
  };

  constructor(
    private productoService: ProductoService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
    this.cargarMarcas();
  }

  cargarProductos(): void {
    this.productoService.obtenerTodosLosProductos().subscribe({
      next: (response) => {
        if (response.success) {
          this.productos = response.data.map((p: any) => ({
            ...p,
            stock: p.stock || 0, // Usar el stock que viene del backend
            ventas: this.calcularVentas(p),
            estado: this.obtenerEstado(p)
          }));
        }
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
        (window as any).Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar los productos',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  cargarCategorias(): void {
    this.http.get<{ success: boolean; data: Categoria[] }>(`${environment.apiUrl}/categorias/activas`).subscribe({
      next: (response) => {
        if (response.success) {
          this.categorias = response.data;
        }
      },
      error: (error) => {
        console.error('Error al cargar categorías:', error);
      }
    });
  }

  cargarMarcas(): void {
    this.http.get<{ success: boolean; data: Marca[] }>(`${environment.apiUrl}/marcas/activas`).subscribe({
      next: (response) => {
        if (response.success) {
          this.marcas = response.data;
        }
      },
      error: (error) => {
        console.error('Error al cargar marcas:', error);
      }
    });
  }

  calcularStock(producto: Producto): number {
    // El stock ahora viene del backend calculado desde el inventario
    return producto.stock || 0;
  }

  calcularVentas(producto: Producto): number {
    // Por ahora retornamos 0, esto se puede calcular desde detalleVenta
    // TODO: Implementar cálculo real desde ventas
    return 0;
  }

  obtenerEstado(producto: Producto): 'Activo' | 'Agotado' {
    const stock = this.calcularStock(producto);
    if (!producto.activo) {
      return 'Agotado';
    }
    return stock > 0 ? 'Activo' : 'Agotado';
  }

  abrirFormulario(producto?: Producto): void {
    if (producto) {
      this.editando = true;
      this.productoForm = {
        ...producto,
        categoriaId: producto.categoria?.id || producto.categoriaId || 0,
        marcaId: producto.marca?.id || producto.marcaId || 0
      };
    } else {
      this.editando = false;
      this.productoForm = {
        nombre: '',
        categoriaId: 0,
        marcaId: 0,
        precioVenta: 0,
        imagenPrincipal: '',
        activo: true
      };
    }
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.editando = false;
    this.productoForm = {
      nombre: '',
      categoriaId: 0,
      marcaId: 0,
      precioVenta: 0,
      imagenPrincipal: '',
      activo: true
    };
  }

  guardarProducto(): void {
    if (!this.productoForm.nombre || !this.productoForm.categoriaId || !this.productoForm.marcaId || !this.productoForm.precioVenta) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor completa todos los campos obligatorios',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    // Validar que el precio sea mayor a 0
    if (this.productoForm.precioVenta <= 0) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'El precio de venta debe ser mayor a 0',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    // Validar que la categoría y marca sean válidas
    if (this.productoForm.categoriaId <= 0 || this.productoForm.marcaId <= 0) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'Por favor selecciona una categoría y una marca válidas',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    // Preparar el producto para enviar al backend
    const productoParaEnviar: any = {
      nombre: this.productoForm.nombre.trim(),
      descripcion: this.productoForm.descripcion ? this.productoForm.descripcion.trim() : null,
      precioVenta: Number(this.productoForm.precioVenta),
      precioCompra: this.productoForm.precioCompra ? Number(this.productoForm.precioCompra) : null,
      imagenPrincipal: this.productoForm.imagenPrincipal ? this.productoForm.imagenPrincipal.trim() : null,
      activo: this.productoForm.activo !== false,
      categoria: { id: Number(this.productoForm.categoriaId) },
      marca: { id: Number(this.productoForm.marcaId) }
    };

    if (this.editando && this.productoForm.id) {
      this.productoService.actualizarProducto(this.productoForm.id, productoParaEnviar).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Producto actualizado correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarProductos();
            this.cerrarFormulario();
          }
        },
        error: (error) => {
          console.error('Error al actualizar producto:', error);
          let errorMessage = 'No se pudo actualizar el producto';
          
          // Manejar errores de validación
          if (error.error?.errors) {
            const errors = error.error.errors;
            const errorList = Object.keys(errors).map(key => `${key}: ${errors[key]}`).join('\n');
            errorMessage = `Errores de validación:\n${errorList}`;
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          } else if (error.error?.error) {
            errorMessage = error.error.error;
          } else if (error.message) {
            errorMessage = error.message;
          }
          
          (window as any).Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Aceptar',
            width: '600px'
          });
        }
      });
    } else {
      this.productoService.crearProducto(productoParaEnviar).subscribe({
        next: (response) => {
          if (response.success) {
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Producto creado correctamente',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarProductos();
            this.cerrarFormulario();
          }
        },
        error: (error) => {
          console.error('Error al crear producto:', error);
          console.error('Error completo:', JSON.stringify(error, null, 2));
          
          // Si el status es 201, significa que el producto se creó pero hubo un error al parsear la respuesta
          if (error.status === 201) {
            // El producto se creó exitosamente, pero hubo un problema al parsear la respuesta
            (window as any).Swal.fire({
              title: '¡Éxito!',
              text: 'Producto creado correctamente (pero hubo un problema al leer la respuesta)',
              icon: 'success',
              confirmButtonText: 'Aceptar',
              timer: 3000
            });
            this.cargarProductos();
            this.cerrarFormulario();
            return;
          }
          
          let errorMessage = 'No se pudo crear el producto';
          
          // Manejar errores de validación
          if (error.error?.errors) {
            const errors = error.error.errors;
            const errorList = Object.keys(errors).map(key => `${key}: ${errors[key]}`).join('\n');
            errorMessage = `Errores de validación:\n${errorList}`;
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          } else if (error.error?.error) {
            errorMessage = error.error.error;
          } else if (error.message) {
            errorMessage = error.message;
          }
          
          (window as any).Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Aceptar',
            width: '600px'
          });
        }
      });
    }
  }

  eliminarProducto(producto: Producto): void {
    if (!producto.id) return;

    (window as any).Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar el producto "${producto.nombre}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d32f2f',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.productoService.eliminarProducto(producto.id!).subscribe({
          next: (response) => {
            if (response.success) {
              (window as any).Swal.fire({
                title: '¡Eliminado!',
                text: 'El producto ha sido eliminado',
                icon: 'success',
                confirmButtonText: 'Aceptar',
                timer: 3000
              });
              this.cargarProductos();
            }
          },
          error: (error) => {
            console.error('Error al eliminar producto:', error);
            (window as any).Swal.fire({
              title: 'Error',
              text: 'No se pudo eliminar el producto',
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        });
      }
    });
  }

  verProducto(producto: Producto): void {
    // TODO: Implementar vista detallada del producto
    (window as any).Swal.fire({
      title: producto.nombre,
      html: `
        <p><strong>Marca:</strong> ${producto.marca?.nombre || 'N/A'}</p>
        <p><strong>Precio:</strong> $${producto.precioVenta.toLocaleString()}</p>
        <p><strong>Stock:</strong> ${this.calcularStock(producto)}</p>
        <p><strong>Estado:</strong> ${this.obtenerEstado(producto)}</p>
      `,
      icon: 'info',
      confirmButtonText: 'Cerrar'
    });
  }

  formatearPrecio(precio: number): string {
    return precio.toLocaleString('es-CO');
  }

  obtenerNombreMarca(marcaId: number | undefined): string {
    if (!marcaId) return 'N/A';
    const marca = this.marcas.find(m => m.id === marcaId);
    return marca ? marca.nombre : 'N/A';
  }

  obtenerNombreMarcaDesdeProducto(producto: Producto): string {
    if (producto.marca?.nombre) {
      return producto.marca.nombre;
    }
    return this.obtenerNombreMarca(producto.marcaId);
  }

  // Métodos para categorías
  abrirFormularioCategoria(): void {
    this.categoriaForm = {
      nombre: '',
      descripcion: '',
      activo: true
    };
    this.mostrarFormularioCategoria = true;
  }

  cerrarFormularioCategoria(): void {
    this.mostrarFormularioCategoria = false;
    this.categoriaForm = {
      nombre: '',
      descripcion: '',
      activo: true
    };
  }

  guardarCategoria(): void {
    if (!this.categoriaForm.nombre) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'El nombre de la categoría es obligatorio',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.http.post<{ success: boolean; message: string; data: Categoria }>(
      `${environment.apiUrl}/categorias`,
      this.categoriaForm
    ).subscribe({
      next: (response) => {
        if (response.success) {
          (window as any).Swal.fire({
            title: '¡Éxito!',
            text: 'Categoría creada correctamente',
            icon: 'success',
            confirmButtonText: 'Aceptar',
            timer: 3000
          });
          this.cargarCategorias();
          this.cerrarFormularioCategoria();
        }
      },
      error: (error) => {
        console.error('Error al crear categoría:', error);
        const errorMessage = error.error?.message || 'No se pudo crear la categoría';
        (window as any).Swal.fire({
          title: 'Error',
          text: errorMessage,
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  // Métodos para marcas
  abrirFormularioMarca(): void {
    this.marcaForm = {
      nombre: '',
      descripcion: '',
      activo: true
    };
    this.mostrarFormularioMarca = true;
  }

  cerrarFormularioMarca(): void {
    this.mostrarFormularioMarca = false;
    this.marcaForm = {
      nombre: '',
      descripcion: '',
      activo: true
    };
  }

  guardarMarca(): void {
    if (!this.marcaForm.nombre) {
      (window as any).Swal.fire({
        title: 'Error',
        text: 'El nombre de la marca es obligatorio',
        icon: 'error',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.http.post<{ success: boolean; message: string; data: Marca }>(
      `${environment.apiUrl}/marcas`,
      this.marcaForm
    ).subscribe({
      next: (response) => {
        if (response.success) {
          (window as any).Swal.fire({
            title: '¡Éxito!',
            text: 'Marca creada correctamente',
            icon: 'success',
            confirmButtonText: 'Aceptar',
            timer: 3000
          });
          this.cargarMarcas();
          this.cerrarFormularioMarca();
        }
      },
      error: (error) => {
        console.error('Error al crear marca:', error);
        const errorMessage = error.error?.message || 'No se pudo crear la marca';
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
