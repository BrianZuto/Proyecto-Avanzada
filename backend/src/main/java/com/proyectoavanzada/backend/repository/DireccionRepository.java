package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    
    /**
     * Busca todas las direcciones de un usuario
     * @param usuarioId el ID del usuario
     * @return lista de direcciones del usuario
     */
    @Query("SELECT d FROM Direccion d WHERE d.usuario.id = :usuarioId")
    List<Direccion> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca direcciones activas de un usuario
     * @param usuarioId el ID del usuario
     * @param activo estado de activación
     * @return lista de direcciones activas del usuario
     */
    @Query("SELECT d FROM Direccion d WHERE d.usuario.id = :usuarioId AND d.activo = :activo")
    List<Direccion> findByUsuarioIdAndActivo(@Param("usuarioId") Long usuarioId, @Param("activo") Boolean activo);
    
    /**
     * Busca una dirección por ID y usuario
     * @param id el ID de la dirección
     * @param usuarioId el ID del usuario
     * @return Optional con la dirección si existe
     */
    @Query("SELECT d FROM Direccion d WHERE d.id = :id AND d.usuario.id = :usuarioId")
    Optional<Direccion> findByIdAndUsuarioId(@Param("id") Long id, @Param("usuarioId") Long usuarioId);
    
    /**
     * Verifica si existe una dirección para un usuario
     * @param usuarioId el ID del usuario
     * @return true si existe al menos una dirección, false en caso contrario
     */
    @Query("SELECT COUNT(d) > 0 FROM Direccion d WHERE d.usuario.id = :usuarioId")
    boolean existsByUsuarioId(@Param("usuarioId") Long usuarioId);
}

