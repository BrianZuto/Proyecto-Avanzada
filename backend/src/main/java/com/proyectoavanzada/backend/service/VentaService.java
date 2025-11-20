package com.proyectoavanzada.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.model.DetalleVenta;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.model.Venta;
import com.proyectoavanzada.backend.repository.DetalleVentaRepository;
import com.proyectoavanzada.backend.repository.ProductoRepository;
import com.proyectoavanzada.backend.repository.UsuarioRepository;
import com.proyectoavanzada.backend.repository.VentaRepository;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired(required = false)
    private PresentacionService presentacionService;
    
    @Autowired
    private InventarioService inventarioService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Obtener todas las ventas
     */
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        // Inicializar detalles de venta y productos
        for (Venta venta : ventas) {
            if (venta.getDetallesVenta() != null) {
                venta.getDetallesVenta().size(); // Forzar carga de detalles
                for (DetalleVenta detalle : venta.getDetallesVenta()) {
                    if (detalle.getProducto() != null) {
                        detalle.getProducto().getNombre(); // Forzar carga de producto
                        if (detalle.getProducto().getMarca() != null) {
                            detalle.getProducto().getMarca().getNombre(); // Forzar carga de marca
                        }
                    }
                }
            }
        }
        return ventas;
    }
    
    /**
     * Obtener todas las ventas activas
     */
    public List<Venta> obtenerVentasActivas() {
        List<Venta> ventas = ventaRepository.findByActivoTrue();
        // Inicializar detalles de venta y productos
        for (Venta venta : ventas) {
            if (venta.getDetallesVenta() != null) {
                venta.getDetallesVenta().size(); // Forzar carga de detalles
                for (DetalleVenta detalle : venta.getDetallesVenta()) {
                    if (detalle.getProducto() != null) {
                        detalle.getProducto().getNombre(); // Forzar carga de producto
                        if (detalle.getProducto().getMarca() != null) {
                            detalle.getProducto().getMarca().getNombre(); // Forzar carga de marca
                        }
                    }
                }
            }
        }
        return ventas;
    }
    
    /**
     * Obtener venta por ID
     */
    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }
    
    /**
     * Obtener venta por número de venta
     */
    public Optional<Venta> obtenerVentaPorNumeroVenta(String numeroVenta) {
        return ventaRepository.findByNumeroVenta(numeroVenta);
    }
    
    /**
     * Obtener ventas por cliente
     */
    public List<Venta> obtenerVentasPorCliente(Cliente cliente) {
        return ventaRepository.findByCliente(cliente);
    }
    
    /**
     * Obtener ventas por usuario vendedor
     */
    public List<Venta> obtenerVentasPorUsuario(Usuario usuario) {
        List<Venta> ventas = ventaRepository.findByUsuario(usuario);
        // Inicializar detalles de venta y productos
        for (Venta venta : ventas) {
            if (venta.getDetallesVenta() != null) {
                venta.getDetallesVenta().size(); // Forzar carga de detalles
                for (DetalleVenta detalle : venta.getDetallesVenta()) {
                    if (detalle.getProducto() != null) {
                        detalle.getProducto().getNombre(); // Forzar carga de producto
                        if (detalle.getProducto().getMarca() != null) {
                            detalle.getProducto().getMarca().getNombre(); // Forzar carga de marca
                        }
                    }
                }
            }
        }
        return ventas;
    }
    
    /**
     * Obtener ventas por estado
     */
    public List<Venta> obtenerVentasPorEstado(String estado) {
        return ventaRepository.findByEstado(estado);
    }
    
    /**
     * Obtener ventas por método de pago
     */
    public List<Venta> obtenerVentasPorMetodoPago(String metodoPago) {
        return ventaRepository.findByMetodoPago(metodoPago);
    }
    
    /**
     * Obtener ventas por rango de fechas
     */
    public List<Venta> obtenerVentasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener ventas completadas
     */
    public List<Venta> obtenerVentasCompletadas() {
        return ventaRepository.findVentasCompletadas();
    }
    
    /**
     * Obtener ventas canceladas
     */
    public List<Venta> obtenerVentasCanceladas() {
        return ventaRepository.findVentasCanceladas();
    }
    
    /**
     * Obtener ventas devueltas
     */
    public List<Venta> obtenerVentasDevueltas() {
        return ventaRepository.findVentasDevueltas();
    }
    
    /**
     * Obtener ventas con cliente
     */
    public List<Venta> obtenerVentasConCliente() {
        return ventaRepository.findVentasConCliente();
    }
    
    /**
     * Obtener ventas sin cliente (ventas al contado)
     */
    public List<Venta> obtenerVentasSinCliente() {
        return ventaRepository.findVentasSinCliente();
    }
    
    /**
     * Obtener ventas por rango de total
     */
    public List<Venta> obtenerVentasPorRangoTotal(BigDecimal totalMin, BigDecimal totalMax) {
        return ventaRepository.findByTotalBetween(totalMin, totalMax);
    }
    
    /**
     * Obtener ventas más recientes
     */
    public List<Venta> obtenerVentasRecientes() {
        return ventaRepository.findVentasRecientes();
    }
    
    /**
     * Obtener ventas por cliente y rango de fechas
     */
    public List<Venta> obtenerVentasPorClienteYRangoFechas(Cliente cliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByClienteAndFechaVentaBetween(cliente, fechaInicio, fechaFin);
    }
    
    /**
     * Obtener ventas por usuario y rango de fechas
     */
    public List<Venta> obtenerVentasPorUsuarioYRangoFechas(Usuario usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByUsuarioAndFechaVentaBetween(usuario, fechaInicio, fechaFin);
    }
    
    /**
     * Obtener ventas con mayor total
     */
    public List<Venta> obtenerVentasConMayorTotal() {
        return ventaRepository.findVentasConMayorTotal();
    }
    
    /**
     * Obtener ventas con menor total
     */
    public List<Venta> obtenerVentasConMenorTotal() {
        return ventaRepository.findVentasConMenorTotal();
    }
    
    /**
     * Obtener ventas con puntos de fidelidad otorgados
     */
    public List<Venta> obtenerVentasConPuntosOtorgados() {
        return ventaRepository.findVentasConPuntosOtorgados();
    }
    
    /**
     * Obtener ventas que usaron puntos de fidelidad
     */
    public List<Venta> obtenerVentasConPuntosUsados() {
        return ventaRepository.findVentasConPuntosUsados();
    }
    
    /**
     * Obtener clientes más frecuentes
     */
    public List<Object[]> obtenerClientesMasFrecuentes() {
        return ventaRepository.findClientesMasFrecuentes();
    }
    
    /**
     * Obtener vendedores más activos
     */
    public List<Object[]> obtenerVendedoresMasActivos() {
        return ventaRepository.findVendedoresMasActivos();
    }
    
    /**
     * Obtener ventas por número de comprobante
     */
    public List<Venta> obtenerVentasPorNumeroComprobante(String numeroComprobante) {
        return ventaRepository.findByNumeroComprobante(numeroComprobante);
    }
    
    /**
     * Crear nueva venta
     */
    public Venta crearVenta(Venta venta) {
        // Cargar usuario desde la base de datos si solo se proporciona el ID
        Long usuarioId = null;
        if (venta.getUsuario() != null && venta.getUsuario().getId() != null) {
            usuarioId = venta.getUsuario().getId();
        } else {
            // Intentar obtener el ID desde el método getUsuarioId()
            usuarioId = venta.getUsuarioId();
        }
        
        final Long finalUsuarioId = usuarioId;
        if (finalUsuarioId != null) {
            Usuario usuario = usuarioRepository.findById(finalUsuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + finalUsuarioId));
            venta.setUsuario(usuario);
        } else {
            throw new RuntimeException("El usuario es obligatorio para crear una venta");
        }
        
        // Generar número de venta si no se proporciona
        if (venta.getNumeroVenta() == null || venta.getNumeroVenta().isEmpty()) {
            venta.setNumeroVenta(generarNumeroVenta());
        }
        
        // Verificar si el número de venta ya existe
        if (ventaRepository.existsByNumeroVenta(venta.getNumeroVenta())) {
            throw new RuntimeException("Ya existe una venta con este número");
        }
        
        // Validar que el subtotal y total sean positivos
        if (venta.getSubtotal() == null || venta.getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El subtotal debe ser mayor a 0");
        }
        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El total debe ser mayor a 0");
        }
        
        // Procesar detalles antes de guardar
        if (venta.getDetallesVenta() == null || venta.getDetallesVenta().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un detalle");
        }
        
        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            // Validar cantidad y precio unitario
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0 para cada detalle");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El precio unitario debe ser mayor a 0 para cada detalle");
            }
            
            // Cargar producto desde la base de datos si solo se proporciona el ID
            Long productoId = null;
            if (detalle.getProducto() != null && detalle.getProducto().getId() != null) {
                productoId = detalle.getProducto().getId();
            } else {
                // Intentar obtener el ID desde el método getProductoId()
                productoId = detalle.getProductoId();
            }
            
            final Long finalProductoId = productoId;
            if (finalProductoId == null) {
                throw new RuntimeException("El producto es obligatorio para cada detalle de venta");
            }
            
            Producto producto = productoRepository.findById(finalProductoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + finalProductoId));
            detalle.setProducto(producto);
            
            // Establecer la venta en el detalle
            detalle.setVenta(venta);
            
            // Calcular subtotal
            detalle.calcularSubtotal();
        }
        
        // Guardar la venta con todos los detalles (la cascada los guardará automáticamente)
        Venta ventaGuardada = ventaRepository.saveAndFlush(venta);
        
        // Actualizar stock después de guardar
        if (ventaGuardada.getDetallesVenta() != null && !ventaGuardada.getDetallesVenta().isEmpty()) {
            for (DetalleVenta detalle : ventaGuardada.getDetallesVenta()) {
                actualizarStockVenta(detalle);
            }
        }
        
        // Recalcular totales
        recalcularTotalesVenta(ventaGuardada.getId());
        
        // Recargar la venta con los totales actualizados
        return ventaRepository.findById(ventaGuardada.getId())
                .orElseThrow(() -> new RuntimeException("Error al recargar la venta después de guardar"));
    }
    
    /**
     * Actualizar venta
     */
    public Venta actualizarVenta(Venta venta) {
        if (!ventaRepository.existsById(venta.getId())) {
            throw new RuntimeException("Venta no encontrada");
        }
        
        // Verificar si el número de venta ya existe en otra venta
        if (venta.getNumeroVenta() != null) {
            Optional<Venta> ventaExistente = ventaRepository.findByNumeroVenta(venta.getNumeroVenta());
            if (ventaExistente.isPresent() && !ventaExistente.get().getId().equals(venta.getId())) {
                throw new RuntimeException("Ya existe una venta con este número");
            }
        }
        
        return ventaRepository.save(venta);
    }
    
    /**
     * Eliminar venta (soft delete)
     */
    public void eliminarVenta(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            
            // Revertir stock de los productos
            revertirStockVenta(venta);
            
            // Revertir puntos de fidelidad si se otorgaron
            if (venta.getPuntosOtorgados() > 0 && venta.getCliente() != null) {
                clienteService.usarPuntosFidelidad(venta.getCliente().getId(), venta.getPuntosOtorgados());
            }
            
            venta.setActivo(false);
            ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Eliminar venta permanentemente
     */
    public void eliminarVentaPermanentemente(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            
            // Revertir stock de los productos
            revertirStockVenta(venta);
            
            // Revertir puntos de fidelidad si se otorgaron
            if (venta.getPuntosOtorgados() > 0 && venta.getCliente() != null) {
                clienteService.usarPuntosFidelidad(venta.getCliente().getId(), venta.getPuntosOtorgados());
            }
            
            // Eliminar detalles de venta
            List<DetalleVenta> detalles = detalleVentaRepository.findByVenta(venta);
            detalleVentaRepository.deleteAll(detalles);
            
            ventaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Marcar venta como completada
     */
    public Venta marcarComoCompletada(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            venta.setEstado("COMPLETADA");
            
            // Calcular y otorgar puntos de fidelidad
            if (venta.getCliente() != null) {
                venta.calcularPuntosFidelidad();
                if (venta.getPuntosOtorgados() > 0) {
                    clienteService.agregarPuntosFidelidad(venta.getCliente().getId(), venta.getPuntosOtorgados());
                }
            }
            
            return ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Marcar venta como cancelada
     */
    public Venta marcarComoCancelada(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            venta.setEstado("CANCELADA");
            
            // Revertir stock de los productos
            revertirStockVenta(venta);
            
            // Revertir puntos de fidelidad si se otorgaron
            if (venta.getPuntosOtorgados() > 0 && venta.getCliente() != null) {
                clienteService.usarPuntosFidelidad(venta.getCliente().getId(), venta.getPuntosOtorgados());
            }
            
            return ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Marcar venta como devuelta
     */
    public Venta marcarComoDevuelta(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            venta.setEstado("DEVUELTA");
            
            // Revertir stock de los productos
            revertirStockVenta(venta);
            
            // Revertir puntos de fidelidad si se otorgaron
            if (venta.getPuntosOtorgados() > 0 && venta.getCliente() != null) {
                clienteService.usarPuntosFidelidad(venta.getCliente().getId(), venta.getPuntosOtorgados());
            }
            
            return ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Agregar detalle a venta
     */
    public DetalleVenta agregarDetalleVenta(DetalleVenta detalleVenta) {
        // Verificar que la venta existe
        if (!ventaRepository.existsById(detalleVenta.getVenta().getId())) {
            throw new RuntimeException("Venta no encontrada");
        }
        
        // Verificar que hay stock suficiente (solo si hay presentación)
        if (detalleVenta.getPresentacion() != null && !detalleVenta.hayStockSuficiente()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + detalleVenta.getProducto().getNombre());
        }
        
        // Si no hay presentación, verificar stock del producto directamente
        if (detalleVenta.getPresentacion() == null && detalleVenta.getProducto() != null && detalleVenta.getProducto().getId() != null) {
            Integer stockDisponible = inventarioService.obtenerStockProducto(detalleVenta.getProducto().getId());
            if (stockDisponible < detalleVenta.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + detalleVenta.getProducto().getNombre());
            }
        }
        
        // Calcular subtotal
        detalleVenta.calcularSubtotal();
        
        // Guardar detalle
        DetalleVenta detalleGuardado = detalleVentaRepository.save(detalleVenta);
        
        // Actualizar stock del producto
        actualizarStockVenta(detalleVenta);
        
        // Recalcular totales de la venta
        recalcularTotalesVenta(detalleVenta.getVenta().getId());
        
        return detalleGuardado;
    }
    
    /**
     * Eliminar detalle de venta
     */
    public void eliminarDetalleVenta(Long detalleId) {
        Optional<DetalleVenta> detalleOpt = detalleVentaRepository.findById(detalleId);
        if (detalleOpt.isPresent()) {
            DetalleVenta detalle = detalleOpt.get();
            Long ventaId = detalle.getVenta().getId();
            
            // Revertir stock del producto
            revertirStockDetalleVenta(detalle);
            
            // Eliminar detalle
            detalleVentaRepository.deleteById(detalleId);
            
            // Recalcular totales de la venta
            recalcularTotalesVenta(ventaId);
        } else {
            throw new RuntimeException("Detalle de venta no encontrado");
        }
    }
    
    /**
     * Obtener detalles de venta
     */
    public List<DetalleVenta> obtenerDetallesVenta(Long ventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (ventaOpt.isPresent()) {
            return detalleVentaRepository.findByVenta(ventaOpt.get());
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Recalcular totales de venta
     */
    public void recalcularTotalesVenta(Long ventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            List<DetalleVenta> detalles = detalleVentaRepository.findByVenta(venta);
            
            BigDecimal subtotal = detalles.stream()
                    .map(DetalleVenta::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            venta.setSubtotal(subtotal);
            venta.calcularTotal();
            ventaRepository.save(venta);
        }
    }
    
    /**
     * Aplicar descuento por puntos de fidelidad
     */
    public Venta aplicarDescuentoPorPuntos(Long ventaId, Integer puntosUsar) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            
            if (venta.getCliente() == null) {
                throw new RuntimeException("Solo se pueden usar puntos en ventas con cliente registrado");
            }
            
            if (venta.getCliente().getPuntosFidelidad() < puntosUsar) {
                throw new RuntimeException("Puntos insuficientes");
            }
            
            // Calcular descuento (1 punto = 0.10 soles)
            BigDecimal descuento = new BigDecimal(puntosUsar).multiply(new BigDecimal("0.10"));
            
            venta.setPuntosUsados(puntosUsar);
            venta.setDescuentoPuntos(descuento);
            venta.calcularTotal();
            
            return ventaRepository.save(venta);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
    
    /**
     * Actualizar stock al agregar venta
     */
    private void actualizarStockVenta(DetalleVenta detalleVenta) {
        if (detalleVenta.getPresentacion() != null) {
            // Si hay presentación, actualizar stock de la presentación
            if (presentacionService != null) {
                presentacionService.reducirStock(detalleVenta.getPresentacion().getId(), detalleVenta.getCantidad());
            }
        } else {
            // Si no hay presentación, actualizar stock directamente del producto
            if (detalleVenta.getProducto() != null && detalleVenta.getProducto().getId() != null) {
                inventarioService.reducirStockProducto(detalleVenta.getProducto().getId(), detalleVenta.getCantidad());
            } else {
                throw new RuntimeException("No se puede actualizar el stock: el producto no está definido en el detalle");
            }
        }
    }
    
    /**
     * Revertir stock al eliminar venta
     */
    private void revertirStockVenta(Venta venta) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVenta(venta);
        for (DetalleVenta detalle : detalles) {
            revertirStockDetalleVenta(detalle);
        }
    }
    
    /**
     * Revertir stock de un detalle de venta
     */
    private void revertirStockDetalleVenta(DetalleVenta detalleVenta) {
        if (detalleVenta.getPresentacion() != null) {
            // Si hay presentación, revertir stock de la presentación
            if (presentacionService != null) {
                presentacionService.agregarStock(detalleVenta.getPresentacion().getId(), detalleVenta.getCantidad());
            }
        } else {
            // Si no hay presentación, revertir stock directamente del producto
            if (detalleVenta.getProducto() != null && detalleVenta.getProducto().getId() != null) {
                inventarioService.agregarStockProducto(detalleVenta.getProducto().getId(), detalleVenta.getCantidad());
            } else {
                throw new RuntimeException("No se puede revertir el stock: el producto no está definido en el detalle");
            }
        }
    }
    
    /**
     * Generar número de venta automático
     */
    private String generarNumeroVenta() {
        long count = ventaRepository.count() + 1;
        return "VENT-" + String.format("%06d", count);
    }
    
    /**
     * Verificar si existe una venta con el número
     */
    public boolean existeVentaConNumero(String numeroVenta) {
        return ventaRepository.existsByNumeroVenta(numeroVenta);
    }
    
    /**
     * Contar ventas por estado
     */
    public long contarVentasPorEstado(String estado) {
        return ventaRepository.countByEstado(estado);
    }
    
    /**
     * Contar ventas activas
     */
    public long contarVentasActivas() {
        return ventaRepository.countByActivoTrue();
    }
    
    /**
     * Obtener total de ventas por cliente
     */
    public BigDecimal obtenerTotalVentasPorCliente(Cliente cliente) {
        return ventaRepository.sumTotalByCliente(cliente);
    }
    
    /**
     * Obtener total de ventas por usuario
     */
    public BigDecimal obtenerTotalVentasPorUsuario(Usuario usuario) {
        return ventaRepository.sumTotalByUsuario(usuario);
    }
    
    /**
     * Obtener total de ventas en un rango de fechas
     */
    public BigDecimal obtenerTotalVentasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.sumTotalByFechaVentaBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener total de ventas por método de pago
     */
    public BigDecimal obtenerTotalVentasPorMetodoPago(String metodoPago) {
        return ventaRepository.sumTotalByMetodoPago(metodoPago);
    }
}
