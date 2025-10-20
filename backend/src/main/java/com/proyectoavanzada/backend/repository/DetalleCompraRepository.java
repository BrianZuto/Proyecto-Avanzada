package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.DetalleCompra;
import com.proyectoavanzada.backend.model.Compra;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    
    // Buscar por compra
    List<DetalleCompra> findByCompra(Compra compra);
    
    // Buscar por producto
    List<DetalleCompra> findByProducto(Producto producto);
    
    // Buscar por presentación
    List<DetalleCompra> findByPresentacion(Presentacion presentacion);
    
    // Buscar por compra y producto
    List<DetalleCompra> findByCompraAndProducto(Compra compra, Producto producto);
    
    // Buscar por compra y presentación
    List<DetalleCompra> findByCompraAndPresentacion(Compra compra, Presentacion presentacion);
    
    // Buscar por cantidad
    List<DetalleCompra> findByCantidad(Integer cantidad);
    
    // Buscar por rango de cantidad
    @Query("SELECT dc FROM DetalleCompra dc WHERE dc.cantidad BETWEEN :cantidadMin AND :cantidadMax")
    List<DetalleCompra> findByCantidadBetween(@Param("cantidadMin") Integer cantidadMin, 
                                            @Param("cantidadMax") Integer cantidadMax);
    
    // Buscar por rango de precio unitario
    @Query("SELECT dc FROM DetalleCompra dc WHERE dc.precioUnitario BETWEEN :precioMin AND :precioMax")
    List<DetalleCompra> findByPrecioUnitarioBetween(@Param("precioMin") BigDecimal precioMin, 
                                                   @Param("precioMax") BigDecimal precioMax);
    
    // Buscar por rango de subtotal
    @Query("SELECT dc FROM DetalleCompra dc WHERE dc.subtotal BETWEEN :subtotalMin AND :subtotalMax")
    List<DetalleCompra> findBySubtotalBetween(@Param("subtotalMin") BigDecimal subtotalMin, 
                                            @Param("subtotalMax") BigDecimal subtotalMax);
    
    // Contar detalles por compra
    long countByCompra(Compra compra);
    
    // Contar detalles por producto
    long countByProducto(Producto producto);
    
    // Contar detalles por presentación
    long countByPresentacion(Presentacion presentacion);
    
    // Sumar cantidad total por producto
    @Query("SELECT COALESCE(SUM(dc.cantidad), 0) FROM DetalleCompra dc WHERE dc.producto = :producto")
    Integer sumCantidadByProducto(@Param("producto") Producto producto);
    
    // Sumar cantidad total por presentación
    @Query("SELECT COALESCE(SUM(dc.cantidad), 0) FROM DetalleCompra dc WHERE dc.presentacion = :presentacion")
    Integer sumCantidadByPresentacion(@Param("presentacion") Presentacion presentacion);
    
    // Sumar subtotal por compra
    @Query("SELECT COALESCE(SUM(dc.subtotal), 0) FROM DetalleCompra dc WHERE dc.compra = :compra")
    BigDecimal sumSubtotalByCompra(@Param("compra") Compra compra);
    
    // Sumar subtotal por producto
    @Query("SELECT COALESCE(SUM(dc.subtotal), 0) FROM DetalleCompra dc WHERE dc.producto = :producto")
    BigDecimal sumSubtotalByProducto(@Param("producto") Producto producto);
    
    // Buscar detalles con mayor cantidad
    @Query("SELECT dc FROM DetalleCompra dc ORDER BY dc.cantidad DESC")
    List<DetalleCompra> findDetallesConMayorCantidad();
    
    // Buscar detalles con mayor subtotal
    @Query("SELECT dc FROM DetalleCompra dc ORDER BY dc.subtotal DESC")
    List<DetalleCompra> findDetallesConMayorSubtotal();
    
    // Buscar detalles con mayor precio unitario
    @Query("SELECT dc FROM DetalleCompra dc ORDER BY dc.precioUnitario DESC")
    List<DetalleCompra> findDetallesConMayorPrecioUnitario();
    
    // Buscar productos más comprados
    @Query("SELECT dc.producto, SUM(dc.cantidad) as totalCantidad FROM DetalleCompra dc GROUP BY dc.producto ORDER BY totalCantidad DESC")
    List<Object[]> findProductosMasComprados();
    
    // Buscar presentaciones más compradas
    @Query("SELECT dc.presentacion, SUM(dc.cantidad) as totalCantidad FROM DetalleCompra dc GROUP BY dc.presentacion ORDER BY totalCantidad DESC")
    List<Object[]> findPresentacionesMasCompradas();
    
    // Buscar detalles por compra ordenados por subtotal descendente
    List<DetalleCompra> findByCompraOrderBySubtotalDesc(Compra compra);
    
    // Buscar detalles por compra ordenados por cantidad descendente
    List<DetalleCompra> findByCompraOrderByCantidadDesc(Compra compra);
}
