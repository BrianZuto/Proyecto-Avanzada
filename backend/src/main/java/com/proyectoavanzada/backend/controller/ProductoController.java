package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Producto;
import com.proyectoavanzada.backend.model.Categoria;
import com.proyectoavanzada.backend.model.Marca;
import com.proyectoavanzada.backend.service.ProductoService;
import com.proyectoavanzada.backend.service.CategoriaService;
import com.proyectoavanzada.backend.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Gestión de Productos", description = "Endpoints para la gestión completa de productos del sistema de venta de tenis")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private com.proyectoavanzada.backend.service.InventarioService inventarioService;
    
    /**
     * Obtener todos los productos
     */
    @Operation(
        summary = "Obtener todos los productos",
        description = "Retorna una lista con todos los productos del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"data\": [], \"total\": 0}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosProductos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            
            // Agregar stock calculado desde inventario a cada producto
            List<Map<String, Object>> productosConStock = productos.stream().map(producto -> {
                Map<String, Object> productoMap = new HashMap<>();
                productoMap.put("id", producto.getId());
                productoMap.put("nombre", producto.getNombre());
                productoMap.put("descripcion", producto.getDescripcion());
                productoMap.put("codigoProducto", producto.getCodigoProducto());
                productoMap.put("categoriaId", producto.getCategoriaId());
                productoMap.put("marcaId", producto.getMarcaId());
                productoMap.put("precioVenta", producto.getPrecioVenta());
                productoMap.put("precioCompra", producto.getPrecioCompra());
                productoMap.put("imagenPrincipal", producto.getImagenPrincipal());
                productoMap.put("imagenesAdicionales", producto.getImagenesAdicionales());
                productoMap.put("genero", producto.getGenero());
                productoMap.put("edadTarget", producto.getEdadTarget());
                productoMap.put("materialPrincipal", producto.getMaterialPrincipal());
                productoMap.put("tipoSuela", producto.getTipoSuela());
                productoMap.put("tecnologia", producto.getTecnologia());
                productoMap.put("pesoGramos", producto.getPesoGramos());
                productoMap.put("garantiaMeses", producto.getGarantiaMeses());
                productoMap.put("stockMinimo", producto.getStockMinimo());
                productoMap.put("esDestacado", producto.getEsDestacado());
                productoMap.put("esNuevo", producto.getEsNuevo());
                productoMap.put("descuentoPorcentaje", producto.getDescuentoPorcentaje());
                productoMap.put("activo", producto.getActivo());
                productoMap.put("fechaCreacion", producto.getFechaCreacion());
                productoMap.put("fechaActualizacion", producto.getFechaActualizacion());
                
                // Calcular stock desde inventario
                Integer stock = inventarioService.obtenerStockProducto(producto.getId());
                productoMap.put("stock", stock);
                
                // Agregar información de categoría y marca si están disponibles
                if (producto.getCategoria() != null) {
                    Map<String, Object> categoriaMap = new HashMap<>();
                    categoriaMap.put("id", producto.getCategoria().getId());
                    categoriaMap.put("nombre", producto.getCategoria().getNombre());
                    productoMap.put("categoria", categoriaMap);
                }
                if (producto.getMarca() != null) {
                    Map<String, Object> marcaMap = new HashMap<>();
                    marcaMap.put("id", producto.getMarca().getId());
                    marcaMap.put("nombre", producto.getMarca().getNombre());
                    productoMap.put("marca", marcaMap);
                }
                
                return productoMap;
            }).collect(java.util.stream.Collectors.toList());
            
            response.put("success", true);
            response.put("data", productosConStock);
            response.put("total", productosConStock.size());
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
    @Operation(
        summary = "Obtener producto por ID",
        description = "Retorna un producto específico basado en su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"data\": {\"id\": 1, \"nombre\": \"Nike Air Max\", \"precioVenta\": 150.00}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerProductoPorId(
        @Parameter(description = "ID del producto", required = true)
        @PathVariable Long id) {
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
    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo producto en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Producto creado exitosamente\", \"data\": {\"id\": 1, \"nombre\": \"Nike Air Max\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearProducto(
        @Parameter(description = "Datos del producto a crear", required = true)
        @Valid @RequestBody Producto producto) {
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
    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza los datos de un producto existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Producto actualizado exitosamente\", \"data\": {\"id\": 1, \"nombre\": \"Nike Air Max Actualizado\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarProducto(
        @Parameter(description = "ID del producto a actualizar", required = true)
        @PathVariable Long id, 
        @Parameter(description = "Datos actualizados del producto", required = true)
        @Valid @RequestBody Producto producto) {
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
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del sistema (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Producto eliminado exitosamente\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error al eliminar producto"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(
        @Parameter(description = "ID del producto a eliminar", required = true)
        @PathVariable Long id) {
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
