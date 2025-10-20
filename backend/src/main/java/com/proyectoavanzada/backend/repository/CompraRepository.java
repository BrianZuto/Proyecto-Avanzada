package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Compra;
import com.proyectoavanzada.backend.model.Proveedor;
import com.proyectoavanzada.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    // Buscar por número de factura
    Optional<Compra> findByNumeroFactura(String numeroFactura);
    
    // Buscar por proveedor
    List<Compra> findByProveedor(Proveedor proveedor);
    
    // Buscar por usuario
    List<Compra> findByUsuario(Usuario usuario);
    
    // Buscar por estado
    List<Compra> findByEstado(String estado);
    
    // Buscar por método de pago
    List<Compra> findByMetodoPago(String metodoPago);
    
    // Buscar compras activas
    List<Compra> findByActivoTrue();
    
    // Buscar compras inactivas
    List<Compra> findByActivoFalse();
    
    // Buscar por rango de fechas
    @Query("SELECT c FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin")
    List<Compra> findByFechaCompraBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                        @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar compras pendientes
    List<Compra> findByEstadoAndActivoTrue(String estado);
    
    // Buscar compras pagadas
    @Query("SELECT c FROM Compra c WHERE c.estado = 'PAGADA' AND c.activo = true")
    List<Compra> findComprasPagadas();
    
    // Buscar compras canceladas
    @Query("SELECT c FROM Compra c WHERE c.estado = 'CANCELADA' AND c.activo = true")
    List<Compra> findComprasCanceladas();
    
    // Buscar compras vencidas
    @Query("SELECT c FROM Compra c WHERE c.estado = 'PENDIENTE' AND c.fechaVencimiento < :fechaActual AND c.activo = true")
    List<Compra> findComprasVencidas(@Param("fechaActual") LocalDateTime fechaActual);
    
    // Buscar por rango de total
    @Query("SELECT c FROM Compra c WHERE c.activo = true AND c.total BETWEEN :totalMin AND :totalMax")
    List<Compra> findByTotalBetween(@Param("totalMin") BigDecimal totalMin, 
                                   @Param("totalMax") BigDecimal totalMax);
    
    // Contar compras por estado
    long countByEstado(String estado);
    
    // Contar compras activas
    long countByActivoTrue();
    
    // Contar compras por proveedor
    long countByProveedor(Proveedor proveedor);
    
    // Contar compras por usuario
    long countByUsuario(Usuario usuario);
    
    // Sumar total de compras por proveedor
    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Compra c WHERE c.proveedor = :proveedor AND c.activo = true")
    BigDecimal sumTotalByProveedor(@Param("proveedor") Proveedor proveedor);
    
    // Sumar total de compras por usuario
    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Compra c WHERE c.usuario = :usuario AND c.activo = true")
    BigDecimal sumTotalByUsuario(@Param("usuario") Usuario usuario);
    
    // Sumar total de compras en un rango de fechas
    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin AND c.activo = true")
    BigDecimal sumTotalByFechaCompraBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                          @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar compras más recientes
    @Query("SELECT c FROM Compra c WHERE c.activo = true ORDER BY c.fechaCompra DESC")
    List<Compra> findComprasRecientes();
    
    // Buscar compras más antiguas
    @Query("SELECT c FROM Compra c WHERE c.activo = true ORDER BY c.fechaCompra ASC")
    List<Compra> findComprasAntiguas();
    
    // Buscar compras por proveedor y rango de fechas
    @Query("SELECT c FROM Compra c WHERE c.proveedor = :proveedor AND c.fechaCompra BETWEEN :fechaInicio AND :fechaFin AND c.activo = true")
    List<Compra> findByProveedorAndFechaCompraBetween(@Param("proveedor") Proveedor proveedor, 
                                                     @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                     @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar compras por usuario y rango de fechas
    @Query("SELECT c FROM Compra c WHERE c.usuario = :usuario AND c.fechaCompra BETWEEN :fechaInicio AND :fechaFin AND c.activo = true")
    List<Compra> findByUsuarioAndFechaCompraBetween(@Param("usuario") Usuario usuario, 
                                                   @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar compras con mayor total
    @Query("SELECT c FROM Compra c WHERE c.activo = true ORDER BY c.total DESC")
    List<Compra> findComprasConMayorTotal();
    
    // Buscar compras con menor total
    @Query("SELECT c FROM Compra c WHERE c.activo = true ORDER BY c.total ASC")
    List<Compra> findComprasConMenorTotal();
    
    // Verificar si existe un número de factura
    boolean existsByNumeroFactura(String numeroFactura);
    
    // Buscar compras por número de comprobante
    List<Compra> findByNumeroComprobante(String numeroComprobante);
}
