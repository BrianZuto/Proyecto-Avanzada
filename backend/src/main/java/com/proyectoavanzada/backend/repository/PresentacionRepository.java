package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Presentacion;
import com.proyectoavanzada.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresentacionRepository extends JpaRepository<Presentacion, Long> {
    
    // Buscar por producto
    List<Presentacion> findByProducto(Producto producto);
    
    // Buscar por producto y talla
    List<Presentacion> findByProductoAndTalla(Producto producto, String talla);
    
    // Buscar por producto y color
    List<Presentacion> findByProductoAndColor(Producto producto, String color);
    
    // Buscar por producto, talla y color
    Optional<Presentacion> findByProductoAndTallaAndColor(Producto producto, String talla, String color);
    
    // Buscar presentaciones activas
    List<Presentacion> findByActivoTrue();
    
    // Buscar presentaciones inactivas
    List<Presentacion> findByActivoFalse();
    
    // Buscar por talla
    List<Presentacion> findByTalla(String talla);
    
    // Buscar por color
    List<Presentacion> findByColorContainingIgnoreCase(String color);
    
    // Buscar presentaciones con stock
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true AND p.stockDisponible > 0")
    List<Presentacion> findPresentacionesConStock();
    
    // Buscar presentaciones sin stock
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true AND p.stockDisponible = 0")
    List<Presentacion> findPresentacionesSinStock();
    
    // Buscar presentaciones con stock bajo
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true AND p.stockDisponible <= p.producto.stockMinimo")
    List<Presentacion> findPresentacionesConStockBajo();
    
    // Buscar presentaciones destacadas
    List<Presentacion> findByEsDestacadoTrue();
    
    // Buscar por ubicación en almacén
    List<Presentacion> findByUbicacionAlmacen(String ubicacionAlmacen);
    
    // Buscar presentaciones con precio especial
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true AND p.precioEspecial IS NOT NULL")
    List<Presentacion> findPresentacionesConPrecioEspecial();
    
    // Buscar por rango de stock
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true AND p.stockDisponible BETWEEN :stockMin AND :stockMax")
    List<Presentacion> findByStockDisponibleBetween(@Param("stockMin") Integer stockMin, 
                                                   @Param("stockMax") Integer stockMax);
    
    // Buscar por rango de precio especial
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true AND p.precioEspecial BETWEEN :precioMin AND :precioMax")
    List<Presentacion> findByPrecioEspecialBetween(@Param("precioMin") BigDecimal precioMin, 
                                                  @Param("precioMax") BigDecimal precioMax);
    
    // Contar presentaciones activas
    long countByActivoTrue();
    
    // Contar presentaciones por producto
    long countByProducto(Producto producto);
    
    // Contar presentaciones con stock
    @Query("SELECT COUNT(p) FROM Presentacion p WHERE p.activo = true AND p.stockDisponible > 0")
    long countPresentacionesConStock();
    
    // Contar presentaciones sin stock
    @Query("SELECT COUNT(p) FROM Presentacion p WHERE p.activo = true AND p.stockDisponible = 0")
    long countPresentacionesSinStock();
    
    // Buscar tallas disponibles para un producto
    @Query("SELECT DISTINCT p.talla FROM Presentacion p WHERE p.producto = :producto AND p.activo = true ORDER BY p.talla")
    List<String> findTallasDisponiblesPorProducto(@Param("producto") Producto producto);
    
    // Buscar colores disponibles para un producto
    @Query("SELECT DISTINCT p.color FROM Presentacion p WHERE p.producto = :producto AND p.activo = true ORDER BY p.color")
    List<String> findColoresDisponiblesPorProducto(@Param("producto") Producto producto);
    
    // Buscar colores disponibles para un producto y talla
    @Query("SELECT DISTINCT p.color FROM Presentacion p WHERE p.producto = :producto AND p.talla = :talla AND p.activo = true ORDER BY p.color")
    List<String> findColoresDisponiblesPorProductoYTalla(@Param("producto") Producto producto, 
                                                        @Param("talla") String talla);
    
    // Buscar tallas disponibles para un producto y color
    @Query("SELECT DISTINCT p.talla FROM Presentacion p WHERE p.producto = :producto AND p.color = :color AND p.activo = true ORDER BY p.talla")
    List<String> findTallasDisponiblesPorProductoYColor(@Param("producto") Producto producto, 
                                                       @Param("color") String color);
    
    // Buscar presentaciones más vendidas
    @Query("SELECT p FROM Presentacion p WHERE p.activo = true ORDER BY " +
           "(SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv WHERE dv.presentacion = p) DESC")
    List<Presentacion> findPresentacionesMasVendidas();
    
    // Buscar presentaciones ordenadas por stock descendente
    List<Presentacion> findByActivoTrueOrderByStockDisponibleDesc();
    
    // Buscar presentaciones ordenadas por stock ascendente
    List<Presentacion> findByActivoTrueOrderByStockDisponibleAsc();
}
