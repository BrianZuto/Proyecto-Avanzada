package com.proyectoavanzada.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @NotNull(message = "La presentación es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentacion_id", nullable = false)
    private Presentacion presentacion;
    
    @PositiveOrZero(message = "El stock debe ser mayor o igual a 0")
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;
    
    @PositiveOrZero(message = "El stock mínimo debe ser mayor o igual a 0")
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 5;
    
    @PositiveOrZero(message = "El stock máximo debe ser mayor o igual a 0")
    @Column(name = "stock_maximo")
    private Integer stockMaximo;
    
    @Column(name = "ubicacion_almacen")
    private String ubicacionAlmacen;
    
    @Column(name = "fecha_ultima_entrada")
    private LocalDateTime fechaUltimaEntrada;
    
    @Column(name = "fecha_ultima_salida")
    private LocalDateTime fechaUltimaSalida;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    // Constructores
    public Inventario() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Inventario(Producto producto, Presentacion presentacion, Integer stockActual) {
        this();
        this.producto = producto;
        this.presentacion = presentacion;
        this.stockActual = stockActual;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Integer getStockActual() {
        return stockActual;
    }
    
    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }
    
    public Integer getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public Integer getStockMaximo() {
        return stockMaximo;
    }
    
    public void setStockMaximo(Integer stockMaximo) {
        this.stockMaximo = stockMaximo;
    }
    
    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }
    
    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
    }
    
    public LocalDateTime getFechaUltimaEntrada() {
        return fechaUltimaEntrada;
    }
    
    public void setFechaUltimaEntrada(LocalDateTime fechaUltimaEntrada) {
        this.fechaUltimaEntrada = fechaUltimaEntrada;
    }
    
    public LocalDateTime getFechaUltimaSalida() {
        return fechaUltimaSalida;
    }
    
    public void setFechaUltimaSalida(LocalDateTime fechaUltimaSalida) {
        this.fechaUltimaSalida = fechaUltimaSalida;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    // Métodos de negocio
    public void agregarStock(Integer cantidad) {
        if (cantidad > 0) {
            this.stockActual += cantidad;
            this.fechaUltimaEntrada = LocalDateTime.now();
            this.fechaActualizacion = LocalDateTime.now();
        }
    }
    
    public void reducirStock(Integer cantidad) {
        if (cantidad > 0 && this.stockActual >= cantidad) {
            this.stockActual -= cantidad;
            this.fechaUltimaSalida = LocalDateTime.now();
            this.fechaActualizacion = LocalDateTime.now();
        }
    }
    
    public boolean hayStockSuficiente(Integer cantidad) {
        return this.stockActual >= cantidad;
    }
    
    public boolean stockBajo() {
        return this.stockActual <= this.stockMinimo;
    }
    
    public boolean stockAlto() {
        return stockMaximo != null && this.stockActual >= this.stockMaximo;
    }
    
    public Integer getStockDisponible() {
        return Math.max(0, this.stockActual);
    }
    
    public String getEstadoStock() {
        if (stockBajo()) {
            return "BAJO";
        } else if (stockAlto()) {
            return "ALTO";
        } else {
            return "NORMAL";
        }
    }
    
    // Método para actualizar fecha de modificación
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
