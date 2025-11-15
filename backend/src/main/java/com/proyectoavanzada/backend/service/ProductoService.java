package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Categoria;
import com.proyectoavanzada.backend.model.Marca;
import com.proyectoavanzada.backend.repository.ProductoRepository;
import com.proyectoavanzada.backend.repository.CategoriaRepository;
import com.proyectoavanzada.backend.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    /**
     * Obtener todos los productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
    
    /**
     * Obtener todos los productos activos
     */
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue();
    }
    
    /**
     * Obtener producto por ID
     */
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    /**
     * Obtener producto por código
     */
    public Optional<Producto> obtenerProductoPorCodigo(String codigoProducto) {
        return productoRepository.findByCodigoProducto(codigoProducto);
    }
    
    /**
     * Buscar productos por nombre
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Obtener productos por categoría
     */
    public List<Producto> obtenerProductosPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    /**
     * Obtener productos por marca
     */
    public List<Producto> obtenerProductosPorMarca(Marca marca) {
        return productoRepository.findByMarca(marca);
    }
    
    /**
     * Obtener productos por categoría y marca
     */
    public List<Producto> obtenerProductosPorCategoriaYMarca(Categoria categoria, Marca marca) {
        return productoRepository.findByCategoriaAndMarca(categoria, marca);
    }
    
    /**
     * Obtener productos destacados
     */
    public List<Producto> obtenerProductosDestacados() {
        return productoRepository.findByEsDestacadoTrue();
    }
    
    /**
     * Obtener productos nuevos
     */
    public List<Producto> obtenerProductosNuevos() {
        return productoRepository.findByEsNuevoTrue();
    }
    
    /**
     * Obtener productos por género
     */
    public List<Producto> obtenerProductosPorGenero(String genero) {
        return productoRepository.findByGenero(genero);
    }
    
    /**
     * Obtener productos por edad target
     */
    public List<Producto> obtenerProductosPorEdadTarget(String edadTarget) {
        return productoRepository.findByEdadTarget(edadTarget);
    }
    
    /**
     * Obtener productos por rango de precio
     */
    public List<Producto> obtenerProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioVentaBetween(precioMin, precioMax);
    }
    
    /**
     * Obtener productos con descuento
     */
    public List<Producto> obtenerProductosConDescuento() {
        return productoRepository.findProductosConDescuento();
    }
    
    /**
     * Obtener productos con stock bajo
     */
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }
    
    /**
     * Obtener productos más vendidos
     */
    public List<Producto> obtenerProductosMasVendidos() {
        return productoRepository.findProductosMasVendidos();
    }
    
    /**
     * Buscar productos por material
     */
    public List<Producto> buscarProductosPorMaterial(String material) {
        return productoRepository.findByMaterialPrincipalContainingIgnoreCase(material);
    }
    
    /**
     * Buscar productos por tecnología
     */
    public List<Producto> buscarProductosPorTecnologia(String tecnologia) {
        return productoRepository.findByTecnologiaContainingIgnoreCase(tecnologia);
    }
    
    /**
     * Buscar productos por tipo de suela
     */
    public List<Producto> buscarProductosPorTipoSuela(String tipoSuela) {
        return productoRepository.findByTipoSuelaContainingIgnoreCase(tipoSuela);
    }
    
    /**
     * Obtener productos creados en un rango de fechas
     */
    public List<Producto> obtenerProductosPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return productoRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener productos actualizados recientemente
     */
    public List<Producto> obtenerProductosActualizadosRecientemente() {
        return productoRepository.findProductosActualizadosRecientemente();
    }
    
    /**
     * Obtener productos por peso
     */
    public List<Producto> obtenerProductosPorPeso(Integer pesoMin, Integer pesoMax) {
        return productoRepository.findByPesoGramosBetween(pesoMin, pesoMax);
    }
    
    /**
     * Obtener productos con garantía
     */
    public List<Producto> obtenerProductosConGarantia() {
        return productoRepository.findProductosConGarantia();
    }
    
    /**
     * Obtener productos ordenados por precio ascendente
     */
    public List<Producto> obtenerProductosOrdenadosPorPrecioAsc() {
        return productoRepository.findByActivoTrueOrderByPrecioVentaAsc();
    }
    
    /**
     * Obtener productos ordenados por precio descendente
     */
    public List<Producto> obtenerProductosOrdenadosPorPrecioDesc() {
        return productoRepository.findByActivoTrueOrderByPrecioVentaDesc();
    }
    
    /**
     * Obtener productos ordenados por nombre
     */
    public List<Producto> obtenerProductosOrdenadosPorNombre() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }
    
    /**
     * Guardar producto
     */
    public Producto guardarProducto(Producto producto) {
        // Cargar categoría desde la base de datos si solo se proporciona el ID
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + producto.getCategoria().getId()));
            producto.setCategoria(categoria);
        } else if (producto.getCategoria() == null) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        
        // Cargar marca desde la base de datos si solo se proporciona el ID
        if (producto.getMarca() != null && producto.getMarca().getId() != null) {
            Marca marca = marcaRepository.findById(producto.getMarca().getId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + producto.getMarca().getId()));
            producto.setMarca(marca);
        } else if (producto.getMarca() == null) {
            throw new RuntimeException("La marca es obligatoria");
        }
        
        // Verificar si el código de producto ya existe
        if (producto.getCodigoProducto() != null && productoRepository.existsByCodigoProducto(producto.getCodigoProducto())) {
            throw new RuntimeException("Ya existe un producto con este código");
        }
        
        // Generar código automático si no se proporciona
        if (producto.getCodigoProducto() == null || producto.getCodigoProducto().isEmpty()) {
            producto.setCodigoProducto(generarCodigoProducto(producto));
        }
        
        return productoRepository.save(producto);
    }
    
    /**
     * Actualizar producto
     */
    public Producto actualizarProducto(Producto producto) {
        Optional<Producto> productoExistenteOpt = productoRepository.findById(producto.getId());
        if (!productoExistenteOpt.isPresent()) {
            throw new RuntimeException("Producto no encontrado");
        }
        
        Producto productoExistente = productoExistenteOpt.get();
        
        // Cargar categoría desde la base de datos si solo se proporciona el ID
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + producto.getCategoria().getId()));
            productoExistente.setCategoria(categoria);
        }
        
        // Cargar marca desde la base de datos si solo se proporciona el ID
        if (producto.getMarca() != null && producto.getMarca().getId() != null) {
            Marca marca = marcaRepository.findById(producto.getMarca().getId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + producto.getMarca().getId()));
            productoExistente.setMarca(marca);
        }
        
        // Actualizar otros campos
        if (producto.getNombre() != null) {
            productoExistente.setNombre(producto.getNombre());
        }
        if (producto.getDescripcion() != null) {
            productoExistente.setDescripcion(producto.getDescripcion());
        }
        if (producto.getPrecioVenta() != null) {
            productoExistente.setPrecioVenta(producto.getPrecioVenta());
        }
        if (producto.getPrecioCompra() != null) {
            productoExistente.setPrecioCompra(producto.getPrecioCompra());
        }
        if (producto.getImagenPrincipal() != null) {
            productoExistente.setImagenPrincipal(producto.getImagenPrincipal());
        }
        if (producto.getActivo() != null) {
            productoExistente.setActivo(producto.getActivo());
        }
        
        // Verificar si el código de producto ya existe en otro producto
        if (producto.getCodigoProducto() != null) {
            Optional<Producto> productoConCodigo = productoRepository.findByCodigoProducto(producto.getCodigoProducto());
            if (productoConCodigo.isPresent() && !productoConCodigo.get().getId().equals(producto.getId())) {
                throw new RuntimeException("Ya existe un producto con este código");
            }
            productoExistente.setCodigoProducto(producto.getCodigoProducto());
        }
        
        // Actualizar fecha de modificación
        productoExistente.setFechaActualizacion(LocalDateTime.now());
        
        return productoRepository.save(productoExistente);
    }
    
    /**
     * Eliminar producto (soft delete)
     */
    public void eliminarProducto(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setActivo(false);
            producto.setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Eliminar producto permanentemente
     */
    public void eliminarProductoPermanentemente(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }
    
    /**
     * Activar producto
     */
    public Producto activarProducto(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setActivo(true);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Desactivar producto
     */
    public Producto desactivarProducto(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setActivo(false);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Marcar producto como destacado
     */
    public Producto marcarComoDestacado(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setEsDestacado(true);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Desmarcar producto como destacado
     */
    public Producto desmarcarComoDestacado(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setEsDestacado(false);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Marcar producto como nuevo
     */
    public Producto marcarComoNuevo(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setEsNuevo(true);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Desmarcar producto como nuevo
     */
    public Producto desmarcarComoNuevo(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setEsNuevo(false);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Actualizar precio de venta
     */
    public Producto actualizarPrecioVenta(Long id, BigDecimal nuevoPrecio) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setPrecioVenta(nuevoPrecio);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Actualizar precio de compra
     */
    public Producto actualizarPrecioCompra(Long id, BigDecimal nuevoPrecio) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setPrecioCompra(nuevoPrecio);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Actualizar descuento
     */
    public Producto actualizarDescuento(Long id, BigDecimal descuentoPorcentaje) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setDescuentoPorcentaje(descuentoPorcentaje);
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
    
    /**
     * Verificar si existe un producto con el código
     */
    public boolean existeProductoConCodigo(String codigoProducto) {
        return productoRepository.existsByCodigoProducto(codigoProducto);
    }
    
    /**
     * Contar productos activos
     */
    public long contarProductosActivos() {
        return productoRepository.countByActivoTrue();
    }
    
    /**
     * Contar productos por categoría
     */
    public long contarProductosPorCategoria(Categoria categoria) {
        return productoRepository.countByCategoria(categoria);
    }
    
    /**
     * Contar productos por marca
     */
    public long contarProductosPorMarca(Marca marca) {
        return productoRepository.countByMarca(marca);
    }
    
    /**
     * Generar código de producto automático
     */
    private String generarCodigoProducto(Producto producto) {
        String prefijo = "";
        if (producto.getCategoria() != null) {
            prefijo += producto.getCategoria().getNombre().substring(0, Math.min(3, producto.getCategoria().getNombre().length())).toUpperCase();
        }
        if (producto.getMarca() != null) {
            prefijo += producto.getMarca().getNombre().substring(0, Math.min(3, producto.getMarca().getNombre().length())).toUpperCase();
        }
        
        long count = productoRepository.count() + 1;
        return prefijo + String.format("%04d", count);
    }
}
