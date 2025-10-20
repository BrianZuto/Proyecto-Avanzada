package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar por email
    Optional<Cliente> findByEmail(String email);
    
    // Buscar por número de documento
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
    
    // Buscar por nombre o apellido
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
    
    // Buscar clientes activos
    List<Cliente> findByActivoTrue();
    
    // Buscar clientes inactivos
    List<Cliente> findByActivoFalse();
    
    // Buscar por ciudad
    List<Cliente> findByCiudad(String ciudad);
    
    // Buscar clientes con puntos de fidelidad
    List<Cliente> findByPuntosFidelidadGreaterThan(Integer puntos);
    
    // Buscar clientes registrados en un rango de fechas
    @Query("SELECT c FROM Cliente c WHERE c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Cliente> findByFechaRegistroBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                           @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar clientes por tipo de documento
    List<Cliente> findByTipoDocumento(String tipoDocumento);
    
    // Contar clientes activos
    long countByActivoTrue();
    
    // Contar clientes por ciudad
    long countByCiudad(String ciudad);
    
    // Buscar clientes con más puntos de fidelidad
    @Query("SELECT c FROM Cliente c WHERE c.activo = true ORDER BY c.puntosFidelidad DESC")
    List<Cliente> findTopClientesByPuntosFidelidad();
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Verificar si existe un número de documento
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    // Buscar clientes que no han comprado en un período
    @Query("SELECT c FROM Cliente c WHERE c.id NOT IN " +
           "(SELECT DISTINCT v.cliente.id FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin)")
    List<Cliente> findClientesSinComprasEnPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                 @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar por teléfono
    Optional<Cliente> findByTelefono(String telefono);
    
    // Buscar por nombre (búsqueda parcial, case insensitive)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
