package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Compra;
import com.proyectoavanzada.backend.model.DetalleCompra;
import com.proyectoavanzada.backend.model.Proveedor;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.model.Presentacion;
import com.proyectoavanzada.backend.repository.CompraRepository;
import com.proyectoavanzada.backend.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompraService {
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private DetalleCompraRepository detalleCompraRepository;
    
    @Autowired
    private PresentacionService presentacionService;
    
    /**
     * Obtener todas las compras
     */
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }
    
    /**
     * Obtener todas las compras activas
     */
    public List<Compra> obtenerComprasActivas() {
        return compraRepository.findByActivoTrue();
    }
    
    /**
     * Obtener compra por ID
     */
    public Optional<Compra> obtenerCompraPorId(Long id) {
        return compraRepository.findById(id);
    }
    
    /**
     * Obtener compra por número de factura
     */
    public Optional<Compra> obtenerCompraPorNumeroFactura(String numeroFactura) {
        return compraRepository.findByNumeroFactura(numeroFactura);
    }
    
    /**
     * Obtener compras por proveedor
     */
    public List<Compra> obtenerComprasPorProveedor(Proveedor proveedor) {
        return compraRepository.findByProveedor(proveedor);
    }
    
    /**
     * Obtener compras por usuario
     */
    public List<Compra> obtenerComprasPorUsuario(Usuario usuario) {
        return compraRepository.findByUsuario(usuario);
    }
    
    /**
     * Obtener compras por estado
     */
    public List<Compra> obtenerComprasPorEstado(String estado) {
        return compraRepository.findByEstado(estado);
    }
    
    /**
     * Obtener compras por método de pago
     */
    public List<Compra> obtenerComprasPorMetodoPago(String metodoPago) {
        return compraRepository.findByMetodoPago(metodoPago);
    }
    
    /**
     * Obtener compras por rango de fechas
     */
    public List<Compra> obtenerComprasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener compras pendientes
     */
    public List<Compra> obtenerComprasPendientes() {
        return compraRepository.findComprasPagadas();
    }
    
    /**
     * Obtener compras pagadas
     */
    public List<Compra> obtenerComprasPagadas() {
        return compraRepository.findComprasPagadas();
    }
    
    /**
     * Obtener compras canceladas
     */
    public List<Compra> obtenerComprasCanceladas() {
        return compraRepository.findComprasCanceladas();
    }
    
    /**
     * Obtener compras vencidas
     */
    public List<Compra> obtenerComprasVencidas() {
        return compraRepository.findComprasVencidas(LocalDateTime.now());
    }
    
    /**
     * Obtener compras por rango de total
     */
    public List<Compra> obtenerComprasPorRangoTotal(BigDecimal totalMin, BigDecimal totalMax) {
        return compraRepository.findByTotalBetween(totalMin, totalMax);
    }
    
    /**
     * Obtener compras más recientes
     */
    public List<Compra> obtenerComprasRecientes() {
        return compraRepository.findComprasRecientes();
    }
    
    /**
     * Obtener compras por proveedor y rango de fechas
     */
    public List<Compra> obtenerComprasPorProveedorYRangoFechas(Proveedor proveedor, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByProveedorAndFechaCompraBetween(proveedor, fechaInicio, fechaFin);
    }
    
    /**
     * Obtener compras por usuario y rango de fechas
     */
    public List<Compra> obtenerComprasPorUsuarioYRangoFechas(Usuario usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByUsuarioAndFechaCompraBetween(usuario, fechaInicio, fechaFin);
    }
    
    /**
     * Obtener compras con mayor total
     */
    public List<Compra> obtenerComprasConMayorTotal() {
        return compraRepository.findComprasConMayorTotal();
    }
    
    /**
     * Obtener compras con menor total
     */
    public List<Compra> obtenerComprasConMenorTotal() {
        return compraRepository.findComprasConMenorTotal();
    }
    
    /**
     * Obtener compras por número de comprobante
     */
    public List<Compra> obtenerComprasPorNumeroComprobante(String numeroComprobante) {
        return compraRepository.findByNumeroComprobante(numeroComprobante);
    }
    
    /**
     * Crear nueva compra
     */
    public Compra crearCompra(Compra compra) {
        // Generar número de factura si no se proporciona
        if (compra.getNumeroFactura() == null || compra.getNumeroFactura().isEmpty()) {
            compra.setNumeroFactura(generarNumeroFactura());
        }
        
        // Verificar si el número de factura ya existe
        if (compraRepository.existsByNumeroFactura(compra.getNumeroFactura())) {
            throw new RuntimeException("Ya existe una compra con este número de factura");
        }
        
        // Calcular fecha de vencimiento si no se proporciona
        if (compra.getFechaVencimiento() == null && compra.getProveedor().getCreditoDias() != null) {
            compra.setFechaVencimiento(compra.getFechaCompra().plusDays(compra.getProveedor().getCreditoDias()));
        }
        
        return compraRepository.save(compra);
    }
    
    /**
     * Actualizar compra
     */
    public Compra actualizarCompra(Compra compra) {
        if (!compraRepository.existsById(compra.getId())) {
            throw new RuntimeException("Compra no encontrada");
        }
        
        // Verificar si el número de factura ya existe en otra compra
        if (compra.getNumeroFactura() != null) {
            Optional<Compra> compraExistente = compraRepository.findByNumeroFactura(compra.getNumeroFactura());
            if (compraExistente.isPresent() && !compraExistente.get().getId().equals(compra.getId())) {
                throw new RuntimeException("Ya existe una compra con este número de factura");
            }
        }
        
        return compraRepository.save(compra);
    }
    
    /**
     * Eliminar compra (soft delete)
     */
    public void eliminarCompra(Long id) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            
            // Revertir stock de los productos
            revertirStockCompra(compra);
            
            compra.setActivo(false);
            compraRepository.save(compra);
        } else {
            throw new RuntimeException("Compra no encontrada");
        }
    }
    
    /**
     * Eliminar compra permanentemente
     */
    public void eliminarCompraPermanentemente(Long id) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            
            // Revertir stock de los productos
            revertirStockCompra(compra);
            
            // Eliminar detalles de compra
            List<DetalleCompra> detalles = detalleCompraRepository.findByCompra(compra);
            detalleCompraRepository.deleteAll(detalles);
            
            compraRepository.deleteById(id);
        } else {
            throw new RuntimeException("Compra no encontrada");
        }
    }
    
    /**
     * Marcar compra como pagada
     */
    public Compra marcarComoPagada(Long id) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            compra.setEstado("PAGADA");
            return compraRepository.save(compra);
        } else {
            throw new RuntimeException("Compra no encontrada");
        }
    }
    
    /**
     * Marcar compra como cancelada
     */
    public Compra marcarComoCancelada(Long id) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            compra.setEstado("CANCELADA");
            
            // Revertir stock de los productos
            revertirStockCompra(compra);
            
            return compraRepository.save(compra);
        } else {
            throw new RuntimeException("Compra no encontrada");
        }
    }
    
    /**
     * Agregar detalle a compra
     */
    public DetalleCompra agregarDetalleCompra(DetalleCompra detalleCompra) {
        // Verificar que la compra existe
        if (!compraRepository.existsById(detalleCompra.getCompra().getId())) {
            throw new RuntimeException("Compra no encontrada");
        }
        
        // Calcular subtotal
        detalleCompra.calcularSubtotal();
        
        // Guardar detalle
        DetalleCompra detalleGuardado = detalleCompraRepository.save(detalleCompra);
        
        // Actualizar stock del producto
        actualizarStockCompra(detalleCompra);
        
        // Recalcular totales de la compra
        recalcularTotalesCompra(detalleCompra.getCompra().getId());
        
        return detalleGuardado;
    }
    
    /**
     * Eliminar detalle de compra
     */
    public void eliminarDetalleCompra(Long detalleId) {
        Optional<DetalleCompra> detalleOpt = detalleCompraRepository.findById(detalleId);
        if (detalleOpt.isPresent()) {
            DetalleCompra detalle = detalleOpt.get();
            Long compraId = detalle.getCompra().getId();
            
            // Revertir stock del producto
            revertirStockDetalleCompra(detalle);
            
            // Eliminar detalle
            detalleCompraRepository.deleteById(detalleId);
            
            // Recalcular totales de la compra
            recalcularTotalesCompra(compraId);
        } else {
            throw new RuntimeException("Detalle de compra no encontrado");
        }
    }
    
    /**
     * Obtener detalles de compra
     */
    public List<DetalleCompra> obtenerDetallesCompra(Long compraId) {
        Optional<Compra> compraOpt = compraRepository.findById(compraId);
        if (compraOpt.isPresent()) {
            return detalleCompraRepository.findByCompra(compraOpt.get());
        } else {
            throw new RuntimeException("Compra no encontrada");
        }
    }
    
    /**
     * Recalcular totales de compra
     */
    public void recalcularTotalesCompra(Long compraId) {
        Optional<Compra> compraOpt = compraRepository.findById(compraId);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            List<DetalleCompra> detalles = detalleCompraRepository.findByCompra(compra);
            
            BigDecimal subtotal = detalles.stream()
                    .map(DetalleCompra::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            compra.setSubtotal(subtotal);
            compra.calcularTotal();
            compraRepository.save(compra);
        }
    }
    
    /**
     * Actualizar stock al agregar compra
     */
    private void actualizarStockCompra(DetalleCompra detalleCompra) {
        Presentacion presentacion = detalleCompra.getPresentacion();
        presentacionService.agregarStock(presentacion.getId(), detalleCompra.getCantidad());
    }
    
    /**
     * Revertir stock al eliminar compra
     */
    private void revertirStockCompra(Compra compra) {
        List<DetalleCompra> detalles = detalleCompraRepository.findByCompra(compra);
        for (DetalleCompra detalle : detalles) {
            revertirStockDetalleCompra(detalle);
        }
    }
    
    /**
     * Revertir stock de un detalle de compra
     */
    private void revertirStockDetalleCompra(DetalleCompra detalleCompra) {
        Presentacion presentacion = detalleCompra.getPresentacion();
        presentacionService.reducirStock(presentacion.getId(), detalleCompra.getCantidad());
    }
    
    /**
     * Generar número de factura automático
     */
    private String generarNumeroFactura() {
        long count = compraRepository.count() + 1;
        return "COMP-" + String.format("%06d", count);
    }
    
    /**
     * Verificar si existe una compra con el número de factura
     */
    public boolean existeCompraConNumeroFactura(String numeroFactura) {
        return compraRepository.existsByNumeroFactura(numeroFactura);
    }
    
    /**
     * Contar compras por estado
     */
    public long contarComprasPorEstado(String estado) {
        return compraRepository.countByEstado(estado);
    }
    
    /**
     * Contar compras activas
     */
    public long contarComprasActivas() {
        return compraRepository.countByActivoTrue();
    }
    
    /**
     * Obtener total de compras por proveedor
     */
    public BigDecimal obtenerTotalComprasPorProveedor(Proveedor proveedor) {
        return compraRepository.sumTotalByProveedor(proveedor);
    }
    
    /**
     * Obtener total de compras por usuario
     */
    public BigDecimal obtenerTotalComprasPorUsuario(Usuario usuario) {
        return compraRepository.sumTotalByUsuario(usuario);
    }
    
    /**
     * Obtener total de compras en un rango de fechas
     */
    public BigDecimal obtenerTotalComprasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.sumTotalByFechaCompraBetween(fechaInicio, fechaFin);
    }
}
