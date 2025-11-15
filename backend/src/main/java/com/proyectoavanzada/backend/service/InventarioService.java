package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Inventario;
import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.repository.InventarioRepository;
import com.proyectoavanzada.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioService {
    
    @Autowired
    private InventarioRepository inventarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * Agregar stock a un producto (sin presentación específica)
     * Si no existe inventario para el producto, lo crea
     */
    public void agregarStockProducto(Long productoId, Integer cantidad) {
        System.out.println("=== AGREGAR STOCK PRODUCTO ===");
        System.out.println("Producto ID: " + productoId);
        System.out.println("Cantidad a agregar: " + cantidad);
        
        if (productoId == null) {
            throw new RuntimeException("El ID del producto no puede ser null");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (!productoOpt.isPresent()) {
            throw new RuntimeException("Producto no encontrado con ID: " + productoId);
        }
        
        Producto producto = productoOpt.get();
        System.out.println("Producto encontrado: " + producto.getNombre());
        
        // Buscar inventario existente sin presentación específica
        List<Inventario> inventarios = inventarioRepository.findByProducto(producto);
        System.out.println("Inventarios encontrados: " + inventarios.size());
        
        Inventario inventario = null;
        
        // Buscar un inventario sin presentación o con presentación null
        for (Inventario inv : inventarios) {
            if (inv.getPresentacion() == null) {
                inventario = inv;
                System.out.println("Inventario existente encontrado. ID: " + inv.getId() + ", Stock actual: " + inv.getStockActual());
                break;
            }
        }
        
        // Si no existe, crear uno nuevo
        if (inventario == null) {
            System.out.println("Creando nuevo inventario para el producto");
            inventario = new Inventario();
            inventario.setProducto(producto);
            inventario.setPresentacion(null); // Sin presentación específica
            inventario.setStockActual(0);
            inventario.setStockMinimo(producto.getStockMinimo() != null ? producto.getStockMinimo() : 5);
            inventario.setActivo(true);
            inventario.setFechaCreacion(java.time.LocalDateTime.now());
        }
        
        // Agregar stock
        Integer stockAnterior = inventario.getStockActual();
        inventario.agregarStock(cantidad);
        System.out.println("Stock anterior: " + stockAnterior + ", Stock nuevo: " + inventario.getStockActual());
        
        // Guardar el inventario
        try {
            Inventario inventarioGuardado = inventarioRepository.saveAndFlush(inventario);
            System.out.println("Inventario guardado exitosamente. ID: " + inventarioGuardado.getId() + ", Stock: " + inventarioGuardado.getStockActual());
            
            // Verificar que se guardó correctamente
            Optional<Inventario> inventarioVerificado = inventarioRepository.findById(inventarioGuardado.getId());
            if (inventarioVerificado.isPresent()) {
                System.out.println("Inventario verificado en BD. ID: " + inventarioVerificado.get().getId() + ", Stock: " + inventarioVerificado.get().getStockActual());
            } else {
                System.err.println("ERROR: El inventario no se encontró después de guardar!");
            }
        } catch (Exception e) {
            System.err.println("ERROR al guardar inventario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el inventario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Reducir stock de un producto
     */
    public void reducirStockProducto(Long productoId, Integer cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (!productoOpt.isPresent()) {
            throw new RuntimeException("Producto no encontrado");
        }
        
        Producto producto = productoOpt.get();
        List<Inventario> inventarios = inventarioRepository.findByProducto(producto);
        
        // Buscar inventario sin presentación
        Inventario inventario = null;
        for (Inventario inv : inventarios) {
            if (inv.getPresentacion() == null) {
                inventario = inv;
                break;
            }
        }
        
        if (inventario == null || !inventario.hayStockSuficiente(cantidad)) {
            throw new RuntimeException("Stock insuficiente para el producto");
        }
        
        inventario.reducirStock(cantidad);
        inventarioRepository.save(inventario);
    }
    
    /**
     * Obtener stock total de un producto
     */
    public Integer obtenerStockProducto(Long productoId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (!productoOpt.isPresent()) {
            return 0;
        }
        
        Producto producto = productoOpt.get();
        Integer stockTotal = inventarioRepository.sumStockActualByProducto(producto);
        return stockTotal != null ? stockTotal : 0;
    }
}

