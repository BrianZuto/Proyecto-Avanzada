package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.repository.UsuarioRepository;
import com.proyectoavanzada.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordUtil passwordUtil;
    
    /**
     * Obtiene todos los usuarios
     * @return lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    
    /**
     * Obtiene un usuario por su ID
     * @param id el ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    /**
     * Obtiene un usuario por su email (case insensitive)
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        String emailNormalizado = email.trim().toLowerCase();
        // Intentar primero con ignoreCase, si no existe, intentar con el método normal
        Optional<Usuario> usuario = usuarioRepository.findByEmailIgnoreCase(emailNormalizado);
        if (usuario.isPresent()) {
            return usuario;
        }
        return usuarioRepository.findByEmail(emailNormalizado);
    }
    
    /**
     * Guarda un nuevo usuario (encripta la contraseña automáticamente)
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    public Usuario guardarUsuario(Usuario usuario) {
        try {
            System.out.println("=== GUARDAR USUARIO ===");
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Password (antes): " + (usuario.getPassword() != null ? "***" : "null"));
            
            // Validar que el usuario tenga los campos requeridos
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("El email es obligatorio");
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("La contraseña es obligatoria");
            }
            
            // Asegurar que el email esté en minúsculas
            if (usuario.getEmail() != null) {
                usuario.setEmail(usuario.getEmail().trim().toLowerCase());
            }
            
            // Encriptar la contraseña antes de guardar
            if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
                System.out.println("Encriptando contraseña...");
                String passwordEncriptado = passwordUtil.encodePassword(usuario.getPassword());
                usuario.setPassword(passwordEncriptado);
                System.out.println("Password encriptado: " + passwordEncriptado.substring(0, Math.min(20, passwordEncriptado.length())) + "...");
            } else {
                System.out.println("Password ya está encriptado");
            }
            
            System.out.println("Guardando usuario en BD...");
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("Usuario guardado con ID: " + usuarioGuardado.getId());
            System.out.println("========================");
            
            return usuarioGuardado;
        } catch (Exception e) {
            System.err.println("ERROR en guardarUsuario:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Clase: " + e.getClass().getName());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Actualiza un usuario existente (encripta la contraseña si es necesario)
     * @param usuario el usuario con los datos actualizados
     * @return el usuario actualizado
     */
    public Usuario actualizarUsuario(Usuario usuario) {
        // Encriptar la contraseña si no está ya encriptada
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordUtil.encodePassword(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     */
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    /**
     * Verifica si existe un usuario con el email dado (case insensitive)
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existeUsuarioConEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailNormalizado = email.trim().toLowerCase();
        // Intentar primero con ignoreCase, si no existe, intentar con el método normal
        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            return true;
        }
        return usuarioRepository.existsByEmail(emailNormalizado);
    }
    
    /**
     * Obtiene todos los usuarios activos
     * @return lista de usuarios activos
     */
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivo(true);
    }
    
    /**
     * Verifica si una contraseña coincide con la del usuario
     * @param rawPassword contraseña en texto plano
     * @param encodedPassword contraseña encriptada
     * @return true si coinciden, false en caso contrario
     */
    public boolean verificarContraseña(String rawPassword, String encodedPassword) {
        System.out.println("=== VERIFICAR CONTRASEÑA ===");
        System.out.println("Raw password: " + (rawPassword != null ? "***" : "null"));
        System.out.println("Encoded password: " + (encodedPassword != null ? encodedPassword.substring(0, Math.min(20, encodedPassword.length())) + "..." : "null"));
        
        if (rawPassword == null || encodedPassword == null) {
            System.out.println("ERROR: Password o hash es null");
            return false;
        }
        if (rawPassword.trim().isEmpty() || encodedPassword.trim().isEmpty()) {
            System.out.println("ERROR: Password o hash está vacío");
            return false;
        }
        
        // Verificar si el hash tiene el formato correcto de BCrypt
        if (!encodedPassword.startsWith("$2a$") && !encodedPassword.startsWith("$2b$") && !encodedPassword.startsWith("$2y$")) {
            System.out.println("ERROR: Hash no tiene formato BCrypt válido");
            return false;
        }
        
        try {
            boolean matches = passwordUtil.matches(rawPassword, encodedPassword);
            System.out.println("Resultado verificación: " + matches);
            System.out.println("========================");
            return matches;
        } catch (Exception e) {
            System.err.println("ERROR al verificar contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Busca un usuario por email (alias para obtenerUsuarioPorEmail)
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    /**
     * Verifica una contraseña (alias para verificarContraseña)
     * @param rawPassword contraseña en texto plano
     * @param encodedPassword contraseña encriptada
     * @return true si coinciden, false en caso contrario
     */
    public boolean verificarPassword(String rawPassword, String encodedPassword) {
        return passwordUtil.matches(rawPassword, encodedPassword);
    }
    
    /**
     * Crea un nuevo usuario (alias para guardarUsuario)
     * @param usuario el usuario a crear
     * @return el usuario creado
     */
    public Usuario crearUsuario(Usuario usuario) {
        return guardarUsuario(usuario);
    }
    
    /**
     * Actualiza un usuario por ID
     * @param id el ID del usuario
     * @param usuario el usuario con los datos actualizados
     * @return el usuario actualizado
     */
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        usuario.setId(id);
        return actualizarUsuario(usuario);
    }
    
    /**
     * Obtiene usuarios por rol
     * @param rol el rol a buscar
     * @return lista de usuarios con ese rol
     */
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    /**
     * Cuenta usuarios por rol
     * @param rol el rol a contar
     * @return número de usuarios con ese rol
     */
    public long contarUsuariosPorRol(String rol) {
        return usuarioRepository.countByRol(rol);
    }
    
    /**
     * Busca usuarios por nombre
     * @param nombre el nombre a buscar
     * @return lista de usuarios que contienen ese nombre
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Encripta una contraseña
     * @param password la contraseña en texto plano
     * @return la contraseña encriptada
     */
    public String encriptarPassword(String password) {
        return passwordUtil.encodePassword(password);
    }
    
    /**
     * Obtiene un usuario por ID (versión que lanza excepción si no existe)
     * @param id el ID del usuario
     * @return el usuario encontrado
     * @throws RuntimeException si el usuario no existe
     */
    public Usuario obtenerUsuarioPorIdObligatorio(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}

