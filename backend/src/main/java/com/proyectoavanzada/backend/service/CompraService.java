package com.proyectoavanzada.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectoavanzada.backend.model.Compra;
import com.proyectoavanzada.backend.model.DetalleCompra;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Proveedor;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.repository.CompraRepository;
import com.proyectoavanzada.backend.repository.DetalleCompraRepository;
import com.proyectoavanzada.backend.repository.ProductoRepository;
import com.proyectoavanzada.backend.repository.ProveedorRepository;
import com.proyectoavanzada.backend.repository.UsuarioRepository;

@Service
@Transactional
public class CompraService {
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private DetalleCompraRepository detalleCompraRepository;
    
    @Autowired
    private PresentacionService presentacionService;
    
    @Autowired
    private InventarioService inventarioService;
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
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
        // Cargar proveedor desde la base de datos si solo se proporciona el ID
        if (compra.getProveedor() != null && compra.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorRepository.findById(compra.getProveedor().getId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + compra.getProveedor().getId()));
            compra.setProveedor(proveedor);
        } else if (compra.getProveedor() == null) {
            throw new RuntimeException("El proveedor es obligatorio");
        }
        
        // Cargar usuario desde la base de datos si solo se proporciona el ID
        if (compra.getUsuario() != null && compra.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(compra.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + compra.getUsuario().getId()));
            compra.setUsuario(usuario);
        } else if (compra.getUsuario() == null) {
            throw new RuntimeException("El usuario es obligatorio");
        }
        
        // Generar número de factura si no se proporciona
        if (compra.getNumeroFactura() == null || compra.getNumeroFactura().isEmpty()) {
            compra.setNumeroFactura(generarNumeroFactura());
        }
        
        // Verificar si el número de factura ya existe
        if (compraRepository.existsByNumeroFactura(compra.getNumeroFactura())) {
            throw new RuntimeException("Ya existe una compra con este número de factura");
        }
        
        // Establecer fecha de compra si no se proporciona
        if (compra.getFechaCompra() == null) {
            compra.setFechaCompra(LocalDateTime.now());
        }
        
        // Calcular fecha de vencimiento si no se proporciona
        if (compra.getFechaVencimiento() == null && compra.getProveedor().getCreditoDias() != null) {
            compra.setFechaVencimiento(compra.getFechaCompra().plusDays(compra.getProveedor().getCreditoDias()));
        }
        
        // Asegurar que descuento e impuesto estén establecidos
        if (compra.getDescuento() == null) {
            compra.setDescuento(BigDecimal.ZERO);
        }
        if (compra.getImpuesto() == null) {
            compra.setImpuesto(BigDecimal.ZERO);
        }
        
        // Si el subtotal no está establecido, establecerlo en 0 temporalmente
        // Se recalculará después de guardar los detalles
        if (compra.getSubtotal() == null) {
            compra.setSubtotal(BigDecimal.ZERO);
        }
        
        // Calcular total inicial (se recalculará después)
        compra.calcularTotal();
        
        // Procesar detalles ANTES de guardar la compra
        System.out.println("=== PROCESANDO DETALLES ===");
        System.out.println("Detalles recibidos: " + (compra.getDetallesCompra() != null ? compra.getDetallesCompra().size() : 0));
        
        if (compra.getDetallesCompra() != null && !compra.getDetallesCompra().isEmpty()) {
            System.out.println("Iniciando procesamiento de " + compra.getDetallesCompra().size() + " detalles");
            
            // Procesar cada detalle: cargar producto y establecer la compra
            for (DetalleCompra detalle : compra.getDetallesCompra()) {
                System.out.println("--- Procesando detalle ---");
                System.out.println("Producto ID en detalle: " + (detalle.getProducto() != null ? detalle.getProducto().getId() : "NULL"));
                System.out.println("Cantidad: " + detalle.getCantidad());
                
                // ESTABLECER LA COMPRA EN EL DETALLE (necesario para la validación @NotNull)
                detalle.setCompra(compra);
                System.out.println("Compra establecida en detalle");
                
                // Cargar producto desde la base de datos si solo se proporciona el ID
                if (detalle.getProducto() != null && detalle.getProducto().getId() != null) {
                    Producto producto = productoRepository.findById(detalle.getProducto().getId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getId()));
                    detalle.setProducto(producto);
                    System.out.println("Producto cargado: " + producto.getNombre());
                } else if (detalle.getProducto() == null) {
                    throw new RuntimeException("El producto es obligatorio en el detalle");
                }
                
                // Calcular subtotal
                detalle.calcularSubtotal();
                System.out.println("Subtotal calculado: " + detalle.getSubtotal());
            }
        }
        
        // Guardar la compra con todos los detalles (la cascada los guardará automáticamente)
        System.out.println("=== GUARDANDO COMPRA CON DETALLES ===");
        Compra compraGuardada = compraRepository.saveAndFlush(compra);
        System.out.println("Compra guardada con ID: " + compraGuardada.getId());
        System.out.println("Número de factura: " + compraGuardada.getNumeroFactura());
        
        // Actualizar stock después de guardar (necesitamos los detalles guardados)
        if (compraGuardada.getDetallesCompra() != null && !compraGuardada.getDetallesCompra().isEmpty()) {
            System.out.println("=== ACTUALIZANDO STOCK PARA " + compraGuardada.getDetallesCompra().size() + " DETALLES ===");
            for (DetalleCompra detalle : compraGuardada.getDetallesCompra()) {
                System.out.println("Actualizando stock para producto ID: " + detalle.getProducto().getId() + ", Cantidad: " + detalle.getCantidad());
                actualizarStockCompra(detalle);
                System.out.println("Stock actualizado para producto ID: " + detalle.getProducto().getId());
            }
        }
        
        // Recalcular totales después de guardar todos los detalles
        System.out.println("=== RECALCULANDO TOTALES ===");
        recalcularTotalesCompra(compraGuardada.getId());
        
        // Recargar la compra con los totales actualizados
        compraGuardada = compraRepository.findById(compraGuardada.getId())
                .orElseThrow(() -> new RuntimeException("Error al recargar la compra después de guardar los detalles"));
        
        System.out.println("Compra recargada - Subtotal: " + compraGuardada.getSubtotal() + ", Total: " + compraGuardada.getTotal());
        
        return compraGuardada;
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
            
            // Calcular subtotal sumando todos los subtotales de los detalles
            BigDecimal subtotal = detalles.stream()
                    .map(detalle -> {
                        if (detalle.getSubtotal() != null) {
                            return detalle.getSubtotal();
                        } else {
                            // Si el subtotal no está calculado, calcularlo
                            detalle.calcularSubtotal();
                            return detalle.getSubtotal();
                        }
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            System.out.println("Subtotal calculado: " + subtotal);
            compra.setSubtotal(subtotal);
            
            // Asegurar que descuento e impuesto estén establecidos
            if (compra.getDescuento() == null) {
                compra.setDescuento(BigDecimal.ZERO);
            }
            if (compra.getImpuesto() == null) {
                compra.setImpuesto(BigDecimal.ZERO);
            }
            
            System.out.println("Descuento: " + compra.getDescuento() + ", Impuesto: " + compra.getImpuesto());
            
            // Calcular el total final (subtotal - descuento + impuesto)
            compra.calcularTotal();
            System.out.println("Total calculado: " + compra.getTotal());
            
            compraRepository.save(compra);
            System.out.println("Compra guardada con total: " + compra.getTotal());
        }
    }
    
    /**
     * Actualizar stock al agregar compra
     */
    private void actualizarStockCompra(DetalleCompra detalleCompra) {
        System.out.println("=== INICIANDO actualizarStockCompra ===");
        System.out.println("Presentación: " + (detalleCompra.getPresentacion() != null ? detalleCompra.getPresentacion().getId() : "NULL"));
        System.out.println("Producto: " + (detalleCompra.getProducto() != null ? detalleCompra.getProducto().getId() : "NULL"));
        System.out.println("Cantidad: " + detalleCompra.getCantidad());
        
        try {
            if (detalleCompra.getPresentacion() != null) {
                System.out.println("Actualizando stock de presentación");
                // Si hay presentación, actualizar stock de la presentación
                presentacionService.agregarStock(detalleCompra.getPresentacion().getId(), detalleCompra.getCantidad());
            } else {
                System.out.println("Actualizando stock de producto (sin presentación)");
                // Si no hay presentación, actualizar stock directamente del producto
                if (detalleCompra.getProducto() != null && detalleCompra.getProducto().getId() != null) {
                    System.out.println("Llamando a inventarioService.agregarStockProducto con ID: " + detalleCompra.getProducto().getId());
                    inventarioService.agregarStockProducto(detalleCompra.getProducto().getId(), detalleCompra.getCantidad());
                    System.out.println("inventarioService.agregarStockProducto completado");
                } else {
                    System.err.println("ERROR: Producto es NULL o no tiene ID");
                    throw new RuntimeException("No se puede actualizar el stock: el producto no está definido en el detalle");
                }
            }
            System.out.println("=== actualizarStockCompra completado exitosamente ===");
        } catch (Exception e) {
            System.err.println("ERROR en actualizarStockCompra: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el stock del producto: " + e.getMessage(), e);
        }
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
        if (detalleCompra.getPresentacion() != null) {
            // Si hay presentación, revertir stock de la presentación
            presentacionService.reducirStock(detalleCompra.getPresentacion().getId(), detalleCompra.getCantidad());
        } else {
            // Si no hay presentación, revertir stock directamente del producto
            inventarioService.reducirStockProducto(detalleCompra.getProducto().getId(), detalleCompra.getCantidad());
        }
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
