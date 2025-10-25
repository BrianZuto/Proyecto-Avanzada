package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Categoria;
import com.proyectoavanzada.backend.service.CategoriaService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Gestión de Categorías", description = "Endpoints para la gestión completa de categorías de productos")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    /**
     * Obtener todas las categorías
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasCategorias() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();
            response.put("success", true);
            response.put("data", categorias);
            response.put("total", categorias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener categorías: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todas las categorías activas
     */
    @GetMapping("/activas")
    public ResponseEntity<Map<String, Object>> obtenerCategoriasActivas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
            response.put("success", true);
            response.put("data", categorias);
            response.put("total", categorias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener categorías activas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener categorías activas ordenadas por nombre
     */
    @GetMapping("/activas-ordenadas")
    public ResponseEntity<Map<String, Object>> obtenerCategoriasActivasOrdenadas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasActivasOrdenadas();
            response.put("success", true);
            response.put("data", categorias);
            response.put("total", categorias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener categorías ordenadas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener categoría por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerCategoriaPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Categoria> categoriaOpt = categoriaService.obtenerCategoriaPorId(id);
            if (categoriaOpt.isPresent()) {
                response.put("success", true);
                response.put("data", categoriaOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Buscar categorías por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarCategoriasPorNombre(@RequestParam String nombre) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Categoria> categorias = categoriaService.buscarCategoriasPorNombre(nombre);
            response.put("success", true);
            response.put("data", categorias);
            response.put("total", categorias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar categorías: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener categorías con productos
     */
    @GetMapping("/con-productos")
    public ResponseEntity<Map<String, Object>> obtenerCategoriasConProductos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasConProductos();
            response.put("success", true);
            response.put("data", categorias);
            response.put("total", categorias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener categorías con productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener categorías sin productos
     */
    @GetMapping("/sin-productos")
    public ResponseEntity<Map<String, Object>> obtenerCategoriasSinProductos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasSinProductos();
            response.put("success", true);
            response.put("data", categorias);
            response.put("total", categorias.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener categorías sin productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener conteo de productos por categoría
     */
    @GetMapping("/conteo-productos")
    public ResponseEntity<Map<String, Object>> obtenerConteoProductosPorCategoria() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> conteo = categoriaService.obtenerConteoProductosPorCategoria();
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
     * Crear nueva categoría
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCategoria(@Valid @RequestBody Categoria categoria) {
        Map<String, Object> response = new HashMap<>();
        try {
            Categoria categoriaGuardada = categoriaService.guardarCategoria(categoria);
            response.put("success", true);
            response.put("message", "Categoría creada exitosamente");
            response.put("data", categoriaGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar categoría
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        Map<String, Object> response = new HashMap<>();
        try {
            categoria.setId(id);
            Categoria categoriaActualizada = categoriaService.actualizarCategoria(categoria);
            response.put("success", true);
            response.put("message", "Categoría actualizada exitosamente");
            response.put("data", categoriaActualizada);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar categoría (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCategoria(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            categoriaService.eliminarCategoria(id);
            response.put("success", true);
            response.put("message", "Categoría eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar categoría permanentemente
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Map<String, Object>> eliminarCategoriaPermanentemente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            categoriaService.eliminarCategoriaPermanentemente(id);
            response.put("success", true);
            response.put("message", "Categoría eliminada permanentemente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar categoría permanentemente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Activar categoría
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Map<String, Object>> activarCategoria(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Categoria categoria = categoriaService.activarCategoria(id);
            response.put("success", true);
            response.put("message", "Categoría activada exitosamente");
            response.put("data", categoria);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al activar categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Desactivar categoría
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivarCategoria(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Categoria categoria = categoriaService.desactivarCategoria(id);
            response.put("success", true);
            response.put("message", "Categoría desactivada exitosamente");
            response.put("data", categoria);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al desactivar categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Verificar si existe una categoría con el nombre
     */
    @GetMapping("/verificar-nombre")
    public ResponseEntity<Map<String, Object>> verificarNombre(@RequestParam String nombre) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean existe = categoriaService.existeCategoriaConNombreIgnoreCase(nombre);
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
     * Obtener estadísticas de categorías
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivas = categoriaService.contarCategoriasActivas();
            List<Categoria> inactivas = categoriaService.obtenerCategoriasInactivas();
            
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
