package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el email dado
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuarios activos
     * @param activo estado de activación
     * @return lista de usuarios activos
     */
    List<Usuario> findByActivo(Boolean activo);
    
    /**
     * Busca usuarios por rol
     * @param rol el rol a buscar
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol(String rol);
    
    /**
     * Busca usuarios activos
     * @return lista de usuarios activos
     */
    List<Usuario> findByActivoTrue();
    
    /**
     * Cuenta usuarios por rol
     * @param rol el rol a contar
     * @return número de usuarios con ese rol
     */
    long countByRol(String rol);
    
    /**
     * Busca usuarios por nombre (búsqueda parcial, case insensitive)
     * @param nombre el nombre a buscar
     * @return lista de usuarios que contienen ese nombre
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}

