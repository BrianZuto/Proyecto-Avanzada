package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Reporte;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReporteService {
    
    @Autowired
    private ReporteRepository reporteRepository;
    
    /**
     * Obtener todos los reportes
     */
    public List<Reporte> obtenerTodosLosReportes() {
        return reporteRepository.findAll();
    }
    
    /**
     * Obtener todos los reportes activos
     */
    public List<Reporte> obtenerReportesActivos() {
        return reporteRepository.findByActivoTrue();
    }
    
    /**
     * Obtener reporte por ID
     */
    public Optional<Reporte> obtenerReportePorId(Long id) {
        return reporteRepository.findById(id);
    }
    
    /**
     * Obtener reportes por tipo
     */
    public List<Reporte> obtenerReportesPorTipo(String tipoReporte) {
        return reporteRepository.findByTipoReporte(tipoReporte);
    }
    
    /**
     * Obtener reportes por usuario
     */
    public List<Reporte> obtenerReportesPorUsuario(Usuario usuario) {
        return reporteRepository.findByUsuario(usuario);
    }
    
    /**
     * Obtener reportes por estado
     */
    public List<Reporte> obtenerReportesPorEstado(String estado) {
        return reporteRepository.findByEstado(estado);
    }
    
    /**
     * Obtener reportes por formato
     */
    public List<Reporte> obtenerReportesPorFormato(String formato) {
        return reporteRepository.findByFormato(formato);
    }
    
    /**
     * Obtener reportes por rango de fechas
     */
    public List<Reporte> obtenerReportesPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return reporteRepository.findByFechaGeneracionBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener reportes más recientes
     */
    public List<Reporte> obtenerReportesRecientes() {
        return reporteRepository.findReportesRecientes();
    }
    
    /**
     * Guardar reporte
     */
    public Reporte guardarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }
    
    /**
     * Actualizar reporte
     */
    public Reporte actualizarReporte(Reporte reporte) {
        if (!reporteRepository.existsById(reporte.getId())) {
            throw new RuntimeException("Reporte no encontrado");
        }
        return reporteRepository.save(reporte);
    }
    
    /**
     * Eliminar reporte (soft delete)
     */
    public void eliminarReporte(Long id) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(id);
        if (reporteOpt.isPresent()) {
            Reporte reporte = reporteOpt.get();
            reporte.setActivo(false);
            reporteRepository.save(reporte);
        } else {
            throw new RuntimeException("Reporte no encontrado");
        }
    }
    
    /**
     * Generar reporte de ventas
     */
    public Reporte generarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, Usuario usuario) {
        Reporte reporte = new Reporte();
        reporte.setNombre("Reporte de Ventas - " + fechaInicio + " a " + fechaFin);
        reporte.setDescripcion("Reporte de ventas del período especificado");
        reporte.setTipoReporte("VENTAS");
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setUsuario(usuario);
        reporte.setEstado("GENERADO");
        reporte.setFormato("JSON");
        
        // Aquí se implementaría la lógica para generar el reporte
        // Por ahora, solo guardamos la estructura básica
        reporte.setResultados("{\"mensaje\": \"Reporte de ventas generado\"}");
        
        return reporteRepository.save(reporte);
    }
    
    /**
     * Generar reporte de compras
     */
    public Reporte generarReporteCompras(LocalDate fechaInicio, LocalDate fechaFin, Usuario usuario) {
        Reporte reporte = new Reporte();
        reporte.setNombre("Reporte de Compras - " + fechaInicio + " a " + fechaFin);
        reporte.setDescripcion("Reporte de compras del período especificado");
        reporte.setTipoReporte("COMPRAS");
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setUsuario(usuario);
        reporte.setEstado("GENERADO");
        reporte.setFormato("JSON");
        
        // Aquí se implementaría la lógica para generar el reporte
        // Por ahora, solo guardamos la estructura básica
        reporte.setResultados("{\"mensaje\": \"Reporte de compras generado\"}");
        
        return reporteRepository.save(reporte);
    }
    
    /**
     * Generar reporte de inventario
     */
    public Reporte generarReporteInventario(Usuario usuario) {
        Reporte reporte = new Reporte();
        reporte.setNombre("Reporte de Inventario - " + LocalDate.now());
        reporte.setDescripcion("Reporte de estado actual del inventario");
        reporte.setTipoReporte("INVENTARIO");
        reporte.setUsuario(usuario);
        reporte.setEstado("GENERADO");
        reporte.setFormato("JSON");
        
        // Aquí se implementaría la lógica para generar el reporte
        // Por ahora, solo guardamos la estructura básica
        reporte.setResultados("{\"mensaje\": \"Reporte de inventario generado\"}");
        
        return reporteRepository.save(reporte);
    }
    
    /**
     * Contar reportes activos
     */
    public long contarReportesActivos() {
        return reporteRepository.countByActivoTrue();
    }
    
    /**
     * Contar reportes por estado
     */
    public long contarReportesPorEstado(String estado) {
        return reporteRepository.countByEstado(estado);
    }
    
    /**
     * Contar reportes por tipo
     */
    public long contarReportesPorTipo(String tipoReporte) {
        return reporteRepository.countByTipoReporte(tipoReporte);
    }
    
    /**
     * Contar reportes por usuario
     */
    public long contarReportesPorUsuario(Usuario usuario) {
        return reporteRepository.countByUsuario(usuario);
    }
}
