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

