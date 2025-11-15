package com.proyectoavanzada.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_venta", unique = true)
    private String numeroVenta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;
    
    @NotNull(message = "El usuario vendedor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    
    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;
    
    @Positive(message = "El subtotal debe ser mayor a 0")
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;
    
    @Column(name = "impuesto", precision = 10, scale = 2)
    private BigDecimal impuesto = BigDecimal.ZERO;
    
    @Positive(message = "El total debe ser mayor a 0")
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado")
    private String estado = "COMPLETADA"; // COMPLETADA, CANCELADA, DEVUELTA
    
    @Column(name = "metodo_pago")
    private String metodoPago; // EFECTIVO, TARJETA, TRANSFERENCIA, CREDITO
    
    @Column(name = "numero_comprobante")
    private String numeroComprobante;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "puntos_otorgados")
    private Integer puntosOtorgados = 0;
    
    @Column(name = "puntos_usados")
    private Integer puntosUsados = 0;
    
    @Column(name = "descuento_puntos", precision = 10, scale = 2)
    private BigDecimal descuentoPuntos = BigDecimal.ZERO;
    
    // Relaciones
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "venta"})
    private List<DetalleVenta> detallesVenta;
    
    // Constructores
    public Venta() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.fechaVenta = LocalDateTime.now();
    }
    
    public Venta(Usuario usuario) {
        this();
        this.usuario = usuario;
    }
    
    public Venta(Cliente cliente, Usuario usuario) {
        this();
        this.cliente = cliente;
        this.usuario = usuario;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumeroVenta() {
        return numeroVenta;
    }
    
    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }
    
    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getDescuento() {
        return descuento;
    }
    
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    public BigDecimal getImpuesto() {
        return impuesto;
    }
    
    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public String getNumeroComprobante() {
        return numeroComprobante;
    }
    
    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
    
    public Integer getPuntosOtorgados() {
        return puntosOtorgados;
    }
    
    public void setPuntosOtorgados(Integer puntosOtorgados) {
        this.puntosOtorgados = puntosOtorgados;
    }
    
    public Integer getPuntosUsados() {
        return puntosUsados;
    }
    
    public void setPuntosUsados(Integer puntosUsados) {
        this.puntosUsados = puntosUsados;
    }
    
    public BigDecimal getDescuentoPuntos() {
        return descuentoPuntos;
    }
    
    public void setDescuentoPuntos(BigDecimal descuentoPuntos) {
        this.descuentoPuntos = descuentoPuntos;
    }
    
    public List<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }
    
    public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }
    
    // Métodos de negocio
    public void calcularTotal() {
        if (subtotal != null) {
            BigDecimal totalCalculado = subtotal;
            if (descuento != null) {
                totalCalculado = totalCalculado.subtract(descuento);
            }
            if (descuentoPuntos != null) {
                totalCalculado = totalCalculado.subtract(descuentoPuntos);
            }
            if (impuesto != null) {
                totalCalculado = totalCalculado.add(impuesto);
            }
            this.total = totalCalculado;
        }
    }
    
    public void calcularPuntosFidelidad() {
        if (total != null && cliente != null) {
            // 1 punto por cada 10 soles gastados
            this.puntosOtorgados = total.divide(new BigDecimal("10")).intValue();
        }
    }
    
    public boolean esCompletada() {
        return "COMPLETADA".equals(estado);
    }
    
    public boolean esCancelada() {
        return "CANCELADA".equals(estado);
    }
    
    public boolean esDevuelta() {
        return "DEVUELTA".equals(estado);
    }
    
    public boolean esVentaConCliente() {
        return cliente != null;
    }
    
    public boolean esVentaAlContado() {
        return "EFECTIVO".equals(metodoPago) || "TARJETA".equals(metodoPago);
    }
    
    // Método para actualizar fecha de modificación
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters para IDs (para facilitar la deserialización desde el frontend)
    @JsonProperty("usuarioId")
    public Long getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }
    
    @JsonProperty("usuarioId")
    public void setUsuarioId(Long usuarioId) {
        if (usuarioId != null) {
            this.usuario = new Usuario();
            this.usuario.setId(usuarioId);
        }
    }
    
    @JsonProperty("clienteId")
    public Long getClienteId() {
        return cliente != null ? cliente.getId() : null;
    }
    
    @JsonProperty("clienteId")
    public void setClienteId(Long clienteId) {
        if (clienteId != null) {
            this.cliente = new Cliente();
            this.cliente.setId(clienteId);
        }
    }
}
