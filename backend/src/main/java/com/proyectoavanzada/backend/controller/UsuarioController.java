package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Usuario;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Gestión de Usuarios", description = "Endpoints para la gestión completa de usuarios del sistema")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Endpoint de prueba para verificar que el controlador funciona
     * @return mensaje de confirmación
     */
    @Operation(
        summary = "Prueba del controlador",
        description = "Endpoint de prueba para verificar que el controlador de usuarios funciona correctamente"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Controlador funcionando correctamente",
        content = @Content(
            mediaType = "text/plain",
            examples = @ExampleObject(value = "UsuarioController funcionando correctamente")
        )
    )
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("UsuarioController funcionando correctamente");
    }
    
    /**
     * Endpoint para verificar la conexión a la base de datos
     * @return información sobre la conexión
     */
    @Operation(
        summary = "Prueba de conexión a base de datos",
        description = "Verifica la conexión a la base de datos y retorna información sobre los usuarios"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Conexión exitosa",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"status\": \"OK\", \"message\": \"Conexión a base de datos exitosa\", \"totalUsuarios\": 5}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error de conexión a base de datos"
        )
    })
    @GetMapping("/db-test")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            response.put("status", "OK");
            response.put("message", "Conexión a base de datos exitosa");
            response.put("totalUsuarios", usuarios.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error de conexión a base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtiene todos los usuarios
     * @return lista de usuarios
     */
    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna una lista con todos los usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosUsuarios() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            response.put("success", true);
            response.put("data", usuarios);
            response.put("total", usuarios.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener usuarios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtiene un usuario por su ID
     * @param id el ID del usuario
     * @return el usuario si existe
     */
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Retorna un usuario específico basado en su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crea un nuevo usuario
     * @param usuario el usuario a crear
     * @return el usuario creado
     */
    @Operation(
        summary = "Crear nuevo usuario",
        description = "Crea un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El email ya está registrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(
        @Parameter(description = "Datos del usuario a crear", required = true)
        @Valid @RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Verificar si ya existe un usuario con ese email
            if (usuarioService.existeUsuarioConEmail(usuario.getEmail())) {
                response.put("success", false);
                response.put("message", "El email ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            Usuario usuarioCreado = usuarioService.guardarUsuario(usuario);
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", usuarioCreado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualiza un usuario existente
     * @param id el ID del usuario
     * @param usuarioActualizado el usuario con los datos actualizados
     * @return el usuario actualizado
     */
    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
        @Parameter(description = "ID del usuario a actualizar", required = true)
        @PathVariable Long id, 
        @Parameter(description = "Datos actualizados del usuario", required = true)
        @Valid @RequestBody Usuario usuarioActualizado) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
            
            if (usuarioExistente.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Verificar si el email está siendo cambiado y si ya existe
            if (!usuarioExistente.get().getEmail().equalsIgnoreCase(usuarioActualizado.getEmail())) {
                if (usuarioService.existeUsuarioConEmail(usuarioActualizado.getEmail())) {
                    response.put("success", false);
                    response.put("message", "El email ya está registrado");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
            }
            
            usuarioActualizado.setId(id);
            // Si no se proporciona contraseña, mantener la existente
            if (usuarioActualizado.getPassword() == null || usuarioActualizado.getPassword().trim().isEmpty()) {
                usuarioActualizado.setPassword(usuarioExistente.get().getPassword());
            }
            
            Usuario usuarioActualizadoResult = usuarioService.actualizarUsuario(usuarioActualizado);
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("data", usuarioActualizadoResult);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Elimina un usuario
     * @param id el ID del usuario a eliminar
     * @return respuesta de confirmación
     */
    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuario eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(
        @Parameter(description = "ID del usuario a eliminar", required = true)
        @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            
            if (usuario.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            usuarioService.eliminarUsuario(id);
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtiene usuarios activos
     * @return lista de usuarios activos
     */
    @Operation(
        summary = "Obtener usuarios activos",
        description = "Retorna una lista con todos los usuarios activos del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios activos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        )
    })
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        List<Usuario> usuariosActivos = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuariosActivos);
    }
}
