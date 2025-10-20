package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Inventario;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    // Buscar por producto
    List<Inventario> findByProducto(Producto producto);
    
    // Buscar por presentación
    List<Inventario> findByPresentacion(Presentacion presentacion);
    
    // Buscar por producto y presentación
    Optional<Inventario> findByProductoAndPresentacion(Producto producto, Presentacion presentacion);
    
    // Buscar inventarios activos
    List<Inventario> findByActivoTrue();
    
    // Buscar inventarios inactivos
    List<Inventario> findByActivoFalse();
    
    // Buscar por ubicación en almacén
    List<Inventario> findByUbicacionAlmacen(String ubicacionAlmacen);
    
    // Buscar inventarios con stock
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.stockActual > 0")
    List<Inventario> findInventariosConStock();
    
    // Buscar inventarios sin stock
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.stockActual = 0")
    List<Inventario> findInventariosSinStock();
    
    // Buscar inventarios con stock bajo
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.stockActual <= i.stockMinimo")
    List<Inventario> findInventariosConStockBajo();
    
    // Buscar inventarios con stock alto
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.stockMaximo IS NOT NULL AND i.stockActual >= i.stockMaximo")
    List<Inventario> findInventariosConStockAlto();
    
    // Buscar por rango de stock
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.stockActual BETWEEN :stockMin AND :stockMax")
    List<Inventario> findByStockActualBetween(@Param("stockMin") Integer stockMin, 
                                             @Param("stockMax") Integer stockMax);
    
    // Buscar por stock mínimo
    List<Inventario> findByStockMinimo(Integer stockMinimo);
    
    // Buscar por stock máximo
    List<Inventario> findByStockMaximo(Integer stockMaximo);
    
    // Contar inventarios activos
    long countByActivoTrue();
    
    // Contar inventarios con stock
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.activo = true AND i.stockActual > 0")
    long countInventariosConStock();
    
    // Contar inventarios sin stock
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.activo = true AND i.stockActual = 0")
    long countInventariosSinStock();
    
    // Contar inventarios con stock bajo
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.activo = true AND i.stockActual <= i.stockMinimo")
    long countInventariosConStockBajo();
    
    // Sumar stock total por producto
    @Query("SELECT COALESCE(SUM(i.stockActual), 0) FROM Inventario i WHERE i.producto = :producto AND i.activo = true")
    Integer sumStockActualByProducto(@Param("producto") Producto producto);
    
    // Sumar stock total por presentación
    @Query("SELECT COALESCE(SUM(i.stockActual), 0) FROM Inventario i WHERE i.presentacion = :presentacion AND i.activo = true")
    Integer sumStockActualByPresentacion(@Param("presentacion") Presentacion presentacion);
    
    // Buscar inventarios actualizados recientemente
    @Query("SELECT i FROM Inventario i WHERE i.activo = true ORDER BY i.fechaActualizacion DESC")
    List<Inventario> findInventariosActualizadosRecientemente();
    
    // Buscar inventarios con última entrada reciente
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.fechaUltimaEntrada IS NOT NULL ORDER BY i.fechaUltimaEntrada DESC")
    List<Inventario> findInventariosConUltimaEntradaReciente();
    
    // Buscar inventarios con última salida reciente
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.fechaUltimaSalida IS NOT NULL ORDER BY i.fechaUltimaSalida DESC")
    List<Inventario> findInventariosConUltimaSalidaReciente();
    
    // Buscar inventarios por rango de fechas de última entrada
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.fechaUltimaEntrada BETWEEN :fechaInicio AND :fechaFin")
    List<Inventario> findByFechaUltimaEntradaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                    @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar inventarios por rango de fechas de última salida
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.fechaUltimaSalida BETWEEN :fechaInicio AND :fechaFin")
    List<Inventario> findByFechaUltimaSalidaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar inventarios ordenados por stock descendente
    List<Inventario> findByActivoTrueOrderByStockActualDesc();
    
    // Buscar inventarios ordenados por stock ascendente
    List<Inventario> findByActivoTrueOrderByStockActualAsc();
    
    // Buscar inventarios ordenados por ubicación
    List<Inventario> findByActivoTrueOrderByUbicacionAlmacenAsc();
    
    // Buscar productos con mayor stock
    @Query("SELECT i.producto, SUM(i.stockActual) as totalStock FROM Inventario i WHERE i.activo = true GROUP BY i.producto ORDER BY totalStock DESC")
    List<Object[]> findProductosConMayorStock();
    
    // Buscar presentaciones con mayor stock
    @Query("SELECT i.presentacion, i.stockActual FROM Inventario i WHERE i.activo = true ORDER BY i.stockActual DESC")
    List<Object[]> findPresentacionesConMayorStock();
    
    // Buscar inventarios que necesitan reposición
    @Query("SELECT i FROM Inventario i WHERE i.activo = true AND i.stockActual <= i.stockMinimo ORDER BY i.stockActual ASC")
    List<Inventario> findInventariosQueNecesitanReposicion();
}
