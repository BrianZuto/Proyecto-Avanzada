package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.UsuarioService;
import com.proyectoavanzada.backend.security.JwtService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * Endpoint para login de usuarios
     * @param loginRequest datos de login (email y password)
     * @return respuesta con token o error
     */
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\n  \"success\": true,\n  \"message\": \"Login exitoso\",\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n  \"user\": {\n    \"id\": 1,\n    \"nombre\": \"Usuario\",\n    \"email\": \"usuario@email.com\",\n    \"activo\": true,\n    \"rol\": \"Usuario\"\n  }\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"message\": \"Usuario no encontrado\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
        @Parameter(description = "Datos de login del usuario", required = true)
        @Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar usuario por email
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorEmail(loginRequest.getEmail());
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Verificar contraseña usando BCrypt
                if (usuarioService.verificarContraseña(loginRequest.getPassword(), usuario.getPassword())) {
                    if (usuario.getActivo()) {
                        // Login exitoso
                        response.put("success", true);
                        response.put("message", "Login exitoso");
                        response.put("user", createUserResponse(usuario));
                        String token = jwtService.generateToken(usuario.getEmail());
                        response.put("token", token);
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("success", false);
                        response.put("message", "Usuario inactivo");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                    }
                } else {
                    response.put("success", false);
                    response.put("message", "Contraseña incorrecta");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para registro de usuarios
     * @param registerRequest datos de registro
     * @return respuesta con usuario creado o error
     */
    @Operation(
        summary = "Registrar usuario",
        description = "Crea un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"success\": true,\n  \"message\": \"Usuario registrado exitosamente\",\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n  \"user\": {\n    \"id\": 1,\n    \"nombre\": \"Usuario\",\n    \"email\": \"usuario@email.com\",\n    \"activo\": true,\n    \"rol\": \"Usuario\"\n  }\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El email ya está registrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"message\": \"El email ya está registrado\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
        @Parameter(description = "Datos de registro del usuario", required = true)
        @Valid @RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar si el email ya existe
            if (usuarioService.existeUsuarioConEmail(registerRequest.getEmail())) {
                response.put("success", false);
                response.put("message", "El email ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(registerRequest.getNombre());
            nuevoUsuario.setEmail(registerRequest.getEmail());
            nuevoUsuario.setPassword(registerRequest.getPassword()); // Se encriptará automáticamente en guardarUsuario()
            nuevoUsuario.setActivo(true);
            
            // Guardar usuario
            Usuario usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
            
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("user", createUserResponse(usuarioGuardado));
            String token = jwtService.generateToken(usuarioGuardado.getEmail());
            response.put("token", token);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint temporal para generar hash BCrypt de una contraseña
     * @param passwordRequest contraseña a encriptar
     * @return respuesta con el hash generado
     */
    @Operation(
        summary = "Generar hash de contraseña",
        description = "Genera un hash BCrypt para una contraseña (endpoint temporal para desarrollo)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Hash generado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"originalPassword\": \"password123\", \"hashedPassword\": \"$2a$10$...\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al generar hash"
        )
    })
    @PostMapping("/generate-hash")
    public ResponseEntity<Map<String, Object>> generatePasswordHash(
        @Parameter(description = "Contraseña a encriptar", required = true)
        @RequestBody PasswordHashRequest passwordRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String hashedPassword = usuarioService.verificarContraseña(passwordRequest.getPassword(), "dummy") ? 
                "Error: No se puede generar hash" : 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(passwordRequest.getPassword());
            
            response.put("success", true);
            response.put("originalPassword", passwordRequest.getPassword());
            response.put("hashedPassword", hashedPassword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al generar hash: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para verificar si un email ya existe
     * @param emailRequest email a verificar
     * @return respuesta con si existe o no
     */
    @Operation(
        summary = "Verificar email",
        description = "Verifica si un email ya está registrado en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Verificación completada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"exists\": false, \"message\": \"Email disponible\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al verificar email"
        )
    })
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailExists(
        @Parameter(description = "Email a verificar", required = true)
        @Valid @RequestBody EmailCheckRequest emailRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean exists = usuarioService.existeUsuarioConEmail(emailRequest.getEmail());
            response.put("exists", exists);
            response.put("message", exists ? "Email ya registrado" : "Email disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exists", false);
            response.put("message", "Error al verificar email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para obtener el perfil completo del usuario
     * @param userId ID del usuario
     * @return respuesta con el perfil completo
     */
    @Operation(
        summary = "Obtener perfil de usuario",
        description = "Obtiene el perfil completo de un usuario por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Perfil obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"user\": {\"id\": 1, \"nombre\": \"Usuario\", \"email\": \"usuario@email.com\", \"telefono\": \"123456789\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al obtener perfil"
        )
    })
    @GetMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> getProfile(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(userId);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                response.put("success", true);
                response.put("user", createFullUserResponse(usuario));
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener perfil");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para actualizar el perfil del usuario
     * @param updateRequest datos a actualizar
     * @return respuesta con el usuario actualizado
     */
    @Operation(
        summary = "Actualizar perfil de usuario",
        description = "Actualiza los datos del perfil de un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Perfil actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Perfil actualizado exitosamente\", \"user\": {\"id\": 1, \"nombre\": \"Usuario Actualizado\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al actualizar perfil"
        )
    })
    @PutMapping("/update-profile")
    public ResponseEntity<Map<String, Object>> updateProfile(
        @Parameter(description = "Datos a actualizar del usuario", required = true)
        @Valid @RequestBody UpdateProfileRequest updateRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(updateRequest.getId());
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Actualizar solo los campos permitidos
                if (updateRequest.getNombre() != null) {
                    usuario.setNombre(updateRequest.getNombre());
                }
                if (updateRequest.getTelefono() != null) {
                    usuario.setTelefono(updateRequest.getTelefono());
                }
                if (updateRequest.getFechaNacimiento() != null) {
                    usuario.setFechaNacimiento(updateRequest.getFechaNacimiento());
                }
                
                Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario);
                
                response.put("success", true);
                response.put("message", "Perfil actualizado exitosamente");
                response.put("user", createFullUserResponse(usuarioActualizado));
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar perfil");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear respuesta de usuario sin información sensible (para login)
     */
    private Map<String, Object> createUserResponse(Usuario usuario) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", usuario.getId());
        userResponse.put("nombre", usuario.getNombre());
        userResponse.put("email", usuario.getEmail());
        userResponse.put("activo", usuario.getActivo());
        userResponse.put("fechaCreacion", usuario.getFechaCreacion());
        userResponse.put("rol", usuario.getRol() != null ? usuario.getRol() : "Usuario");
        return userResponse;
    }

    /**
     * Crear respuesta completa de usuario (para perfil)
     */
    private Map<String, Object> createFullUserResponse(Usuario usuario) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", usuario.getId());
        userResponse.put("nombre", usuario.getNombre());
        userResponse.put("email", usuario.getEmail());
        userResponse.put("telefono", usuario.getTelefono());
        userResponse.put("fechaNacimiento", usuario.getFechaNacimiento());
        userResponse.put("activo", usuario.getActivo());
        userResponse.put("fechaCreacion", usuario.getFechaCreacion());
        userResponse.put("rol", usuario.getRol() != null ? usuario.getRol() : "Usuario");
        return userResponse;
    }
    
    // Clases internas para las peticiones
    public static class LoginRequest {
        private String email;
        private String password;
        
        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class RegisterRequest {
        private String nombre;
        private String email;
        private String password;
        private LocalDate fechaNacimiento;
        private String telefono;
        
        // Getters y setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public LocalDate getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }
    
    public static class EmailCheckRequest {
        private String email;
        
        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    public static class PasswordHashRequest {
        private String password;
        
        // Getters y setters
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class UpdateProfileRequest {
        private Long id;
        private String nombre;
        private String telefono;
        private java.time.LocalDate fechaNacimiento;
        
        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        
        public java.time.LocalDate getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(java.time.LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    }
}
