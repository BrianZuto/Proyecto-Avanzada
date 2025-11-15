package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.MetodoPago;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.MetodoPagoService;
import com.proyectoavanzada.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("/api/metodos-pago")
@Tag(name = "Métodos de Pago", description = "Endpoints para gestionar métodos de pago de usuarios")
public class MetodoPagoController {
    
    @Autowired
    private MetodoPagoService metodoPagoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Obtiene todos los métodos de pago de un usuario
     * @param usuarioId el ID del usuario
     * @return lista de métodos de pago del usuario
     */
    @Operation(
        summary = "Obtener métodos de pago de un usuario",
        description = "Retorna todos los métodos de pago asociados a un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Métodos de pago obtenidos exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"metodosPago\": [...]}"
                )
            )
        )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerMetodosPagoPorUsuario(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<MetodoPago> metodosPago = metodoPagoService.obtenerMetodosPagoPorUsuario(usuarioId);
            response.put("success", true);
            response.put("metodosPago", metodosPago);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener métodos de pago: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtiene un método de pago por su ID
     * @param id el ID del método de pago
     * @return el método de pago encontrado
     */
    @Operation(
        summary = "Obtener método de pago por ID",
        description = "Retorna un método de pago específico por su ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerMetodoPagoPorId(
        @Parameter(description = "ID del método de pago", required = true)
        @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<MetodoPago> metodoPagoOpt = metodoPagoService.obtenerMetodoPagoPorId(id);
            if (metodoPagoOpt.isPresent()) {
                response.put("success", true);
                response.put("metodoPago", metodoPagoOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Método de pago no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener método de pago: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crea un nuevo método de pago
     * @param metodoPagoRequest datos del método de pago a crear
     * @return el método de pago creado
     */
    @Operation(
        summary = "Crear método de pago",
        description = "Crea un nuevo método de pago para un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Método de pago creado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearMetodoPago(
        @Parameter(description = "Datos del método de pago", required = true)
        @Valid @RequestBody MetodoPagoRequest metodoPagoRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(metodoPagoRequest.getUsuarioId());
            if (!usuarioOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            MetodoPago metodoPago = new MetodoPago();
            metodoPago.setUsuario(usuarioOpt.get());
            metodoPago.setTipoTarjeta(metodoPagoRequest.getTipoTarjeta());
            metodoPago.setNumeroTarjeta(metodoPagoRequest.getNumeroTarjeta());
            metodoPago.setNombreTitular(metodoPagoRequest.getNombreTitular());
            metodoPago.setFechaExpiracion(metodoPagoRequest.getFechaExpiracion());
            metodoPago.setPrincipal(metodoPagoRequest.getPrincipal() != null ? metodoPagoRequest.getPrincipal() : false);
            
            MetodoPago metodoPagoGuardado = metodoPagoService.guardarMetodoPago(metodoPago);
            
            response.put("success", true);
            response.put("message", "Método de pago creado exitosamente");
            response.put("metodoPago", metodoPagoGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear método de pago: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Actualiza un método de pago existente
     * @param id el ID del método de pago
     * @param metodoPagoRequest datos actualizados del método de pago
     * @return el método de pago actualizado
     */
    @Operation(
        summary = "Actualizar método de pago",
        description = "Actualiza los datos de un método de pago existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarMetodoPago(
        @Parameter(description = "ID del método de pago", required = true)
        @PathVariable Long id,
        @Parameter(description = "Datos actualizados del método de pago", required = true)
        @Valid @RequestBody MetodoPagoRequest metodoPagoRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<MetodoPago> metodoPagoOpt = metodoPagoService.obtenerMetodoPagoPorId(id);
            if (!metodoPagoOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Método de pago no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            MetodoPago metodoPago = metodoPagoOpt.get();
            metodoPago.setTipoTarjeta(metodoPagoRequest.getTipoTarjeta());
            if (metodoPagoRequest.getNumeroTarjeta() != null) {
                metodoPago.setNumeroTarjeta(metodoPagoRequest.getNumeroTarjeta());
            }
            metodoPago.setNombreTitular(metodoPagoRequest.getNombreTitular());
            metodoPago.setFechaExpiracion(metodoPagoRequest.getFechaExpiracion());
            if (metodoPagoRequest.getPrincipal() != null) {
                metodoPago.setPrincipal(metodoPagoRequest.getPrincipal());
            }
            
            MetodoPago metodoPagoActualizado = metodoPagoService.actualizarMetodoPago(metodoPago);
            
            response.put("success", true);
            response.put("message", "Método de pago actualizado exitosamente");
            response.put("metodoPago", metodoPagoActualizado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar método de pago: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Elimina un método de pago
     * @param id el ID del método de pago a eliminar
     * @return respuesta de confirmación
     */
    @Operation(
        summary = "Eliminar método de pago",
        description = "Elimina un método de pago del sistema"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarMetodoPago(
        @Parameter(description = "ID del método de pago", required = true)
        @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            metodoPagoService.eliminarMetodoPago(id);
            response.put("success", true);
            response.put("message", "Método de pago eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar método de pago: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Establece un método de pago como principal
     * @param id el ID del método de pago
     * @param usuarioId el ID del usuario
     * @return el método de pago actualizado
     */
    @Operation(
        summary = "Establecer método de pago como principal",
        description = "Marca un método de pago como principal y desmarca los demás"
    )
    @PutMapping("/{id}/principal")
    public ResponseEntity<Map<String, Object>> establecerComoPrincipal(
        @Parameter(description = "ID del método de pago", required = true)
        @PathVariable Long id,
        @Parameter(description = "ID del usuario", required = true)
        @RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            MetodoPago metodoPago = metodoPagoService.establecerComoPrincipal(id, usuarioId);
            response.put("success", true);
            response.put("message", "Método de pago establecido como principal");
            response.put("metodoPago", metodoPago);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al establecer método de pago como principal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Clase interna para las peticiones
    public static class MetodoPagoRequest {
        private Long usuarioId;
        private String tipoTarjeta;
        private String numeroTarjeta;
        private String nombreTitular;
        private String fechaExpiracion;
        private Boolean principal;
        
        // Getters y setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public String getTipoTarjeta() { return tipoTarjeta; }
        public void setTipoTarjeta(String tipoTarjeta) { this.tipoTarjeta = tipoTarjeta; }
        
        public String getNumeroTarjeta() { return numeroTarjeta; }
        public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
        
        public String getNombreTitular() { return nombreTitular; }
        public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
        
        public String getFechaExpiracion() { return fechaExpiracion; }
        public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
        
        public Boolean getPrincipal() { return principal; }
        public void setPrincipal(Boolean principal) { this.principal = principal; }
    }
}

