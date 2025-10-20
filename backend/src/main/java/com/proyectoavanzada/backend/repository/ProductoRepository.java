package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Categoria;
import com.proyectoavanzada.backend.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar por código de producto
    Optional<Producto> findByCodigoProducto(String codigoProducto);
    
    // Buscar por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos activos
    List<Producto> findByActivoTrue();
    
    // Buscar productos inactivos
    List<Producto> findByActivoFalse();
    
    // Buscar por categoría
    List<Producto> findByCategoria(Categoria categoria);
    
    // Buscar por marca
    List<Producto> findByMarca(Marca marca);
    
    // Buscar por categoría y marca
    List<Producto> findByCategoriaAndMarca(Categoria categoria, Marca marca);
    
    // Buscar productos destacados
    List<Producto> findByEsDestacadoTrue();
    
    // Buscar productos nuevos
    List<Producto> findByEsNuevoTrue();
    
    // Buscar por género
    List<Producto> findByGenero(String genero);
    
    // Buscar por edad target
    List<Producto> findByEdadTarget(String edadTarget);
    
    // Buscar por rango de precio
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precioVenta BETWEEN :precioMin AND :precioMax")
    List<Producto> findByPrecioVentaBetween(@Param("precioMin") BigDecimal precioMin, 
                                          @Param("precioMax") BigDecimal precioMax);
    
    // Buscar productos con descuento
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.descuentoPorcentaje > 0")
    List<Producto> findProductosConDescuento();
    
    // Verificar si existe un código de producto
    boolean existsByCodigoProducto(String codigoProducto);
    
    // Contar productos activos
    long countByActivoTrue();
    
    // Contar productos por categoría
    long countByCategoria(Categoria categoria);
    
    // Contar productos por marca
    long countByMarca(Marca marca);
    
    // Buscar productos con stock bajo
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockMinimo >= " +
           "(SELECT COALESCE(SUM(pr.stockDisponible), 0) FROM Presentacion pr WHERE pr.producto = p)")
    List<Producto> findProductosConStockBajo();
    
    // Buscar productos más vendidos
    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY " +
           "(SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv WHERE dv.producto = p) DESC")
    List<Producto> findProductosMasVendidos();
    
    // Buscar productos por material
    List<Producto> findByMaterialPrincipalContainingIgnoreCase(String material);
    
    // Buscar productos por tecnología
    List<Producto> findByTecnologiaContainingIgnoreCase(String tecnologia);
    
    // Buscar productos por tipo de suela
    List<Producto> findByTipoSuelaContainingIgnoreCase(String tipoSuela);
    
    // Buscar productos creados en un rango de fechas
    @Query("SELECT p FROM Producto p WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Producto> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                            @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar productos actualizados recientemente
    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.fechaActualizacion DESC")
    List<Producto> findProductosActualizadosRecientemente();
    
    // Buscar productos por peso
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.pesoGramos BETWEEN :pesoMin AND :pesoMax")
    List<Producto> findByPesoGramosBetween(@Param("pesoMin") Integer pesoMin, 
                                         @Param("pesoMax") Integer pesoMax);
    
    // Buscar productos con garantía
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.garantiaMeses > 0")
    List<Producto> findProductosConGarantia();
    
    // Buscar productos ordenados por precio ascendente
    List<Producto> findByActivoTrueOrderByPrecioVentaAsc();
    
    // Buscar productos ordenados por precio descendente
    List<Producto> findByActivoTrueOrderByPrecioVentaDesc();
    
    // Buscar productos ordenados por nombre
    List<Producto> findByActivoTrueOrderByNombreAsc();
}
