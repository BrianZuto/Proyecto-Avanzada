package com.proyectoavanzada.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
    @Column(name = "ruc", nullable = false, unique = true)
    private String ruc;
    
    @Email(message = "El email debe tener un formato válido")
    @Column(name = "email")
    private String email;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "ciudad")
    private String ciudad;
    
    @Column(name = "contacto_nombre")
    private String contactoNombre;
    
    @Column(name = "contacto_telefono")
    private String contactoTelefono;
    
    @Column(name = "contacto_email")
    private String contactoEmail;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "credito_dias")
    private Integer creditoDias = 30; // Días de crédito
    
    @Column(name = "limite_credito")
    private Double limiteCredito = 0.0;
    
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
    
    // Relación con compras
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Compra> compras;
    
    // Constructores
    public Proveedor() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    public Proveedor(String nombre, String ruc) {
        this();
        this.nombre = nombre;
        this.ruc = ruc;
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
    
    public String getRuc() {
        return ruc;
    }
    
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    
    public String getContactoNombre() {
        return contactoNombre;
    }
    
    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }
    
    public String getContactoTelefono() {
        return contactoTelefono;
    }
    
    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }
    
    public String getContactoEmail() {
        return contactoEmail;
    }
    
    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public Integer getCreditoDias() {
        return creditoDias;
    }
    
    public void setCreditoDias(Integer creditoDias) {
        this.creditoDias = creditoDias;
    }
    
    public Double getLimiteCredito() {
        return limiteCredito;
    }
    
    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public List<Compra> getCompras() {
        return compras;
    }
    
    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
}
