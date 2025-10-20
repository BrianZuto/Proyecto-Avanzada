package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.DetalleVenta;
import com.proyectoavanzada.backend.model.Venta;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
    // Buscar por venta
    List<DetalleVenta> findByVenta(Venta venta);
    
    // Buscar por producto
    List<DetalleVenta> findByProducto(Producto producto);
    
    // Buscar por presentación
    List<DetalleVenta> findByPresentacion(Presentacion presentacion);
    
    // Buscar por venta y producto
    List<DetalleVenta> findByVentaAndProducto(Venta venta, Producto producto);
    
    // Buscar por venta y presentación
    List<DetalleVenta> findByVentaAndPresentacion(Venta venta, Presentacion presentacion);
    
    // Buscar por cantidad
    List<DetalleVenta> findByCantidad(Integer cantidad);
    
    // Buscar por rango de cantidad
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.cantidad BETWEEN :cantidadMin AND :cantidadMax")
    List<DetalleVenta> findByCantidadBetween(@Param("cantidadMin") Integer cantidadMin, 
                                            @Param("cantidadMax") Integer cantidadMax);
    
    // Buscar por rango de precio unitario
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.precioUnitario BETWEEN :precioMin AND :precioMax")
    List<DetalleVenta> findByPrecioUnitarioBetween(@Param("precioMin") BigDecimal precioMin, 
                                                   @Param("precioMax") BigDecimal precioMax);
    
    // Buscar por rango de subtotal
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.subtotal BETWEEN :subtotalMin AND :subtotalMax")
    List<DetalleVenta> findBySubtotalBetween(@Param("subtotalMin") BigDecimal subtotalMin, 
                                            @Param("subtotalMax") BigDecimal subtotalMax);
    
    // Contar detalles por venta
    long countByVenta(Venta venta);
    
    // Contar detalles por producto
    long countByProducto(Producto producto);
    
    // Contar detalles por presentación
    long countByPresentacion(Presentacion presentacion);
    
    // Sumar cantidad total por producto
    @Query("SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv WHERE dv.producto = :producto")
    Integer sumCantidadByProducto(@Param("producto") Producto producto);
    
    // Sumar cantidad total por presentación
    @Query("SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv WHERE dv.presentacion = :presentacion")
    Integer sumCantidadByPresentacion(@Param("presentacion") Presentacion presentacion);
    
    // Sumar subtotal por venta
    @Query("SELECT COALESCE(SUM(dv.subtotal), 0) FROM DetalleVenta dv WHERE dv.venta = :venta")
    BigDecimal sumSubtotalByVenta(@Param("venta") Venta venta);
    
    // Sumar subtotal por producto
    @Query("SELECT COALESCE(SUM(dv.subtotal), 0) FROM DetalleVenta dv WHERE dv.producto = :producto")
    BigDecimal sumSubtotalByProducto(@Param("producto") Producto producto);
    
    // Buscar detalles con mayor cantidad
    @Query("SELECT dv FROM DetalleVenta dv ORDER BY dv.cantidad DESC")
    List<DetalleVenta> findDetallesConMayorCantidad();
    
    // Buscar detalles con mayor subtotal
    @Query("SELECT dv FROM DetalleVenta dv ORDER BY dv.subtotal DESC")
    List<DetalleVenta> findDetallesConMayorSubtotal();
    
    // Buscar detalles con mayor precio unitario
    @Query("SELECT dv FROM DetalleVenta dv ORDER BY dv.precioUnitario DESC")
    List<DetalleVenta> findDetallesConMayorPrecioUnitario();
    
    // Buscar productos más vendidos
    @Query("SELECT dv.producto, SUM(dv.cantidad) as totalCantidad, COALESCE(SUM(dv.subtotal), 0) as totalVendido FROM DetalleVenta dv GROUP BY dv.producto ORDER BY totalCantidad DESC")
    List<Object[]> findProductosMasVendidos();
    
    // Buscar presentaciones más vendidas
    @Query("SELECT dv.presentacion, SUM(dv.cantidad) as totalCantidad, COALESCE(SUM(dv.subtotal), 0) as totalVendido FROM DetalleVenta dv GROUP BY dv.presentacion ORDER BY totalCantidad DESC")
    List<Object[]> findPresentacionesMasVendidas();
    
    // Buscar detalles por venta ordenados por subtotal descendente
    List<DetalleVenta> findByVentaOrderBySubtotalDesc(Venta venta);
    
    // Buscar detalles por venta ordenados por cantidad descendente
    List<DetalleVenta> findByVentaOrderByCantidadDesc(Venta venta);
    
    // Buscar productos más vendidos en un rango de fechas
    @Query("SELECT dv.producto, SUM(dv.cantidad) as totalCantidad FROM DetalleVenta dv WHERE dv.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin GROUP BY dv.producto ORDER BY totalCantidad DESC")
    List<Object[]> findProductosMasVendidosEnPeriodo(@Param("fechaInicio") java.time.LocalDateTime fechaInicio, 
                                                    @Param("fechaFin") java.time.LocalDateTime fechaFin);
    
    // Buscar presentaciones más vendidas en un rango de fechas
    @Query("SELECT dv.presentacion, SUM(dv.cantidad) as totalCantidad FROM DetalleVenta dv WHERE dv.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin GROUP BY dv.presentacion ORDER BY totalCantidad DESC")
    List<Object[]> findPresentacionesMasVendidasEnPeriodo(@Param("fechaInicio") java.time.LocalDateTime fechaInicio, 
                                                         @Param("fechaFin") java.time.LocalDateTime fechaFin);
    
    // Sumar cantidad vendida por producto en un rango de fechas
    @Query("SELECT COALESCE(SUM(dv.cantidad), 0) FROM DetalleVenta dv WHERE dv.producto = :producto AND dv.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    Integer sumCantidadByProductoEnPeriodo(@Param("producto") Producto producto, 
                                          @Param("fechaInicio") java.time.LocalDateTime fechaInicio, 
                                          @Param("fechaFin") java.time.LocalDateTime fechaFin);
    
    // Sumar subtotal por producto en un rango de fechas
    @Query("SELECT COALESCE(SUM(dv.subtotal), 0) FROM DetalleVenta dv WHERE dv.producto = :producto AND dv.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumSubtotalByProductoEnPeriodo(@Param("producto") Producto producto, 
                                             @Param("fechaInicio") java.time.LocalDateTime fechaInicio, 
                                             @Param("fechaFin") java.time.LocalDateTime fechaFin);
}
