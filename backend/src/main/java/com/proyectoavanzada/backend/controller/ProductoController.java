package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Categoria;
import com.proyectoavanzada.backend.model.Marca;
import com.proyectoavanzada.backend.service.ProductoService;
import com.proyectoavanzada.backend.service.CategoriaService;
import com.proyectoavanzada.backend.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private MarcaService marcaService;
    
    /**
     * Obtener todos los productos
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosProductos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todos los productos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerProductosActivos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosActivos();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos activos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerProductoPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Producto> productoOpt = productoService.obtenerProductoPorId(id);
            if (productoOpt.isPresent()) {
                response.put("success", true);
                response.put("data", productoOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener producto por código
     */
    @GetMapping("/codigo/{codigoProducto}")
    public ResponseEntity<Map<String, Object>> obtenerProductoPorCodigo(@PathVariable String codigoProducto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Producto> productoOpt = productoService.obtenerProductoPorCodigo(codigoProducto);
            if (productoOpt.isPresent()) {
                response.put("success", true);
                response.put("data", productoOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Buscar productos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarProductosPorNombre(@RequestParam String nombre) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos por categoría
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Map<String, Object>> obtenerProductosPorCategoria(@PathVariable Long categoriaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Categoria> categoriaOpt = categoriaService.obtenerCategoriaPorId(categoriaId);
            if (categoriaOpt.isPresent()) {
                List<Producto> productos = productoService.obtenerProductosPorCategoria(categoriaOpt.get());
                response.put("success", true);
                response.put("data", productos);
                response.put("total", productos.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos por categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos por marca
     */
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<Map<String, Object>> obtenerProductosPorMarca(@PathVariable Long marcaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Marca> marcaOpt = marcaService.obtenerMarcaPorId(marcaId);
            if (marcaOpt.isPresent()) {
                List<Producto> productos = productoService.obtenerProductosPorMarca(marcaOpt.get());
                response.put("success", true);
                response.put("data", productos);
                response.put("total", productos.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Marca no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos por marca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos destacados
     */
    @GetMapping("/destacados")
    public ResponseEntity<Map<String, Object>> obtenerProductosDestacados() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosDestacados();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos destacados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos nuevos
     */
    @GetMapping("/nuevos")
    public ResponseEntity<Map<String, Object>> obtenerProductosNuevos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosNuevos();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos nuevos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos con descuento
     */
    @GetMapping("/con-descuento")
    public ResponseEntity<Map<String, Object>> obtenerProductosConDescuento() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosConDescuento();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos con descuento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<Map<String, Object>> obtenerProductosConStockBajo() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosConStockBajo();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos con stock bajo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos más vendidos
     */
    @GetMapping("/mas-vendidos")
    public ResponseEntity<Map<String, Object>> obtenerProductosMasVendidos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosMasVendidos();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos más vendidos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos por rango de precio
     */
    @GetMapping("/rango-precio")
    public ResponseEntity<Map<String, Object>> obtenerProductosPorRangoPrecio(
            @RequestParam BigDecimal precioMin, 
            @RequestParam BigDecimal precioMax) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosPorRangoPrecio(precioMin, precioMax);
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos por rango de precio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos ordenados por precio ascendente
     */
    @GetMapping("/ordenados-precio-asc")
    public ResponseEntity<Map<String, Object>> obtenerProductosOrdenadosPorPrecioAsc() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosOrdenadosPorPrecioAsc();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos ordenados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener productos ordenados por precio descendente
     */
    @GetMapping("/ordenados-precio-desc")
    public ResponseEntity<Map<String, Object>> obtenerProductosOrdenadosPorPrecioDesc() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerProductosOrdenadosPorPrecioDesc();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos ordenados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear nuevo producto
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearProducto(@Valid @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto productoGuardado = productoService.guardarProducto(producto);
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("data", productoGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar producto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        try {
            producto.setId(id);
            Producto productoActualizado = productoService.actualizarProducto(producto);
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("data", productoActualizado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar producto (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoService.eliminarProducto(id);
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Activar producto
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Map<String, Object>> activarProducto(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.activarProducto(id);
            response.put("success", true);
            response.put("message", "Producto activado exitosamente");
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al activar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Desactivar producto
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivarProducto(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.desactivarProducto(id);
            response.put("success", true);
            response.put("message", "Producto desactivado exitosamente");
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al desactivar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Marcar producto como destacado
     */
    @PutMapping("/{id}/destacar")
    public ResponseEntity<Map<String, Object>> marcarComoDestacado(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.marcarComoDestacado(id);
            response.put("success", true);
            response.put("message", "Producto marcado como destacado");
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al marcar producto como destacado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Desmarcar producto como destacado
     */
    @PutMapping("/{id}/no-destacar")
    public ResponseEntity<Map<String, Object>> desmarcarComoDestacado(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.desmarcarComoDestacado(id);
            response.put("success", true);
            response.put("message", "Producto desmarcado como destacado");
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al desmarcar producto como destacado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar precio de venta
     */
    @PutMapping("/{id}/precio-venta")
    public ResponseEntity<Map<String, Object>> actualizarPrecioVenta(@PathVariable Long id, @RequestParam BigDecimal precio) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.actualizarPrecioVenta(id, precio);
            response.put("success", true);
            response.put("message", "Precio de venta actualizado exitosamente");
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar precio de venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar descuento
     */
    @PutMapping("/{id}/descuento")
    public ResponseEntity<Map<String, Object>> actualizarDescuento(@PathVariable Long id, @RequestParam BigDecimal descuento) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.actualizarDescuento(id, descuento);
            response.put("success", true);
            response.put("message", "Descuento actualizado exitosamente");
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar descuento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Verificar si existe un producto con el código
     */
    @GetMapping("/verificar-codigo")
    public ResponseEntity<Map<String, Object>> verificarCodigo(@RequestParam String codigoProducto) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean existe = productoService.existeProductoConCodigo(codigoProducto);
            response.put("success", true);
            response.put("existe", existe);
            response.put("message", existe ? "Código ya registrado" : "Código disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al verificar código: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener estadísticas de productos
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivos = productoService.contarProductosActivos();
            
            response.put("success", true);
            response.put("data", Map.of(
                "totalActivos", totalActivos
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
