package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Presentacion;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PresentacionService {
    
    @Autowired
    private PresentacionRepository presentacionRepository;
    
    /**
     * Obtener todas las presentaciones
     */
    public List<Presentacion> obtenerTodasLasPresentaciones() {
        return presentacionRepository.findAll();
    }
    
    /**
     * Obtener todas las presentaciones activas
     */
    public List<Presentacion> obtenerPresentacionesActivas() {
        return presentacionRepository.findByActivoTrue();
    }
    
    /**
     * Obtener presentación por ID
     */
    public Optional<Presentacion> obtenerPresentacionPorId(Long id) {
        return presentacionRepository.findById(id);
    }
    
    /**
     * Obtener presentaciones por producto
     */
    public List<Presentacion> obtenerPresentacionesPorProducto(Producto producto) {
        return presentacionRepository.findByProducto(producto);
    }
    
    /**
     * Obtener presentación por producto, talla y color
     */
    public Optional<Presentacion> obtenerPresentacionPorProductoTallaYColor(Producto producto, String talla, String color) {
        return presentacionRepository.findByProductoAndTallaAndColor(producto, talla, color);
    }
    
    /**
     * Obtener presentaciones por talla
     */
    public List<Presentacion> obtenerPresentacionesPorTalla(String talla) {
        return presentacionRepository.findByTalla(talla);
    }
    
    /**
     * Obtener presentaciones por color
     */
    public List<Presentacion> obtenerPresentacionesPorColor(String color) {
        return presentacionRepository.findByColorContainingIgnoreCase(color);
    }
    
    /**
     * Obtener presentaciones con stock
     */
    public List<Presentacion> obtenerPresentacionesConStock() {
        return presentacionRepository.findPresentacionesConStock();
    }
    
    /**
     * Obtener presentaciones sin stock
     */
    public List<Presentacion> obtenerPresentacionesSinStock() {
        return presentacionRepository.findPresentacionesSinStock();
    }
    
    /**
     * Obtener presentaciones con stock bajo
     */
    public List<Presentacion> obtenerPresentacionesConStockBajo() {
        return presentacionRepository.findPresentacionesConStockBajo();
    }
    
    /**
     * Obtener presentaciones destacadas
     */
    public List<Presentacion> obtenerPresentacionesDestacadas() {
        return presentacionRepository.findByEsDestacadoTrue();
    }
    
    /**
     * Obtener presentaciones por ubicación en almacén
     */
    public List<Presentacion> obtenerPresentacionesPorUbicacion(String ubicacionAlmacen) {
        return presentacionRepository.findByUbicacionAlmacen(ubicacionAlmacen);
    }
    
    /**
     * Obtener presentaciones con precio especial
     */
    public List<Presentacion> obtenerPresentacionesConPrecioEspecial() {
        return presentacionRepository.findPresentacionesConPrecioEspecial();
    }
    
    /**
     * Obtener presentaciones por rango de stock
     */
    public List<Presentacion> obtenerPresentacionesPorRangoStock(Integer stockMin, Integer stockMax) {
        return presentacionRepository.findByStockDisponibleBetween(stockMin, stockMax);
    }
    
    /**
     * Obtener presentaciones por rango de precio especial
     */
    public List<Presentacion> obtenerPresentacionesPorRangoPrecioEspecial(BigDecimal precioMin, BigDecimal precioMax) {
        return presentacionRepository.findByPrecioEspecialBetween(precioMin, precioMax);
    }
    
    /**
     * Obtener presentaciones más vendidas
     */
    public List<Presentacion> obtenerPresentacionesMasVendidas() {
        return presentacionRepository.findPresentacionesMasVendidas();
    }
    
    /**
     * Obtener presentaciones ordenadas por stock descendente
     */
    public List<Presentacion> obtenerPresentacionesOrdenadasPorStockDesc() {
        return presentacionRepository.findByActivoTrueOrderByStockDisponibleDesc();
    }
    
    /**
     * Obtener presentaciones ordenadas por stock ascendente
     */
    public List<Presentacion> obtenerPresentacionesOrdenadasPorStockAsc() {
        return presentacionRepository.findByActivoTrueOrderByStockDisponibleAsc();
    }
    
    /**
     * Obtener tallas disponibles para un producto
     */
    public List<String> obtenerTallasDisponiblesPorProducto(Producto producto) {
        return presentacionRepository.findTallasDisponiblesPorProducto(producto);
    }
    
    /**
     * Obtener colores disponibles para un producto
     */
    public List<String> obtenerColoresDisponiblesPorProducto(Producto producto) {
        return presentacionRepository.findColoresDisponiblesPorProducto(producto);
    }
    
    /**
     * Obtener colores disponibles para un producto y talla
     */
    public List<String> obtenerColoresDisponiblesPorProductoYTalla(Producto producto, String talla) {
        return presentacionRepository.findColoresDisponiblesPorProductoYTalla(producto, talla);
    }
    
    /**
     * Obtener tallas disponibles para un producto y color
     */
    public List<String> obtenerTallasDisponiblesPorProductoYColor(Producto producto, String color) {
        return presentacionRepository.findTallasDisponiblesPorProductoYColor(producto, color);
    }
    
    /**
     * Guardar presentación
     */
    public Presentacion guardarPresentacion(Presentacion presentacion) {
        // Verificar si ya existe una presentación con el mismo producto, talla y color
        if (presentacionRepository.findByProductoAndTallaAndColor(
                presentacion.getProducto(), 
                presentacion.getTalla(), 
                presentacion.getColor()).isPresent()) {
            throw new RuntimeException("Ya existe una presentación con este producto, talla y color");
        }
        
        return presentacionRepository.save(presentacion);
    }
    
    /**
     * Actualizar presentación
     */
    public Presentacion actualizarPresentacion(Presentacion presentacion) {
        if (!presentacionRepository.existsById(presentacion.getId())) {
            throw new RuntimeException("Presentación no encontrada");
        }
        
        // Verificar si ya existe otra presentación con el mismo producto, talla y color
        Optional<Presentacion> presentacionExistente = presentacionRepository.findByProductoAndTallaAndColor(
                presentacion.getProducto(), 
                presentacion.getTalla(), 
                presentacion.getColor());
        if (presentacionExistente.isPresent() && !presentacionExistente.get().getId().equals(presentacion.getId())) {
            throw new RuntimeException("Ya existe una presentación con este producto, talla y color");
        }
        
        return presentacionRepository.save(presentacion);
    }
    
    /**
     * Eliminar presentación (soft delete)
     */
    public void eliminarPresentacion(Long id) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setActivo(false);
            presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Eliminar presentación permanentemente
     */
    public void eliminarPresentacionPermanentemente(Long id) {
        if (!presentacionRepository.existsById(id)) {
            throw new RuntimeException("Presentación no encontrada");
        }
        presentacionRepository.deleteById(id);
    }
    
    /**
     * Activar presentación
     */
    public Presentacion activarPresentacion(Long id) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setActivo(true);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Desactivar presentación
     */
    public Presentacion desactivarPresentacion(Long id) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setActivo(false);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Marcar presentación como destacada
     */
    public Presentacion marcarComoDestacada(Long id) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setEsDestacado(true);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Desmarcar presentación como destacada
     */
    public Presentacion desmarcarComoDestacada(Long id) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setEsDestacado(false);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Actualizar stock disponible
     */
    public Presentacion actualizarStock(Long id, Integer nuevoStock) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setStockDisponible(nuevoStock);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Agregar stock
     */
    public Presentacion agregarStock(Long id, Integer cantidad) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setStockDisponible(presentacion.getStockDisponible() + cantidad);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Reducir stock
     */
    public Presentacion reducirStock(Long id, Integer cantidad) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            if (presentacion.getStockDisponible() >= cantidad) {
                presentacion.setStockDisponible(presentacion.getStockDisponible() - cantidad);
                return presentacionRepository.save(presentacion);
            } else {
                throw new RuntimeException("Stock insuficiente");
            }
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Actualizar precio especial
     */
    public Presentacion actualizarPrecioEspecial(Long id, BigDecimal precioEspecial) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setPrecioEspecial(precioEspecial);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Actualizar ubicación en almacén
     */
    public Presentacion actualizarUbicacionAlmacen(Long id, String ubicacion) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            Presentacion presentacion = presentacionOpt.get();
            presentacion.setUbicacionAlmacen(ubicacion);
            return presentacionRepository.save(presentacion);
        } else {
            throw new RuntimeException("Presentación no encontrada");
        }
    }
    
    /**
     * Verificar si hay stock suficiente
     */
    public boolean hayStockSuficiente(Long id, Integer cantidad) {
        Optional<Presentacion> presentacionOpt = presentacionRepository.findById(id);
        if (presentacionOpt.isPresent()) {
            return presentacionOpt.get().getStockDisponible() >= cantidad;
        }
        return false;
    }
    
    /**
     * Contar presentaciones activas
     */
    public long contarPresentacionesActivas() {
        return presentacionRepository.countByActivoTrue();
    }
    
    /**
     * Contar presentaciones con stock
     */
    public long contarPresentacionesConStock() {
        return presentacionRepository.countPresentacionesConStock();
    }
    
    /**
     * Contar presentaciones sin stock
     */
    public long contarPresentacionesSinStock() {
        return presentacionRepository.countPresentacionesSinStock();
    }
    
    /**
     * Contar presentaciones por producto
     */
    public long contarPresentacionesPorProducto(Producto producto) {
        return presentacionRepository.countByProducto(producto);
    }
}
