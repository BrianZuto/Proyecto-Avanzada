import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VentaService } from '../../../services/venta.service';
import { Venta, DetalleVenta } from '../../../models/venta';
import { jsPDF } from 'jspdf';

interface TopProduct { posicion: number; nombre: string; marca: string; ventas: number; }

@Component({
  selector: 'app-admin-ventas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-ventas.html',
  styleUrls: ['./admin-ventas.css']
})
export class AdminVentasComponent implements OnInit {
  ventas: Venta[] = [];
  topProducts: TopProduct[] = [];
  cargando = true;
  ventaSeleccionada: Venta | null = null;
  mostrarModal = false;

  constructor(private ventaService: VentaService) {}

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.cargando = true;
    this.ventaService.obtenerTodasLasVentas().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.ventas = response.data.filter(v => v.activo !== false);
          // Ordenar por fecha más reciente
          this.ventas.sort((a, b) => {
            const fechaA = a.fechaVenta || a.fechaCreacion || '';
            const fechaB = b.fechaVenta || b.fechaCreacion || '';
            return fechaB.localeCompare(fechaA);
          });
          this.calcularTopProductos();
        }
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar ventas:', error);
        this.cargando = false;
      }
    });
  }

  calcularTopProductos(): void {
    const productosMap = new Map<number, { nombre: string; marca: string; ventas: number }>();
    
    this.ventas.forEach(venta => {
      if (venta.detallesVenta) {
        venta.detallesVenta.forEach(detalle => {
          if (detalle.producto) {
            const productoId = detalle.producto.id || 0;
            const nombre = detalle.producto.nombre || 'Sin nombre';
            const marca = detalle.producto.marca?.nombre || 'Sin marca';
            const cantidad = detalle.cantidad || 0;
            
            if (productosMap.has(productoId)) {
              const producto = productosMap.get(productoId)!;
              producto.ventas += cantidad;
            } else {
              productosMap.set(productoId, { nombre, marca, ventas: cantidad });
            }
          }
        });
      }
    });

    // Convertir a array y ordenar por ventas
    this.topProducts = Array.from(productosMap.entries())
      .map(([id, data]) => ({ posicion: 0, ...data }))
      .sort((a, b) => b.ventas - a.ventas)
      .slice(0, 5)
      .map((p, index) => ({ ...p, posicion: index + 1 }));
  }

  obtenerNombreCliente(venta: Venta): string {
    // Si hay cliente asociado, usar su nombre, sino usar el usuario
    if (venta.clienteId) {
      return `Cliente #${venta.clienteId}`;
    }
    return `Usuario #${venta.usuarioId}`;
  }

  obtenerProductosVenta(venta: Venta): string {
    if (!venta.detallesVenta || venta.detallesVenta.length === 0) {
      return 'Sin productos';
    }
    if (venta.detallesVenta.length === 1) {
      return venta.detallesVenta[0].producto?.nombre || 'Producto desconocido';
    }
    return `${venta.detallesVenta.length} productos`;
  }

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'N/A';
    try {
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit' 
      });
    } catch {
      return fecha;
    }
  }

  obtenerEstadoBadge(estado: string | undefined): string {
    if (!estado) return 'PENDIENTE';
    return estado.toUpperCase();
  }

  obtenerClaseEstado(estado: string | undefined): string {
    const estadoUpper = (estado || 'PENDIENTE').toUpperCase();
    if (estadoUpper === 'COMPLETADA') return 'success';
    if (estadoUpper === 'CANCELADA') return 'danger';
    if (estadoUpper === 'PENDIENTE') return 'warning';
    return 'info';
  }

  tieneDescuentos(venta: Venta | null): boolean {
    if (!venta || !venta.detallesVenta) return false;
    return venta.detallesVenta.some(d => d.descuento && d.descuento > 0);
  }

  verDetalles(venta: Venta): void {
    this.ventaSeleccionada = venta;
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.ventaSeleccionada = null;
  }

  descargarPDF(): void {
    if (!this.ventaSeleccionada) return;

    const venta = this.ventaSeleccionada;
    const doc = new jsPDF();
    
    // Configuración de colores
    const primaryColor = [30, 58, 138]; // Azul oscuro
    const secondaryColor = [107, 114, 128]; // Gris
    
    // Encabezado
    doc.setFillColor(primaryColor[0], primaryColor[1], primaryColor[2]);
    doc.rect(0, 0, 210, 40, 'F');
    
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(24);
    doc.setFont('helvetica', 'bold');
    doc.text('SneakerZone', 20, 20);
    
    doc.setFontSize(12);
    doc.setFont('helvetica', 'normal');
    doc.text('Comprobante de Venta', 20, 30);
    
    // Información de la venta
    doc.setTextColor(0, 0, 0);
    doc.setFontSize(10);
    let yPos = 50;
    
    doc.setFont('helvetica', 'bold');
    doc.text('Número de Venta:', 20, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(`#${venta.numeroVenta || venta.id}`, 70, yPos);
    
    yPos += 8;
    doc.setFont('helvetica', 'bold');
    doc.text('Fecha:', 20, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(this.formatearFecha(venta.fechaVenta || venta.fechaCreacion), 70, yPos);
    
    yPos += 8;
    doc.setFont('helvetica', 'bold');
    doc.text('Cliente:', 20, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(this.obtenerNombreCliente(venta), 70, yPos);
    
    yPos += 8;
    doc.setFont('helvetica', 'bold');
    doc.text('Estado:', 20, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(this.obtenerEstadoBadge(venta.estado), 70, yPos);
    
    yPos += 8;
    doc.setFont('helvetica', 'bold');
    doc.text('Método de Pago:', 20, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(venta.metodoPago || 'N/A', 70, yPos);
    
    // Tabla de productos
    yPos += 15;
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(12);
    doc.text('Productos', 20, yPos);
    
    yPos += 8;
    // Encabezado de tabla
    doc.setFillColor(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
    doc.rect(20, yPos - 5, 170, 8, 'F');
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(9);
    doc.text('Producto', 22, yPos);
    doc.text('Cant.', 120, yPos);
    doc.text('Precio Unit.', 140, yPos);
    doc.text('Subtotal', 170, yPos);
    
    yPos += 8;
    doc.setTextColor(0, 0, 0);
    doc.setFont('helvetica', 'normal');
    
    if (venta.detallesVenta && venta.detallesVenta.length > 0) {
      venta.detallesVenta.forEach((detalle) => {
        if (yPos > 250) {
          doc.addPage();
          yPos = 20;
        }
        
        const nombreProducto = detalle.producto?.nombre || 'Producto desconocido';
        const cantidad = detalle.cantidad || 0;
        const precioUnitario = detalle.precioUnitario || 0;
        const subtotal = detalle.subtotal || 0;
        
        doc.text(nombreProducto.substring(0, 40), 22, yPos);
        doc.text(cantidad.toString(), 120, yPos);
        doc.text(`$${precioUnitario.toLocaleString()}`, 140, yPos);
        doc.text(`$${subtotal.toLocaleString()}`, 170, yPos);
        
        yPos += 7;
      });
    } else {
      doc.text('No hay productos registrados', 22, yPos);
      yPos += 7;
    }
    
    // Totales
    yPos += 5;
    doc.setDrawColor(200, 200, 200);
    doc.line(20, yPos, 190, yPos);
    yPos += 8;
    
    doc.setFont('helvetica', 'bold');
    doc.text('Subtotal:', 120, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(`$${(venta.subtotal || 0).toLocaleString()}`, 170, yPos);
    
    yPos += 7;
    doc.setFont('helvetica', 'bold');
    doc.text('Descuento:', 120, yPos);
    doc.setFont('helvetica', 'normal');
    doc.text(`$${(venta.descuento || 0).toLocaleString()}`, 170, yPos);
    
    yPos += 7;
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(12);
    doc.text('TOTAL:', 120, yPos);
    doc.setFont('helvetica', 'bold');
    doc.text(`$${(venta.total || 0).toLocaleString()}`, 170, yPos);
    
    // Pie de página
    const pageHeight = doc.internal.pageSize.height;
    doc.setFontSize(8);
    doc.setTextColor(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
    doc.text('Gracias por su compra', 105, pageHeight - 20, { align: 'center' });
    doc.text(`Generado el ${new Date().toLocaleDateString('es-ES')}`, 105, pageHeight - 15, { align: 'center' });
    
    // Descargar PDF
    const fileName = `comprobante_venta_${venta.numeroVenta || venta.id}.pdf`;
    doc.save(fileName);
  }
}


