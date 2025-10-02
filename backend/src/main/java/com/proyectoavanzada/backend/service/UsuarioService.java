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
     * Obtiene un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    /**
     * Guarda un nuevo usuario (encripta la contraseña automáticamente)
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    public Usuario guardarUsuario(Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordUtil.encodePassword(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
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
     * Verifica si existe un usuario con el email dado
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existeUsuarioConEmail(String email) {
        return usuarioRepository.existsByEmail(email);
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
        return passwordUtil.matches(rawPassword, encodedPassword);
    }
}

