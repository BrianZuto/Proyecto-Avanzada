package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    // Buscar por RUC
    Optional<Proveedor> findByRuc(String ruc);
    
    // Buscar por nombre
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por email
    Optional<Proveedor> findByEmail(String email);
    
    // Buscar proveedores activos
    List<Proveedor> findByActivoTrue();
    
    // Buscar proveedores inactivos
    List<Proveedor> findByActivoFalse();
    
    // Buscar por ciudad
    List<Proveedor> findByCiudad(String ciudad);
    
    // Verificar si existe un RUC
    boolean existsByRuc(String ruc);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Contar proveedores activos
    long countByActivoTrue();
    
    // Buscar proveedores con compras
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true AND SIZE(p.compras) > 0")
    List<Proveedor> findProveedoresConCompras();
    
    // Buscar proveedores sin compras
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true AND SIZE(p.compras) = 0")
    List<Proveedor> findProveedoresSinCompras();
    
    // Buscar proveedores registrados en un rango de fechas
    @Query("SELECT p FROM Proveedor p WHERE p.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Proveedor> findByFechaRegistroBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                             @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar proveedores con crédito
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true AND p.limiteCredito > 0")
    List<Proveedor> findProveedoresConCredito();
    
    // Buscar proveedores por días de crédito
    List<Proveedor> findByCreditoDias(Integer creditoDias);
    
    // Buscar proveedores con límite de crédito mayor a un valor
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true AND p.limiteCredito >= :limite")
    List<Proveedor> findByLimiteCreditoGreaterThanEqual(@Param("limite") Double limite);
    
    // Contar compras por proveedor
    @Query("SELECT p.nombre, COUNT(c) FROM Proveedor p LEFT JOIN p.compras c WHERE p.activo = true GROUP BY p.id, p.nombre")
    List<Object[]> countComprasPorProveedor();
    
    // Buscar proveedores ordenados por nombre
    List<Proveedor> findByActivoTrueOrderByNombreAsc();
    
    // Buscar proveedores por ciudad ordenados por nombre
    List<Proveedor> findByCiudadOrderByNombreAsc(String ciudad);
}
