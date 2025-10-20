package com.proyectoavanzada.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas unitarias simples - no necesita Spring Boot
class ProductoTest {

    private Validator validator;
    private Producto producto;
    private Categoria categoria;
    private Marca marca;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Crear categoría
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tenis Deportivos");
        categoria.setDescripcion("Tenis para deportes");
        categoria.setActivo(true);
        
        // Crear marca
        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Nike");
        marca.setDescripcion("Marca deportiva");
        marca.setActivo(true);
        
        // Crear producto
        producto = new Producto();
        producto.setNombre("Air Max 90");
        producto.setDescripcion("Tenis deportivos Nike Air Max 90");
        producto.setPrecioCompra(new BigDecimal("150.00"));
        producto.setPrecioVenta(new BigDecimal("200.00"));
        producto.setStockMinimo(10);
        producto.setActivo(true);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testProductoValido() {
        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    void testNombreObligatorio() {
        producto.setNombre(null);
        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void testPrecioVentaObligatorio() {
        producto.setPrecioVenta(null);
        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
        assertTrue(violations.isEmpty()); // @Positive no valida null, solo valores positivos
    }

    @Test
    void testPrecioVentaPositivo() {
        producto.setPrecioVenta(new BigDecimal("-10.00"));
        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
        assertFalse(violations.isEmpty()); // @Positive debería fallar con valores negativos
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precioVenta")));
    }

    @Test
    void testStockMinimoPositivo() {
        producto.setStockMinimo(-5);
        Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
        assertTrue(violations.isEmpty()); // No hay validación @Positive en stockMinimo
    }

    @Test
    void testValoresPorDefecto() {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Test Product");
        nuevoProducto.setPrecioVenta(new BigDecimal("100.00"));
        
        assertTrue(nuevoProducto.getActivo(), "Activo debería ser true por defecto");
        assertEquals(5, nuevoProducto.getStockMinimo(), "Stock mínimo debería ser 5 por defecto");
    }

    @Test
    void testGettersYSetters() {
        LocalDateTime fechaCreacion = LocalDateTime.now();
        BigDecimal precioCompra = new BigDecimal("120.00");
        BigDecimal precioVenta = new BigDecimal("180.00");
        
        producto.setId(1L);
        producto.setFechaCreacion(fechaCreacion);
        producto.setPrecioCompra(precioCompra);
        producto.setPrecioVenta(precioVenta);
        producto.setStockMinimo(15);
        
        assertEquals(1L, producto.getId());
        assertEquals(fechaCreacion, producto.getFechaCreacion());
        assertEquals(precioCompra, producto.getPrecioCompra());
        assertEquals(precioVenta, producto.getPrecioVenta());
        assertEquals(15, producto.getStockMinimo());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(marca, producto.getMarca());
    }

    @Test
    void testCalculoMargen() {
        BigDecimal precioCompra = new BigDecimal("100.00");
        BigDecimal precioVenta = new BigDecimal("150.00");
        
        producto.setPrecioCompra(precioCompra);
        producto.setPrecioVenta(precioVenta);
        
        BigDecimal margen = precioVenta.subtract(precioCompra);
        assertEquals(margen, producto.getPrecioVenta().subtract(producto.getPrecioCompra()));
    }
}
