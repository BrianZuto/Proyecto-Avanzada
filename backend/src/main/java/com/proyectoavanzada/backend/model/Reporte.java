package com.proyectoavanzada.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "reportes")
public class Reporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del reporte es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "tipo_reporte")
    private String tipoReporte; // VENTAS, COMPRAS, INVENTARIO, CLIENTES, PRODUCTOS
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;
    
    @NotNull(message = "El usuario que genera el reporte es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "parametros", columnDefinition = "TEXT")
    private String parametros; // JSON con parámetros del reporte
    
    @Column(name = "resultados", columnDefinition = "LONGTEXT")
    private String resultados; // JSON con los resultados del reporte
    
    @Column(name = "estado")
    private String estado = "GENERADO"; // GENERADO, PROCESANDO, ERROR
    
    @Column(name = "archivo_url")
    private String archivoUrl; // URL del archivo generado (PDF, Excel, etc.)
    
    @Column(name = "formato")
    private String formato; // PDF, EXCEL, CSV, JSON
    
    @Column(name = "tamaño_archivo")
    private Long tamañoArchivo; // Tamaño en bytes
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Constructores
    public Reporte() {
        this.fechaGeneracion = LocalDateTime.now();
    }
    
    public Reporte(String nombre, String tipoReporte, Usuario usuario) {
        this();
        this.nombre = nombre;
        this.tipoReporte = tipoReporte;
        this.usuario = usuario;
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
    
    public String getTipoReporte() {
        return tipoReporte;
    }
    
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }
    
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getParametros() {
        return parametros;
    }
    
    public void setParametros(String parametros) {
        this.parametros = parametros;
    }
    
    public String getResultados() {
        return resultados;
    }
    
    public void setResultados(String resultados) {
        this.resultados = resultados;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getArchivoUrl() {
        return archivoUrl;
    }
    
    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }
    
    public String getFormato() {
        return formato;
    }
    
    public void setFormato(String formato) {
        this.formato = formato;
    }
    
    public Long getTamañoArchivo() {
        return tamañoArchivo;
    }
    
    public void setTamañoArchivo(Long tamañoArchivo) {
        this.tamañoArchivo = tamañoArchivo;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    // Métodos de negocio
    public boolean esGenerado() {
        return "GENERADO".equals(estado);
    }
    
    public boolean estaProcesando() {
        return "PROCESANDO".equals(estado);
    }
    
    public boolean tieneError() {
        return "ERROR".equals(estado);
    }
    
    public boolean tieneArchivo() {
        return archivoUrl != null && !archivoUrl.isEmpty();
    }
    
    public String getTamañoArchivoFormateado() {
        if (tamañoArchivo == null) return "N/A";
        
        if (tamañoArchivo < 1024) {
            return tamañoArchivo + " B";
        } else if (tamañoArchivo < 1024 * 1024) {
            return String.format("%.1f KB", tamañoArchivo / 1024.0);
        } else {
            return String.format("%.1f MB", tamañoArchivo / (1024.0 * 1024.0));
        }
    }
    
    public boolean esReporteDeVentas() {
        return "VENTAS".equals(tipoReporte);
    }
    
    public boolean esReporteDeCompras() {
        return "COMPRAS".equals(tipoReporte);
    }
    
    public boolean esReporteDeInventario() {
        return "INVENTARIO".equals(tipoReporte);
    }
    
    public boolean esReporteDeClientes() {
        return "CLIENTES".equals(tipoReporte);
    }
    
    public boolean esReporteDeProductos() {
        return "PRODUCTOS".equals(tipoReporte);
    }
}
