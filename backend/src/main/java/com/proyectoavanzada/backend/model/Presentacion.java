package com.proyectoavanzada.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "presentaciones")
public class Presentacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @NotBlank(message = "La talla es obligatoria")
    @Column(name = "talla", nullable = false)
    private String talla; // 35, 36, 37, etc. o S, M, L, XL
    
    @NotBlank(message = "El color es obligatorio")
    @Column(name = "color", nullable = false)
    private String color;
    
    @Column(name = "codigo_color")
    private String codigoColor; // Código hexadecimal del color
    
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @Positive(message = "El stock debe ser mayor o igual a 0")
    @Column(name = "stock_disponible")
    private Integer stockDisponible = 0;
    
    @Column(name = "precio_especial")
    private BigDecimal precioEspecial; // Precio específico para esta presentación
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "es_destacado")
    private Boolean esDestacado = false;
    
    @Column(name = "ubicacion_almacen")
    private String ubicacionAlmacen; // A1-B2-C3, etc.
    
    @Column(name = "peso_especifico")
    private BigDecimal pesoEspecifico; // Peso específico de esta presentación
    
    @Column(name = "dimensiones")
    private String dimensiones; // Largo x Ancho x Alto
    
    @Column(name = "material_especifico")
    private String materialEspecifico; // Material específico de esta presentación
    
    // Relaciones
    @OneToMany(mappedBy = "presentacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCompra> detallesCompra;
    
    @OneToMany(mappedBy = "presentacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detallesVenta;
    
    @OneToMany(mappedBy = "presentacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventario> inventarios;
    
    // Constructores
    public Presentacion() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Presentacion(Producto producto, String talla, String color) {
        this();
        this.producto = producto;
        this.talla = talla;
        this.color = color;
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
    
    public String getTalla() {
        return talla;
    }
    
    public void setTalla(String talla) {
        this.talla = talla;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getCodigoColor() {
        return codigoColor;
    }
    
    public void setCodigoColor(String codigoColor) {
        this.codigoColor = codigoColor;
    }
    
    public String getImagenUrl() {
        return imagenUrl;
    }
    
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    public Integer getStockDisponible() {
        return stockDisponible;
    }
    
    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
    
    public BigDecimal getPrecioEspecial() {
        return precioEspecial;
    }
    
    public void setPrecioEspecial(BigDecimal precioEspecial) {
        this.precioEspecial = precioEspecial;
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
    
    public Boolean getEsDestacado() {
        return esDestacado;
    }
    
    public void setEsDestacado(Boolean esDestacado) {
        this.esDestacado = esDestacado;
    }
    
    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }
    
    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
    }
    
    public BigDecimal getPesoEspecifico() {
        return pesoEspecifico;
    }
    
    public void setPesoEspecifico(BigDecimal pesoEspecifico) {
        this.pesoEspecifico = pesoEspecifico;
    }
    
    public String getDimensiones() {
        return dimensiones;
    }
    
    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }
    
    public String getMaterialEspecifico() {
        return materialEspecifico;
    }
    
    public void setMaterialEspecifico(String materialEspecifico) {
        this.materialEspecifico = materialEspecifico;
    }
    
    public List<DetalleCompra> getDetallesCompra() {
        return detallesCompra;
    }
    
    public void setDetallesCompra(List<DetalleCompra> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }
    
    public List<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }
    
    public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }
    
    public List<Inventario> getInventarios() {
        return inventarios;
    }
    
    public void setInventarios(List<Inventario> inventarios) {
        this.inventarios = inventarios;
    }
    
    // Método para obtener el precio efectivo (especial o del producto)
    public BigDecimal getPrecioEfectivo() {
        return precioEspecial != null ? precioEspecial : producto.getPrecioVenta();
    }
    
    // Método para verificar si hay stock disponible
    public boolean tieneStock() {
        return stockDisponible > 0;
    }
    
    // Método para verificar si el stock está bajo
    public boolean stockBajo() {
        return stockDisponible <= producto.getStockMinimo();
    }
    
    // Método para obtener descripción completa
    public String getDescripcionCompleta() {
        return producto.getNombre() + " - " + talla + " - " + color;
    }
    
    // Método para actualizar fecha de modificación
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
