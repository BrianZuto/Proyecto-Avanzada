package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Compra;
import com.proyectoavanzada.backend.model.DetalleCompra;
import com.proyectoavanzada.backend.model.Proveedor;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.CompraService;
import com.proyectoavanzada.backend.service.ProveedorService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/compras")
@Tag(name = "Gestión de Compras", description = "Endpoints para la gestión completa de compras del sistema")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @Autowired
    private ProveedorService proveedorService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Obtener todas las compras
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasCompras() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerTodasLasCompras();
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todas las compras activas
     */
    @GetMapping("/activas")
    public ResponseEntity<Map<String, Object>> obtenerComprasActivas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerComprasActivas();
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras activas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compra por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerCompraPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Compra> compraOpt = compraService.obtenerCompraPorId(id);
            if (compraOpt.isPresent()) {
                response.put("success", true);
                response.put("data", compraOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Compra no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras por proveedor
     */
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<Map<String, Object>> obtenerComprasPorProveedor(@PathVariable Long proveedorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Proveedor> proveedorOpt = proveedorService.obtenerProveedorPorId(proveedorId);
            if (proveedorOpt.isPresent()) {
                List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorOpt.get());
                response.put("success", true);
                response.put("data", compras);
                response.put("total", compras.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Proveedor no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras por proveedor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerComprasPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (usuarioOpt.isPresent()) {
                List<Compra> compras = compraService.obtenerComprasPorUsuario(usuarioOpt.get());
                response.put("success", true);
                response.put("data", compras);
                response.put("total", compras.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras por usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerComprasPorEstado(@PathVariable String estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerComprasPorEstado(estado);
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras por estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras por rango de fechas
     */
    @GetMapping("/rango-fechas")
    public ResponseEntity<Map<String, Object>> obtenerComprasPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Compra> compras = compraService.obtenerComprasPorRangoFechas(inicio, fin);
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras por rango de fechas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<Map<String, Object>> obtenerComprasPendientes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerComprasPendientes();
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras pendientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras pagadas
     */
    @GetMapping("/pagadas")
    public ResponseEntity<Map<String, Object>> obtenerComprasPagadas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerComprasPagadas();
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras pagadas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras vencidas
     */
    @GetMapping("/vencidas")
    public ResponseEntity<Map<String, Object>> obtenerComprasVencidas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerComprasVencidas();
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras vencidas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener compras más recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<Map<String, Object>> obtenerComprasRecientes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Compra> compras = compraService.obtenerComprasRecientes();
            response.put("success", true);
            response.put("data", compras);
            response.put("total", compras.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener compras recientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear nueva compra
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCompra(@Valid @RequestBody Compra compra) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== RECIBIENDO COMPRA EN CONTROLLER ===");
            System.out.println("Proveedor ID: " + (compra.getProveedor() != null ? compra.getProveedor().getId() : "NULL"));
            System.out.println("Usuario ID: " + (compra.getUsuario() != null ? compra.getUsuario().getId() : "NULL"));
            System.out.println("Detalles recibidos: " + (compra.getDetallesCompra() != null ? compra.getDetallesCompra().size() : 0));
            if (compra.getDetallesCompra() != null && !compra.getDetallesCompra().isEmpty()) {
                for (int i = 0; i < compra.getDetallesCompra().size(); i++) {
                    DetalleCompra det = compra.getDetallesCompra().get(i);
                    System.out.println("Detalle " + i + ": Producto ID=" + (det.getProducto() != null ? det.getProducto().getId() : "NULL") + 
                                     ", Cantidad=" + det.getCantidad());
                }
            }
            
            Compra compraGuardada = compraService.crearCompra(compra);
            response.put("success", true);
            response.put("message", "Compra creada exitosamente");
            response.put("data", compraGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("ERROR en crearCompra: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al crear compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar compra
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCompra(@PathVariable Long id, @Valid @RequestBody Compra compra) {
        Map<String, Object> response = new HashMap<>();
        try {
            compra.setId(id);
            Compra compraActualizada = compraService.actualizarCompra(compra);
            response.put("success", true);
            response.put("message", "Compra actualizada exitosamente");
            response.put("data", compraActualizada);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar compra (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCompra(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            compraService.eliminarCompra(id);
            response.put("success", true);
            response.put("message", "Compra eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Marcar compra como pagada
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Map<String, Object>> marcarComoPagada(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Compra compra = compraService.marcarComoPagada(id);
            response.put("success", true);
            response.put("message", "Compra marcada como pagada");
            response.put("data", compra);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al marcar compra como pagada: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Marcar compra como cancelada
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Map<String, Object>> marcarComoCancelada(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Compra compra = compraService.marcarComoCancelada(id);
            response.put("success", true);
            response.put("message", "Compra cancelada exitosamente");
            response.put("data", compra);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cancelar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Agregar detalle a compra
     */
    @PostMapping("/{id}/detalles")
    public ResponseEntity<Map<String, Object>> agregarDetalleCompra(@PathVariable Long id, @Valid @RequestBody DetalleCompra detalleCompra) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Establecer la compra en el detalle
            Compra compra = new Compra();
            compra.setId(id);
            detalleCompra.setCompra(compra);
            
            DetalleCompra detalleGuardado = compraService.agregarDetalleCompra(detalleCompra);
            response.put("success", true);
            response.put("message", "Detalle agregado exitosamente");
            response.put("data", detalleGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al agregar detalle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Obtener detalles de compra
     */
    @GetMapping("/{id}/detalles")
    public ResponseEntity<Map<String, Object>> obtenerDetallesCompra(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DetalleCompra> detalles = compraService.obtenerDetallesCompra(id);
            response.put("success", true);
            response.put("data", detalles);
            response.put("total", detalles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener detalles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Eliminar detalle de compra
     */
    @DeleteMapping("/detalles/{detalleId}")
    public ResponseEntity<Map<String, Object>> eliminarDetalleCompra(@PathVariable Long detalleId) {
        Map<String, Object> response = new HashMap<>();
        try {
            compraService.eliminarDetalleCompra(detalleId);
            response.put("success", true);
            response.put("message", "Detalle eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar detalle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Recalcular totales de compra
     */
    @PutMapping("/{id}/recalcular")
    public ResponseEntity<Map<String, Object>> recalcularTotalesCompra(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            compraService.recalcularTotalesCompra(id);
            response.put("success", true);
            response.put("message", "Totales recalculados exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al recalcular totales: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Obtener estadísticas de compras
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivas = compraService.contarComprasActivas();
            long totalPendientes = compraService.contarComprasPorEstado("PENDIENTE");
            long totalPagadas = compraService.contarComprasPorEstado("PAGADA");
            long totalCanceladas = compraService.contarComprasPorEstado("CANCELADA");
            
            response.put("success", true);
            response.put("data", Map.of(
                "totalActivas", totalActivas,
                "totalPendientes", totalPendientes,
                "totalPagadas", totalPagadas,
                "totalCanceladas", totalCanceladas
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener total de compras en un rango de fechas
     */
    @GetMapping("/total-rango-fechas")
    public ResponseEntity<Map<String, Object>> obtenerTotalComprasPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            BigDecimal total = compraService.obtenerTotalComprasPorRangoFechas(inicio, fin);
            response.put("success", true);
            response.put("data", Map.of("total", total));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener total de compras: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
