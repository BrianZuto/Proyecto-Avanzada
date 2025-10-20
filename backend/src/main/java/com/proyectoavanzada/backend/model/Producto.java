package com.proyectoavanzada.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "codigo_producto", unique = true)
    private String codigoProducto;
    
    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @NotNull(message = "La marca es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;
    
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta;
    
    @Positive(message = "El precio de compra debe ser mayor a 0")
    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "imagen_principal")
    private String imagenPrincipal;
    
    @Column(name = "imagenes_adicionales", columnDefinition = "TEXT")
    private String imagenesAdicionales; // JSON array de URLs
    
    @Column(name = "genero")
    private String genero; // Masculino, Femenino, Unisex
    
    @Column(name = "edad_target")
    private String edadTarget; // Adulto, Niño, Bebé
    
    @Column(name = "material_principal")
    private String materialPrincipal;
    
    @Column(name = "tipo_suela")
    private String tipoSuela;
    
    @Column(name = "tecnologia")
    private String tecnologia; // Air Max, Boost, etc.
    
    @Column(name = "peso_gramos")
    private Integer pesoGramos;
    
    @Column(name = "garantia_meses")
    private Integer garantiaMeses;
    
    @Column(name = "stock_minimo")
    private Integer stockMinimo = 5;
    
    @Column(name = "es_destacado")
    private Boolean esDestacado = false;
    
    @Column(name = "es_nuevo")
    private Boolean esNuevo = true;
    
    @Column(name = "descuento_porcentaje")
    private BigDecimal descuentoPorcentaje = BigDecimal.ZERO;
    
    // Relaciones
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Presentacion> presentaciones;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCompra> detallesCompra;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detallesVenta;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventario> inventarios;
    
    // Constructores
    public Producto() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Producto(String nombre, Categoria categoria, Marca marca, BigDecimal precioVenta) {
        this();
        this.nombre = nombre;
        this.categoria = categoria;
        this.marca = marca;
        this.precioVenta = precioVenta;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public Marca getMarca() {
        return marca;
    }
    
    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }
    
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
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
    
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }
    
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }
    
    public String getImagenesAdicionales() {
        return imagenesAdicionales;
    }
    
    public void setImagenesAdicionales(String imagenesAdicionales) {
        this.imagenesAdicionales = imagenesAdicionales;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getEdadTarget() {
        return edadTarget;
    }
    
    public void setEdadTarget(String edadTarget) {
        this.edadTarget = edadTarget;
    }
    
    public String getMaterialPrincipal() {
        return materialPrincipal;
    }
    
    public void setMaterialPrincipal(String materialPrincipal) {
        this.materialPrincipal = materialPrincipal;
    }
    
    public String getTipoSuela() {
        return tipoSuela;
    }
    
    public void setTipoSuela(String tipoSuela) {
        this.tipoSuela = tipoSuela;
    }
    
    public String getTecnologia() {
        return tecnologia;
    }
    
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
    
    public Integer getPesoGramos() {
        return pesoGramos;
    }
    
    public void setPesoGramos(Integer pesoGramos) {
        this.pesoGramos = pesoGramos;
    }
    
    public Integer getGarantiaMeses() {
        return garantiaMeses;
    }
    
    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }
    
    public Integer getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public Boolean getEsDestacado() {
        return esDestacado;
    }
    
    public void setEsDestacado(Boolean esDestacado) {
        this.esDestacado = esDestacado;
    }
    
    public Boolean getEsNuevo() {
        return esNuevo;
    }
    
    public void setEsNuevo(Boolean esNuevo) {
        this.esNuevo = esNuevo;
    }
    
    public BigDecimal getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }
    
    public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }
    
    public List<Presentacion> getPresentaciones() {
        return presentaciones;
    }
    
    public void setPresentaciones(List<Presentacion> presentaciones) {
        this.presentaciones = presentaciones;
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
    
    // Método para calcular precio con descuento
    public BigDecimal getPrecioConDescuento() {
        if (descuentoPorcentaje.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal descuento = precioVenta.multiply(descuentoPorcentaje.divide(new BigDecimal("100")));
            return precioVenta.subtract(descuento);
        }
        return precioVenta;
    }
    
    // Método para actualizar fecha de modificación
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
