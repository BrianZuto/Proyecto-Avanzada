package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    
    /**
     * Busca métodos de pago por usuario
     * @param usuarioId el ID del usuario
     * @return lista de métodos de pago del usuario
     */
    @Query("SELECT mp FROM MetodoPago mp WHERE mp.usuario.id = :usuarioId")
    List<MetodoPago> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca métodos de pago activos por usuario
     * @param usuarioId el ID del usuario
     * @return lista de métodos de pago activos del usuario
     */
    @Query("SELECT mp FROM MetodoPago mp WHERE mp.usuario.id = :usuarioId AND mp.activo = true")
    List<MetodoPago> findByUsuarioIdAndActivoTrue(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca el método de pago principal de un usuario
     * @param usuarioId el ID del usuario
     * @return Optional con el método de pago principal si existe
     */
    @Query("SELECT mp FROM MetodoPago mp WHERE mp.usuario.id = :usuarioId AND mp.principal = true")
    Optional<MetodoPago> findByUsuarioIdAndPrincipalTrue(@Param("usuarioId") Long usuarioId);
    
    /**
     * Verifica si existe un método de pago principal para un usuario
     * @param usuarioId el ID del usuario
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(mp) > 0 FROM MetodoPago mp WHERE mp.usuario.id = :usuarioId AND mp.principal = true")
    boolean existsByUsuarioIdAndPrincipalTrue(@Param("usuarioId") Long usuarioId);
}

