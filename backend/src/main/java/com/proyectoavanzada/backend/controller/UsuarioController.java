package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.UsuarioService;
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
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Endpoint de prueba para verificar que el controlador funciona
     * @return mensaje de confirmación
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("UsuarioController funcionando correctamente");
    }
    
    /**
     * Endpoint para verificar la conexión a la base de datos
     * @return información sobre la conexión
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crea un nuevo usuario
     * @param usuario el usuario a crear
     * @return el usuario creado
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
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
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, 
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
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
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        List<Usuario> usuariosActivos = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuariosActivos);
    }
}
