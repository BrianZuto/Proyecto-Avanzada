package com.proyectoavanzada.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "direcciones")
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    
    public Long getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }
    
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Column(name = "direccion", nullable = false)
    private String direccion;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "ciudad", nullable = false)
    private String ciudad;
    
    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
    @Column(name = "departamento", nullable = false)
    private String departamento;
    
    @Size(max = 20, message = "El código postal no puede exceder 20 caracteres")
    @Column(name = "codigo_postal")
    private String codigoPostal;
    
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    @Column(name = "pais", nullable = false)
    private String pais;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Direccion() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Direccion(Usuario usuario, String direccion, String ciudad, String departamento, String codigoPostal, String pais) {
        this();
        this.usuario = usuario;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.departamento = departamento;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
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
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public String getCodigoPostal() {
        return codigoPostal;
    }
    
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    public String getPais() {
        return pais;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
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

