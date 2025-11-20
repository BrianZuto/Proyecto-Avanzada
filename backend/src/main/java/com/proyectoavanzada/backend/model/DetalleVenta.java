package com.proyectoavanzada.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La venta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    private Venta venta;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "presentaciones", "detallesCompra", "detallesVenta", "inventarios"})
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentacion_id", nullable = true)
    @JsonIgnore
    private Presentacion presentacion;
    
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Positive(message = "El precio unitario debe ser mayor a 0")
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    // Constructores
    public DetalleVenta() {}
    
    public DetalleVenta(Venta venta, Producto producto, Presentacion presentacion, 
                       Integer cantidad, BigDecimal precioUnitario) {
        this.venta = venta;
        this.producto = producto;
        this.presentacion = presentacion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Venta getVenta() {
        return venta;
    }
    
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public Presentacion getPresentacion() {
        return presentacion;
    }
    
    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public BigDecimal getDescuento() {
        return descuento;
    }
    
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    // Métodos de negocio
    public void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            BigDecimal subtotalCalculado = precioUnitario.multiply(new BigDecimal(cantidad));
            if (descuento != null) {
                subtotalCalculado = subtotalCalculado.subtract(descuento);
            }
            this.subtotal = subtotalCalculado;
        }
    }
    
    public BigDecimal getTotalConDescuento() {
        if (subtotal != null && descuento != null) {
            return subtotal.subtract(descuento);
        }
        return subtotal;
    }
    
    // Método para verificar si hay stock suficiente
    public boolean hayStockSuficiente() {
        return presentacion != null && cantidad != null && 
               presentacion.getStockDisponible() >= cantidad;
    }
    
    // Método para actualizar el stock del producto
    public void actualizarStock() {
        if (presentacion != null && cantidad != null) {
            int nuevoStock = presentacion.getStockDisponible() - cantidad;
            presentacion.setStockDisponible(nuevoStock);
        }
    }
    
    // Método para revertir el stock (en caso de cancelación)
    public void revertirStock() {
        if (presentacion != null && cantidad != null) {
            int nuevoStock = presentacion.getStockDisponible() + cantidad;
            presentacion.setStockDisponible(nuevoStock);
        }
    }
    
    // Getters y Setters para IDs (para facilitar la deserialización desde el frontend)
    @JsonProperty("ventaId")
    public Long getVentaId() {
        return venta != null ? venta.getId() : null;
    }
    
    @JsonProperty("ventaId")
    public void setVentaId(Long ventaId) {
        if (ventaId != null) {
            this.venta = new Venta();
            this.venta.setId(ventaId);
        }
    }
    
    @JsonProperty("productoId")
    public Long getProductoId() {
        return producto != null ? producto.getId() : null;
    }
    
    @JsonProperty("productoId")
    public void setProductoId(Long productoId) {
        if (productoId != null) {
            this.producto = new Producto();
            this.producto.setId(productoId);
        }
    }
    
    @JsonProperty("presentacionId")
    public Long getPresentacionId() {
        return presentacion != null ? presentacion.getId() : null;
    }
    
    @JsonProperty("presentacionId")
    public void setPresentacionId(Long presentacionId) {
        if (presentacionId != null) {
            this.presentacion = new Presentacion();
            this.presentacion.setId(presentacionId);
        }
    }
}
