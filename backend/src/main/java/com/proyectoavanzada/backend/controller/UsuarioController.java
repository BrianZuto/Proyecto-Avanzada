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
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
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
    public ResponseEntity<Usuario> crearUsuario(
        @Parameter(description = "Datos del usuario a crear", required = true)
        @Valid @RequestBody Usuario usuario) {
        // Verificar si ya existe un usuario con ese email
        if (usuarioService.existeUsuarioConEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        Usuario usuarioCreado = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
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
    public ResponseEntity<Usuario> actualizarUsuario(
        @Parameter(description = "ID del usuario a actualizar", required = true)
        @PathVariable Long id, 
        @Parameter(description = "Datos actualizados del usuario", required = true)
        @Valid @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        
        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        usuarioActualizado.setId(id);
        Usuario usuarioActualizadoResult = usuarioService.actualizarUsuario(usuarioActualizado);
        return ResponseEntity.ok(usuarioActualizadoResult);
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
    public ResponseEntity<Void> eliminarUsuario(
        @Parameter(description = "ID del usuario a eliminar", required = true)
        @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
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
