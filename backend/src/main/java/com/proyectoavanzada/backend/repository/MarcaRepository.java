package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    
    // Buscar por nombre
    Optional<Marca> findByNombre(String nombre);
    
    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<Marca> findByNombreIgnoreCase(String nombre);
    
    // Buscar marcas activas
    List<Marca> findByActivoTrue();
    
    // Buscar marcas inactivas
    List<Marca> findByActivoFalse();
    
    // Buscar por país de origen
    List<Marca> findByPaisOrigen(String paisOrigen);
    
    // Buscar por nombre que contenga texto
    List<Marca> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe una marca con el nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe una marca con el nombre ignorando mayúsculas/minúsculas
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Contar marcas activas
    long countByActivoTrue();
    
    // Buscar marcas con productos
    @Query("SELECT m FROM Marca m WHERE m.activo = true AND SIZE(m.productos) > 0")
    List<Marca> findMarcasConProductos();
    
    // Buscar marcas sin productos
    @Query("SELECT m FROM Marca m WHERE m.activo = true AND SIZE(m.productos) = 0")
    List<Marca> findMarcasSinProductos();
    
    // Contar productos por marca
    @Query("SELECT m.nombre, COUNT(p) FROM Marca m LEFT JOIN m.productos p WHERE m.activo = true GROUP BY m.id, m.nombre")
    List<Object[]> countProductosPorMarca();
    
    // Buscar marcas ordenadas por nombre
    List<Marca> findByActivoTrueOrderByNombreAsc();
    
    // Buscar marcas por país ordenadas por nombre
    List<Marca> findByPaisOrigenOrderByNombreAsc(String paisOrigen);
    
    // Buscar marcas fundadas en un año específico
    List<Marca> findByFechaFundacion(Integer fechaFundacion);
    
    // Buscar marcas fundadas en un rango de años
    @Query("SELECT m FROM Marca m WHERE m.fechaFundacion BETWEEN :añoInicio AND :añoFin ORDER BY m.fechaFundacion ASC")
    List<Marca> findByFechaFundacionBetween(@Param("añoInicio") Integer añoInicio, 
                                          @Param("añoFin") Integer añoFin);
}
