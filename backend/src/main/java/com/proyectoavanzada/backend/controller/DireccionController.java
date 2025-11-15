package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Direccion;
import com.proyectoavanzada.backend.service.DireccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/direcciones")
@Tag(name = "Gestión de Direcciones", description = "Endpoints para la gestión de direcciones de entrega de usuarios")
public class DireccionController {
    
    @Autowired
    private DireccionService direccionService;
    
    /**
     * Obtiene todas las direcciones de un usuario
     * @param usuarioId el ID del usuario
     * @return lista de direcciones del usuario
     */
    @Operation(
        summary = "Obtener direcciones de usuario",
        description = "Retorna todas las direcciones de entrega de un usuario específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de direcciones obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Direccion.class)
            )
        )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerDireccionesPorUsuario(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Direccion> direcciones = direccionService.obtenerDireccionesActivasPorUsuario(usuarioId);
            response.put("success", true);
            response.put("direcciones", direcciones);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener direcciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtiene una dirección por su ID
     * @param id el ID de la dirección
     * @return la dirección si existe
     */
    @Operation(
        summary = "Obtener dirección por ID",
        description = "Retorna una dirección específica por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dirección encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Direccion.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Dirección no encontrada"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerDireccionPorId(
        @Parameter(description = "ID de la dirección", required = true)
        @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Direccion> direccionOpt = direccionService.obtenerDireccionPorId(id);
            if (direccionOpt.isPresent()) {
                response.put("success", true);
                response.put("direccion", direccionOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Dirección no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener dirección: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crea una nueva dirección
     * @param direccionRequest datos de la dirección a crear
     * @return la dirección creada
     */
    @Operation(
        summary = "Crear nueva dirección",
        description = "Crea una nueva dirección de entrega para un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Dirección creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Direccion.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearDireccion(
        @Parameter(description = "Datos de la dirección a crear", required = true)
        @Valid @RequestBody DireccionRequest direccionRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Direccion nuevaDireccion = new Direccion();
            nuevaDireccion.setUsuario(new com.proyectoavanzada.backend.model.Usuario());
            nuevaDireccion.getUsuario().setId(direccionRequest.getUsuarioId());
            nuevaDireccion.setDireccion(direccionRequest.getDireccion());
            nuevaDireccion.setCiudad(direccionRequest.getCiudad());
            nuevaDireccion.setDepartamento(direccionRequest.getDepartamento());
            nuevaDireccion.setCodigoPostal(direccionRequest.getCodigoPostal());
            nuevaDireccion.setPais(direccionRequest.getPais());
            nuevaDireccion.setActivo(true);
            
            Direccion direccionGuardada = direccionService.guardarDireccion(nuevaDireccion);
            
            response.put("success", true);
            response.put("message", "Dirección creada exitosamente");
            response.put("direccion", direccionGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear dirección: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Actualiza una dirección existente
     * @param id el ID de la dirección
     * @param direccionRequest datos actualizados de la dirección
     * @return la dirección actualizada
     */
    @Operation(
        summary = "Actualizar dirección",
        description = "Actualiza los datos de una dirección existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dirección actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Direccion.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Dirección no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarDireccion(
        @Parameter(description = "ID de la dirección a actualizar", required = true)
        @PathVariable Long id,
        @Parameter(description = "Datos actualizados de la dirección", required = true)
        @Valid @RequestBody DireccionRequest direccionRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Direccion> direccionOpt = direccionService.obtenerDireccionPorId(id);
            
            if (direccionOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Dirección no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Direccion direccion = direccionOpt.get();
            direccion.setDireccion(direccionRequest.getDireccion());
            direccion.setCiudad(direccionRequest.getCiudad());
            direccion.setDepartamento(direccionRequest.getDepartamento());
            direccion.setCodigoPostal(direccionRequest.getCodigoPostal());
            direccion.setPais(direccionRequest.getPais());
            
            Direccion direccionActualizada = direccionService.actualizarDireccion(direccion);
            
            response.put("success", true);
            response.put("message", "Dirección actualizada exitosamente");
            response.put("direccion", direccionActualizada);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar dirección: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Elimina una dirección
     * @param id el ID de la dirección a eliminar
     * @return respuesta de confirmación
     */
    @Operation(
        summary = "Eliminar dirección",
        description = "Elimina una dirección del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dirección eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Dirección no encontrada"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarDireccion(
        @Parameter(description = "ID de la dirección a eliminar", required = true)
        @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Direccion> direccionOpt = direccionService.obtenerDireccionPorId(id);
            
            if (direccionOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Dirección no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            direccionService.eliminarDireccion(id);
            
            response.put("success", true);
            response.put("message", "Dirección eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar dirección: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Clase interna para las peticiones
    public static class DireccionRequest {
        private Long usuarioId;
        private String direccion;
        private String ciudad;
        private String departamento;
        private String codigoPostal;
        private String pais;
        
        // Getters y setters
        public Long getUsuarioId() {
            return usuarioId;
        }
        
        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
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
        
        public String getDepartamento() {
            return departamento;
        }
        
        public void setDepartamento(String departamento) {
            this.departamento = departamento;
        }
        
        public String getCodigoPostal() {
            return codigoPostal;
        }
        
        public void setCodigoPostal(String codigoPostal) {
            this.codigoPostal = codigoPostal;
        }
        
        public String getPais() {
            return pais;
        }
        
        public void setPais(String pais) {
            this.pais = pais;
        }
    }
}

