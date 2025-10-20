package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Reporte;
import com.proyectoavanzada.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    
    // Buscar por nombre
    List<Reporte> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por tipo de reporte
    List<Reporte> findByTipoReporte(String tipoReporte);
    
    // Buscar por usuario
    List<Reporte> findByUsuario(Usuario usuario);
    
    // Buscar por estado
    List<Reporte> findByEstado(String estado);
    
    // Buscar por formato
    List<Reporte> findByFormato(String formato);
    
    // Buscar reportes activos
    List<Reporte> findByActivoTrue();
    
    // Buscar reportes inactivos
    List<Reporte> findByActivoFalse();
    
    // Buscar por rango de fechas de generación
    @Query("SELECT r FROM Reporte r WHERE r.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin")
    List<Reporte> findByFechaGeneracionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                             @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar por rango de fechas del reporte
    @Query("SELECT r FROM Reporte r WHERE r.fechaInicio BETWEEN :fechaInicio AND :fechaFin OR r.fechaFin BETWEEN :fechaInicio AND :fechaFin")
    List<Reporte> findByFechaInicioOrFechaFinBetween(@Param("fechaInicio") LocalDate fechaInicio, 
                                                    @Param("fechaFin") LocalDate fechaFin);
    
    // Buscar reportes generados
    @Query("SELECT r FROM Reporte r WHERE r.estado = 'GENERADO' AND r.activo = true")
    List<Reporte> findReportesGenerados();
    
    // Buscar reportes procesando
    @Query("SELECT r FROM Reporte r WHERE r.estado = 'PROCESANDO' AND r.activo = true")
    List<Reporte> findReportesProcesando();
    
    // Buscar reportes con error
    @Query("SELECT r FROM Reporte r WHERE r.estado = 'ERROR' AND r.activo = true")
    List<Reporte> findReportesConError();
    
    // Buscar reportes con archivo
    @Query("SELECT r FROM Reporte r WHERE r.archivoUrl IS NOT NULL AND r.activo = true")
    List<Reporte> findReportesConArchivo();
    
    // Buscar reportes sin archivo
    @Query("SELECT r FROM Reporte r WHERE r.archivoUrl IS NULL AND r.activo = true")
    List<Reporte> findReportesSinArchivo();
    
    // Contar reportes por tipo
    long countByTipoReporte(String tipoReporte);
    
    // Contar reportes por estado
    long countByEstado(String estado);
    
    // Contar reportes por usuario
    long countByUsuario(Usuario usuario);
    
    // Contar reportes activos
    long countByActivoTrue();
    
    // Buscar reportes más recientes
    @Query("SELECT r FROM Reporte r WHERE r.activo = true ORDER BY r.fechaGeneracion DESC")
    List<Reporte> findReportesRecientes();
    
    // Buscar reportes más antiguos
    @Query("SELECT r FROM Reporte r WHERE r.activo = true ORDER BY r.fechaGeneracion ASC")
    List<Reporte> findReportesAntiguos();
    
    // Buscar reportes por usuario y tipo
    List<Reporte> findByUsuarioAndTipoReporte(Usuario usuario, String tipoReporte);
    
    // Buscar reportes por usuario y estado
    List<Reporte> findByUsuarioAndEstado(Usuario usuario, String estado);
    
    // Buscar reportes por tipo y estado
    List<Reporte> findByTipoReporteAndEstado(String tipoReporte, String estado);
    
    // Buscar reportes de ventas
    @Query("SELECT r FROM Reporte r WHERE r.tipoReporte = 'VENTAS' AND r.activo = true")
    List<Reporte> findReportesDeVentas();
    
    // Buscar reportes de compras
    @Query("SELECT r FROM Reporte r WHERE r.tipoReporte = 'COMPRAS' AND r.activo = true")
    List<Reporte> findReportesDeCompras();
    
    // Buscar reportes de inventario
    @Query("SELECT r FROM Reporte r WHERE r.tipoReporte = 'INVENTARIO' AND r.activo = true")
    List<Reporte> findReportesDeInventario();
    
    // Buscar reportes de clientes
    @Query("SELECT r FROM Reporte r WHERE r.tipoReporte = 'CLIENTES' AND r.activo = true")
    List<Reporte> findReportesDeClientes();
    
    // Buscar reportes de productos
    @Query("SELECT r FROM Reporte r WHERE r.tipoReporte = 'PRODUCTOS' AND r.activo = true")
    List<Reporte> findReportesDeProductos();
    
    // Buscar reportes en formato PDF
    @Query("SELECT r FROM Reporte r WHERE r.formato = 'PDF' AND r.activo = true")
    List<Reporte> findReportesEnPDF();
    
    // Buscar reportes en formato Excel
    @Query("SELECT r FROM Reporte r WHERE r.formato = 'EXCEL' AND r.activo = true")
    List<Reporte> findReportesEnExcel();
    
    // Buscar reportes en formato CSV
    @Query("SELECT r FROM Reporte r WHERE r.formato = 'CSV' AND r.activo = true")
    List<Reporte> findReportesEnCSV();
    
    // Buscar reportes con mayor tamaño de archivo
    @Query("SELECT r FROM Reporte r WHERE r.activo = true AND r.tamañoArchivo IS NOT NULL ORDER BY r.tamañoArchivo DESC")
    List<Reporte> findReportesConMayorTamañoArchivo();
    
    // Buscar reportes por usuario y rango de fechas
    @Query("SELECT r FROM Reporte r WHERE r.usuario = :usuario AND r.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin AND r.activo = true")
    List<Reporte> findByUsuarioAndFechaGeneracionBetween(@Param("usuario") Usuario usuario, 
                                                        @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                        @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar reportes por tipo y rango de fechas
    @Query("SELECT r FROM Reporte r WHERE r.tipoReporte = :tipoReporte AND r.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin AND r.activo = true")
    List<Reporte> findByTipoReporteAndFechaGeneracionBetween(@Param("tipoReporte") String tipoReporte, 
                                                            @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                            @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar usuarios más activos en generación de reportes
    @Query("SELECT r.usuario, COUNT(r) as totalReportes FROM Reporte r WHERE r.activo = true GROUP BY r.usuario ORDER BY totalReportes DESC")
    List<Object[]> findUsuariosMasActivosEnReportes();
    
    // Buscar tipos de reporte más solicitados
    @Query("SELECT r.tipoReporte, COUNT(r) as totalReportes FROM Reporte r WHERE r.activo = true GROUP BY r.tipoReporte ORDER BY totalReportes DESC")
    List<Object[]> findTiposReporteMasSolicitados();
}
