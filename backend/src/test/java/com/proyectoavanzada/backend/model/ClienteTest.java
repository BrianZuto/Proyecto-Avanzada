package com.proyectoavanzada.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// Imports de Spring Boot removidos para pruebas unitarias

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas unitarias simples - no necesita Spring Boot
class ClienteTest {

    private Validator validator;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        cliente = new Cliente();
        cliente.setNombre("María");
        cliente.setApellido("García");
        cliente.setEmail("maria@example.com");
        cliente.setTelefono("9876543210");
        cliente.setDireccion("Calle Principal 123");
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);
        cliente.setPuntosFidelidad(0);
    }

    @Test
    void testClienteValido() {
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    void testNombreObligatorio() {
        cliente.setNombre(null);
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void testEmailObligatorio() {
        cliente.setEmail(null);
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void testEmailInvalido() {
        cliente.setEmail("email-invalido");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("formato válido")));
    }

    @Test
    void testTelefonoObligatorio() {
        cliente.setTelefono(null);
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void testValoresPorDefecto() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Test");
        nuevoCliente.setEmail("test@example.com");
        nuevoCliente.setTelefono("1234567890");
        
        assertTrue(nuevoCliente.getActivo(), "Activo debería ser true por defecto");
        assertEquals(0, nuevoCliente.getPuntosFidelidad(), "Puntos de fidelidad deberían ser 0 por defecto");
    }

    @Test
    void testGettersYSetters() {
        LocalDateTime fechaRegistro = LocalDateTime.now();
        
        cliente.setId(1L);
        cliente.setFechaRegistro(fechaRegistro);
        cliente.setPuntosFidelidad(100);
        
        assertEquals(1L, cliente.getId());
        assertEquals(fechaRegistro, cliente.getFechaRegistro());
        assertEquals(100, cliente.getPuntosFidelidad());
        assertEquals("Calle Principal 123", cliente.getDireccion());
    }

    @Test
    void testAgregarPuntos() {
        cliente.setPuntosFidelidad(50);
        assertEquals(50, cliente.getPuntosFidelidad());
    }

    @Test
    void testUsarPuntos() {
        cliente.setPuntosFidelidad(100);
        // Simular uso de puntos
        cliente.setPuntosFidelidad(cliente.getPuntosFidelidad() - 30);
        assertEquals(70, cliente.getPuntosFidelidad());
    }
}
