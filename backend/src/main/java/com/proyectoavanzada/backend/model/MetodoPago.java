package com.proyectoavanzada.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "metodos_pago")
public class MetodoPago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    
    @NotBlank(message = "El tipo de tarjeta es obligatorio")
    @Size(max = 50, message = "El tipo de tarjeta no puede exceder 50 caracteres")
    @Column(name = "tipo_tarjeta", nullable = false)
    private String tipoTarjeta; // VISA, MASTERCARD, AMEX, etc.
    
    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Size(max = 19, message = "El número de tarjeta no puede exceder 19 caracteres")
    @Column(name = "numero_tarjeta", nullable = false)
    private String numeroTarjeta; // Solo guardaremos los últimos 4 dígitos
    
    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(max = 100, message = "El nombre del titular no puede exceder 100 caracteres")
    @Column(name = "nombre_titular", nullable = false)
    private String nombreTitular;
    
    @NotBlank(message = "La fecha de expiración es obligatoria")
    @Size(max = 7, message = "La fecha de expiración debe tener formato MM/YY")
    @Column(name = "fecha_expiracion", nullable = false)
    private String fechaExpiracion; // Formato MM/YY
    
    @Column(name = "principal")
    private Boolean principal = false;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public MetodoPago() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public MetodoPago(Usuario usuario, String tipoTarjeta, String numeroTarjeta, 
                     String nombreTitular, String fechaExpiracion) {
        this();
        this.usuario = usuario;
        this.tipoTarjeta = tipoTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.nombreTitular = nombreTitular;
        this.fechaExpiracion = fechaExpiracion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    // Getter para obtener el ID del usuario sin serializar el objeto completo
    public Long getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }
    
    public String getTipoTarjeta() {
        return tipoTarjeta;
    }
    
    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }
    
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    
    public String getNombreTitular() {
        return nombreTitular;
    }
    
    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }
    
    public String getFechaExpiracion() {
        return fechaExpiracion;
    }
    
    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
    
    public Boolean getPrincipal() {
        return principal;
    }
    
    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}

