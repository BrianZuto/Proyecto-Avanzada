package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Marca;
import com.proyectoavanzada.backend.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "http://localhost:4200")
public class MarcaController {
    
    @Autowired
    private MarcaService marcaService;
    
    /**
     * Obtener todas las marcas
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasMarcas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerTodasLasMarcas();
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todas las marcas activas
     */
    @GetMapping("/activas")
    public ResponseEntity<Map<String, Object>> obtenerMarcasActivas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasActivas();
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas activas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas activas ordenadas por nombre
     */
    @GetMapping("/activas-ordenadas")
    public ResponseEntity<Map<String, Object>> obtenerMarcasActivasOrdenadas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasActivasOrdenadas();
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas ordenadas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marca por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerMarcaPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Marca> marcaOpt = marcaService.obtenerMarcaPorId(id);
            if (marcaOpt.isPresent()) {
                response.put("success", true);
                response.put("data", marcaOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Marca no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Buscar marcas por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarMarcasPorNombre(@RequestParam String nombre) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.buscarMarcasPorNombre(nombre);
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar marcas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas por país de origen
     */
    @GetMapping("/pais/{paisOrigen}")
    public ResponseEntity<Map<String, Object>> obtenerMarcasPorPais(@PathVariable String paisOrigen) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasPorPais(paisOrigen);
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas por país: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas por país ordenadas por nombre
     */
    @GetMapping("/pais/{paisOrigen}/ordenadas")
    public ResponseEntity<Map<String, Object>> obtenerMarcasPorPaisOrdenadas(@PathVariable String paisOrigen) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasPorPaisOrdenadas(paisOrigen);
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas por país ordenadas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas fundadas en un año específico
     */
    @GetMapping("/año/{fechaFundacion}")
    public ResponseEntity<Map<String, Object>> obtenerMarcasPorAñoFundacion(@PathVariable Integer fechaFundacion) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasPorAñoFundacion(fechaFundacion);
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas por año: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas fundadas en un rango de años
     */
    @GetMapping("/rango-años")
    public ResponseEntity<Map<String, Object>> obtenerMarcasPorRangoAños(@RequestParam Integer añoInicio, @RequestParam Integer añoFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasPorRangoAños(añoInicio, añoFin);
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas por rango de años: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas con productos
     */
    @GetMapping("/con-productos")
    public ResponseEntity<Map<String, Object>> obtenerMarcasConProductos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasConProductos();
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas con productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener marcas sin productos
     */
    @GetMapping("/sin-productos")
    public ResponseEntity<Map<String, Object>> obtenerMarcasSinProductos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Marca> marcas = marcaService.obtenerMarcasSinProductos();
            response.put("success", true);
            response.put("data", marcas);
            response.put("total", marcas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener marcas sin productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener conteo de productos por marca
     */
    @GetMapping("/conteo-productos")
    public ResponseEntity<Map<String, Object>> obtenerConteoProductosPorMarca() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> conteo = marcaService.obtenerConteoProductosPorMarca();
            response.put("success", true);
            response.put("data", conteo);
            response.put("total", conteo.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener conteo de productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear nueva marca
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearMarca(@Valid @RequestBody Marca marca) {
        Map<String, Object> response = new HashMap<>();
        try {
            Marca marcaGuardada = marcaService.guardarMarca(marca);
            response.put("success", true);
            response.put("message", "Marca creada exitosamente");
            response.put("data", marcaGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar marca
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarMarca(@PathVariable Long id, @Valid @RequestBody Marca marca) {
        Map<String, Object> response = new HashMap<>();
        try {
            marca.setId(id);
            Marca marcaActualizada = marcaService.actualizarMarca(marca);
            response.put("success", true);
            response.put("message", "Marca actualizada exitosamente");
            response.put("data", marcaActualizada);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar marca (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarMarca(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            marcaService.eliminarMarca(id);
            response.put("success", true);
            response.put("message", "Marca eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar marca permanentemente
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Map<String, Object>> eliminarMarcaPermanentemente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            marcaService.eliminarMarcaPermanentemente(id);
            response.put("success", true);
            response.put("message", "Marca eliminada permanentemente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar marca permanentemente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Activar marca
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Map<String, Object>> activarMarca(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Marca marca = marcaService.activarMarca(id);
            response.put("success", true);
            response.put("message", "Marca activada exitosamente");
            response.put("data", marca);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al activar marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Desactivar marca
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivarMarca(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Marca marca = marcaService.desactivarMarca(id);
            response.put("success", true);
            response.put("message", "Marca desactivada exitosamente");
            response.put("data", marca);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al desactivar marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Verificar si existe una marca con el nombre
     */
    @GetMapping("/verificar-nombre")
    public ResponseEntity<Map<String, Object>> verificarNombre(@RequestParam String nombre) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean existe = marcaService.existeMarcaConNombreIgnoreCase(nombre);
            response.put("success", true);
            response.put("existe", existe);
            response.put("message", existe ? "Nombre ya registrado" : "Nombre disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al verificar nombre: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener estadísticas de marcas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivas = marcaService.contarMarcasActivas();
            List<Marca> inactivas = marcaService.obtenerMarcasInactivas();
            
            response.put("success", true);
            response.put("data", Map.of(
                "totalActivas", totalActivas,
                "totalInactivas", inactivas.size()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
