package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Venta;
import com.proyectoavanzada.backend.model.DetalleVenta;
import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.VentaService;
import com.proyectoavanzada.backend.service.ClienteService;
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
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Gestión de Ventas", description = "Endpoints para la gestión completa de ventas del sistema")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Obtener todas las ventas
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasVentas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.obtenerTodasLasVentas();
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todas las ventas activas
     */
    @GetMapping("/activas")
    public ResponseEntity<Map<String, Object>> obtenerVentasActivas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.obtenerVentasActivas();
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas activas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener venta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerVentaPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Venta> ventaOpt = ventaService.obtenerVentaPorId(id);
            if (ventaOpt.isPresent()) {
                response.put("success", true);
                response.put("data", ventaOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Venta no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener ventas por cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Map<String, Object>> obtenerVentasPorCliente(@PathVariable Long clienteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Cliente> clienteOpt = clienteService.obtenerClientePorId(clienteId);
            if (clienteOpt.isPresent()) {
                List<Venta> ventas = ventaService.obtenerVentasPorCliente(clienteOpt.get());
                response.put("success", true);
                response.put("data", ventas);
                response.put("total", ventas.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Cliente no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas por cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener ventas por usuario vendedor
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerVentasPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (usuarioOpt.isPresent()) {
                List<Venta> ventas = ventaService.obtenerVentasPorUsuario(usuarioOpt.get());
                response.put("success", true);
                response.put("data", ventas);
                response.put("total", ventas.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas por usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener ventas por rango de fechas
     */
    @GetMapping("/rango-fechas")
    public ResponseEntity<Map<String, Object>> obtenerVentasPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Venta> ventas = ventaService.obtenerVentasPorRangoFechas(inicio, fin);
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas por rango de fechas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener ventas completadas
     */
    @GetMapping("/completadas")
    public ResponseEntity<Map<String, Object>> obtenerVentasCompletadas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.obtenerVentasCompletadas();
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas completadas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener ventas más recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<Map<String, Object>> obtenerVentasRecientes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.obtenerVentasRecientes();
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener ventas recientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener clientes más frecuentes
     */
    @GetMapping("/clientes-frecuentes")
    public ResponseEntity<Map<String, Object>> obtenerClientesMasFrecuentes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> clientes = ventaService.obtenerClientesMasFrecuentes();
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener clientes frecuentes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener vendedores más activos
     */
    @GetMapping("/vendedores-activos")
    public ResponseEntity<Map<String, Object>> obtenerVendedoresMasActivos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> vendedores = ventaService.obtenerVendedoresMasActivos();
            response.put("success", true);
            response.put("data", vendedores);
            response.put("total", vendedores.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener vendedores activos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear nueva venta
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearVenta(@Valid @RequestBody Venta venta) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta ventaGuardada = ventaService.crearVenta(venta);
            response.put("success", true);
            response.put("message", "Venta creada exitosamente");
            response.put("data", ventaGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar venta
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarVenta(@PathVariable Long id, @Valid @RequestBody Venta venta) {
        Map<String, Object> response = new HashMap<>();
        try {
            venta.setId(id);
            Venta ventaActualizada = ventaService.actualizarVenta(venta);
            response.put("success", true);
            response.put("message", "Venta actualizada exitosamente");
            response.put("data", ventaActualizada);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar venta (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarVenta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            ventaService.eliminarVenta(id);
            response.put("success", true);
            response.put("message", "Venta eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Marcar venta como completada
     */
    @PutMapping("/{id}/completar")
    public ResponseEntity<Map<String, Object>> marcarComoCompletada(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta venta = ventaService.marcarComoCompletada(id);
            response.put("success", true);
            response.put("message", "Venta completada exitosamente");
            response.put("data", venta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al completar venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Marcar venta como cancelada
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Map<String, Object>> marcarComoCancelada(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta venta = ventaService.marcarComoCancelada(id);
            response.put("success", true);
            response.put("message", "Venta cancelada exitosamente");
            response.put("data", venta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cancelar venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Marcar venta como devuelta
     */
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Map<String, Object>> marcarComoDevuelta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta venta = ventaService.marcarComoDevuelta(id);
            response.put("success", true);
            response.put("message", "Venta marcada como devuelta");
            response.put("data", venta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al marcar venta como devuelta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Agregar detalle a venta
     */
    @PostMapping("/{id}/detalles")
    public ResponseEntity<Map<String, Object>> agregarDetalleVenta(@PathVariable Long id, @Valid @RequestBody DetalleVenta detalleVenta) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Establecer la venta en el detalle
            Venta venta = new Venta();
            venta.setId(id);
            detalleVenta.setVenta(venta);
            
            DetalleVenta detalleGuardado = ventaService.agregarDetalleVenta(detalleVenta);
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
     * Obtener detalles de venta
     */
    @GetMapping("/{id}/detalles")
    public ResponseEntity<Map<String, Object>> obtenerDetallesVenta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DetalleVenta> detalles = ventaService.obtenerDetallesVenta(id);
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
     * Eliminar detalle de venta
     */
    @DeleteMapping("/detalles/{detalleId}")
    public ResponseEntity<Map<String, Object>> eliminarDetalleVenta(@PathVariable Long detalleId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ventaService.eliminarDetalleVenta(detalleId);
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
     * Aplicar descuento por puntos de fidelidad
     */
    @PutMapping("/{id}/aplicar-puntos")
    public ResponseEntity<Map<String, Object>> aplicarDescuentoPorPuntos(@PathVariable Long id, @RequestParam Integer puntosUsar) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta venta = ventaService.aplicarDescuentoPorPuntos(id, puntosUsar);
            response.put("success", true);
            response.put("message", "Descuento por puntos aplicado exitosamente");
            response.put("data", venta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al aplicar descuento por puntos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Obtener estadísticas de ventas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivas = ventaService.contarVentasActivas();
            long totalCompletadas = ventaService.contarVentasPorEstado("COMPLETADA");
            long totalCanceladas = ventaService.contarVentasPorEstado("CANCELADA");
            
            response.put("success", true);
            response.put("data", Map.of(
                "totalActivas", totalActivas,
                "totalCompletadas", totalCompletadas,
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
     * Obtener total de ventas en un rango de fechas
     */
    @GetMapping("/total-rango-fechas")
    public ResponseEntity<Map<String, Object>> obtenerTotalVentasPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            BigDecimal total = ventaService.obtenerTotalVentasPorRangoFechas(inicio, fin);
            response.put("success", true);
            response.put("data", Map.of("total", total));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener total de ventas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
