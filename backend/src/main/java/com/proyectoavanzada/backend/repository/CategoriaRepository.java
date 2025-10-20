package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar por nombre
    Optional<Categoria> findByNombre(String nombre);
    
    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
    
    // Buscar categorías activas
    List<Categoria> findByActivoTrue();
    
    // Buscar categorías inactivas
    List<Categoria> findByActivoFalse();
    
    // Buscar por nombre que contenga texto
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe una categoría con el nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe una categoría con el nombre ignorando mayúsculas/minúsculas
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Contar categorías activas
    long countByActivoTrue();
    
    // Buscar categorías con productos
    @Query("SELECT c FROM Categoria c WHERE c.activo = true AND SIZE(c.productos) > 0")
    List<Categoria> findCategoriasConProductos();
    
    // Buscar categorías sin productos
    @Query("SELECT c FROM Categoria c WHERE c.activo = true AND SIZE(c.productos) = 0")
    List<Categoria> findCategoriasSinProductos();
    
    // Contar productos por categoría
    @Query("SELECT c.nombre, COUNT(p) FROM Categoria c LEFT JOIN c.productos p WHERE c.activo = true GROUP BY c.id, c.nombre")
    List<Object[]> countProductosPorCategoria();
    
    // Buscar categorías ordenadas por nombre
    List<Categoria> findByActivoTrueOrderByNombreAsc();
}
