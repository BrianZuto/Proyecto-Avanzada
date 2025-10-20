package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Venta;
import com.proyectoavanzada.backend.model.Cliente;
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
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    // Buscar por número de venta
    Optional<Venta> findByNumeroVenta(String numeroVenta);
    
    // Buscar por cliente
    List<Venta> findByCliente(Cliente cliente);
    
    // Buscar por usuario vendedor
    List<Venta> findByUsuario(Usuario usuario);
    
    // Buscar por estado
    List<Venta> findByEstado(String estado);
    
    // Buscar por método de pago
    List<Venta> findByMetodoPago(String metodoPago);
    
    // Buscar ventas activas
    List<Venta> findByActivoTrue();
    
    // Buscar ventas inactivas
    List<Venta> findByActivoFalse();
    
    // Buscar por rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findByFechaVentaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                       @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar ventas completadas
    @Query("SELECT v FROM Venta v WHERE v.estado = 'COMPLETADA' AND v.activo = true")
    List<Venta> findVentasCompletadas();
    
    // Buscar ventas canceladas
    @Query("SELECT v FROM Venta v WHERE v.estado = 'CANCELADA' AND v.activo = true")
    List<Venta> findVentasCanceladas();
    
    // Buscar ventas devueltas
    @Query("SELECT v FROM Venta v WHERE v.estado = 'DEVUELTA' AND v.activo = true")
    List<Venta> findVentasDevueltas();
    
    // Buscar por rango de total
    @Query("SELECT v FROM Venta v WHERE v.activo = true AND v.total BETWEEN :totalMin AND :totalMax")
    List<Venta> findByTotalBetween(@Param("totalMin") BigDecimal totalMin, 
                                  @Param("totalMax") BigDecimal totalMax);
    
    // Buscar ventas con cliente
    @Query("SELECT v FROM Venta v WHERE v.cliente IS NOT NULL AND v.activo = true")
    List<Venta> findVentasConCliente();
    
    // Buscar ventas sin cliente (ventas al contado)
    @Query("SELECT v FROM Venta v WHERE v.cliente IS NULL AND v.activo = true")
    List<Venta> findVentasSinCliente();
    
    // Contar ventas por estado
    long countByEstado(String estado);
    
    // Contar ventas activas
    long countByActivoTrue();
    
    // Contar ventas por cliente
    long countByCliente(Cliente cliente);
    
    // Contar ventas por usuario
    long countByUsuario(Usuario usuario);
    
    // Sumar total de ventas por cliente
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.cliente = :cliente AND v.activo = true")
    BigDecimal sumTotalByCliente(@Param("cliente") Cliente cliente);
    
    // Sumar total de ventas por usuario
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.usuario = :usuario AND v.activo = true")
    BigDecimal sumTotalByUsuario(@Param("usuario") Usuario usuario);
    
    // Sumar total de ventas en un rango de fechas
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.activo = true")
    BigDecimal sumTotalByFechaVentaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                         @Param("fechaFin") LocalDateTime fechaFin);
    
    // Sumar total de ventas por método de pago
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.metodoPago = :metodoPago AND v.activo = true")
    BigDecimal sumTotalByMetodoPago(@Param("metodoPago") String metodoPago);
    
    // Buscar ventas más recientes
    @Query("SELECT v FROM Venta v WHERE v.activo = true ORDER BY v.fechaVenta DESC")
    List<Venta> findVentasRecientes();
    
    // Buscar ventas más antiguas
    @Query("SELECT v FROM Venta v WHERE v.activo = true ORDER BY v.fechaVenta ASC")
    List<Venta> findVentasAntiguas();
    
    // Buscar ventas por cliente y rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.cliente = :cliente AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.activo = true")
    List<Venta> findByClienteAndFechaVentaBetween(@Param("cliente") Cliente cliente, 
                                                 @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                 @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar ventas por usuario y rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.usuario = :usuario AND v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.activo = true")
    List<Venta> findByUsuarioAndFechaVentaBetween(@Param("usuario") Usuario usuario, 
                                                 @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                 @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar ventas con mayor total
    @Query("SELECT v FROM Venta v WHERE v.activo = true ORDER BY v.total DESC")
    List<Venta> findVentasConMayorTotal();
    
    // Buscar ventas con menor total
    @Query("SELECT v FROM Venta v WHERE v.activo = true ORDER BY v.total ASC")
    List<Venta> findVentasConMenorTotal();
    
    // Buscar ventas con puntos de fidelidad
    @Query("SELECT v FROM Venta v WHERE v.activo = true AND v.puntosOtorgados > 0")
    List<Venta> findVentasConPuntosOtorgados();
    
    // Buscar ventas que usaron puntos de fidelidad
    @Query("SELECT v FROM Venta v WHERE v.activo = true AND v.puntosUsados > 0")
    List<Venta> findVentasConPuntosUsados();
    
    // Verificar si existe un número de venta
    boolean existsByNumeroVenta(String numeroVenta);
    
    // Buscar ventas por número de comprobante
    List<Venta> findByNumeroComprobante(String numeroComprobante);
    
    // Buscar clientes más frecuentes
    @Query("SELECT v.cliente, COUNT(v) as totalVentas FROM Venta v WHERE v.cliente IS NOT NULL AND v.activo = true GROUP BY v.cliente ORDER BY totalVentas DESC")
    List<Object[]> findClientesMasFrecuentes();
    
    // Buscar vendedores más activos
    @Query("SELECT v.usuario, COUNT(v) as totalVentas, COALESCE(SUM(v.total), 0) as totalVendido FROM Venta v WHERE v.activo = true GROUP BY v.usuario ORDER BY totalVentas DESC")
    List<Object[]> findVendedoresMasActivos();
}
