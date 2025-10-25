package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Reporte;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.ReporteService;
import com.proyectoavanzada.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Gestión de Reportes", description = "Endpoints para la generación y gestión de reportes del sistema")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Obtener todos los reportes
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosReportes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reporte> reportes = reporteService.obtenerTodosLosReportes();
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todos los reportes activos
     */
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerReportesActivos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reporte> reportes = reporteService.obtenerReportesActivos();
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes activos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reporte por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerReportePorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Reporte> reporteOpt = reporteService.obtenerReportePorId(id);
            if (reporteOpt.isPresent()) {
                response.put("success", true);
                response.put("data", reporteOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Reporte no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reporte: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reportes por tipo
     */
    @GetMapping("/tipo/{tipoReporte}")
    public ResponseEntity<Map<String, Object>> obtenerReportesPorTipo(@PathVariable String tipoReporte) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reporte> reportes = reporteService.obtenerReportesPorTipo(tipoReporte);
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes por tipo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reportes por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerReportesPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (usuarioOpt.isPresent()) {
                List<Reporte> reportes = reporteService.obtenerReportesPorUsuario(usuarioOpt.get());
                response.put("success", true);
                response.put("data", reportes);
                response.put("total", reportes.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes por usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reportes por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerReportesPorEstado(@PathVariable String estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reporte> reportes = reporteService.obtenerReportesPorEstado(estado);
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes por estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reportes por formato
     */
    @GetMapping("/formato/{formato}")
    public ResponseEntity<Map<String, Object>> obtenerReportesPorFormato(@PathVariable String formato) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reporte> reportes = reporteService.obtenerReportesPorFormato(formato);
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes por formato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reportes por rango de fechas
     */
    @GetMapping("/rango-fechas")
    public ResponseEntity<Map<String, Object>> obtenerReportesPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Reporte> reportes = reporteService.obtenerReportesPorRangoFechas(inicio, fin);
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes por rango de fechas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener reportes más recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<Map<String, Object>> obtenerReportesRecientes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reporte> reportes = reporteService.obtenerReportesRecientes();
            response.put("success", true);
            response.put("data", reportes);
            response.put("total", reportes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener reportes recientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear nuevo reporte
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearReporte(@Valid @RequestBody Reporte reporte) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reporte reporteGuardado = reporteService.guardarReporte(reporte);
            response.put("success", true);
            response.put("message", "Reporte creado exitosamente");
            response.put("data", reporteGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear reporte: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar reporte
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarReporte(@PathVariable Long id, @Valid @RequestBody Reporte reporte) {
        Map<String, Object> response = new HashMap<>();
        try {
            reporte.setId(id);
            Reporte reporteActualizado = reporteService.actualizarReporte(reporte);
            response.put("success", true);
            response.put("message", "Reporte actualizado exitosamente");
            response.put("data", reporteActualizado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar reporte: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar reporte (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarReporte(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            reporteService.eliminarReporte(id);
            response.put("success", true);
            response.put("message", "Reporte eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar reporte: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Generar reporte de ventas
     */
    @PostMapping("/generar/ventas")
    public ResponseEntity<Map<String, Object>> generarReporteVentas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            
            if (usuarioOpt.isPresent()) {
                Reporte reporte = reporteService.generarReporteVentas(inicio, fin, usuarioOpt.get());
                response.put("success", true);
                response.put("message", "Reporte de ventas generado exitosamente");
                response.put("data", reporte);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al generar reporte de ventas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Generar reporte de compras
     */
    @PostMapping("/generar/compras")
    public ResponseEntity<Map<String, Object>> generarReporteCompras(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            
            if (usuarioOpt.isPresent()) {
                Reporte reporte = reporteService.generarReporteCompras(inicio, fin, usuarioOpt.get());
                response.put("success", true);
                response.put("message", "Reporte de compras generado exitosamente");
                response.put("data", reporte);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al generar reporte de compras: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Generar reporte de inventario
     */
    @PostMapping("/generar/inventario")
    public ResponseEntity<Map<String, Object>> generarReporteInventario(@RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            
            if (usuarioOpt.isPresent()) {
                Reporte reporte = reporteService.generarReporteInventario(usuarioOpt.get());
                response.put("success", true);
                response.put("message", "Reporte de inventario generado exitosamente");
                response.put("data", reporte);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al generar reporte de inventario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener estadísticas de reportes
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivos = reporteService.contarReportesActivos();
            long totalGenerados = reporteService.contarReportesPorEstado("GENERADO");
            long totalProcesando = reporteService.contarReportesPorEstado("PROCESANDO");
            long totalConError = reporteService.contarReportesPorEstado("ERROR");
            
            response.put("success", true);
            response.put("data", Map.of(
                "totalActivos", totalActivos,
                "totalGenerados", totalGenerados,
                "totalProcesando", totalProcesando,
                "totalConError", totalConError
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
