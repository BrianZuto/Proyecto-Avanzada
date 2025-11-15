package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Proveedor;
import com.proyectoavanzada.backend.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proveedores")
@Tag(name = "Gestión de Proveedores", description = "Endpoints para la gestión de proveedores")
public class ProveedorController {
    
    @Autowired
    private ProveedorService proveedorService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los proveedores")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosProveedores() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();
            response.put("success", true);
            response.put("data", proveedores);
            response.put("total", proveedores.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener proveedores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/activos")
    @Operation(summary = "Obtener proveedores activos")
    public ResponseEntity<Map<String, Object>> obtenerProveedoresActivos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Proveedor> proveedores = proveedorService.obtenerProveedoresActivos();
            response.put("success", true);
            response.put("data", proveedores);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener proveedores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por ID")
    public ResponseEntity<Map<String, Object>> obtenerProveedorPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            var proveedorOpt = proveedorService.obtenerProveedorPorId(id);
            if (proveedorOpt.isPresent()) {
                response.put("success", true);
                response.put("data", proveedorOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Proveedor no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener proveedor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo proveedor")
    public ResponseEntity<Map<String, Object>> crearProveedor(@Valid @RequestBody Proveedor proveedor) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar que el email sea válido si se proporciona
            if (proveedor.getEmail() != null && !proveedor.getEmail().trim().isEmpty()) {
                String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (!proveedor.getEmail().matches(emailPattern)) {
                    response.put("success", false);
                    response.put("message", "El email debe tener un formato válido");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }
            
            Proveedor proveedorGuardado = proveedorService.guardarProveedor(proveedor);
            response.put("success", true);
            response.put("message", "Proveedor creado exitosamente");
            response.put("data", proveedorGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear proveedor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

