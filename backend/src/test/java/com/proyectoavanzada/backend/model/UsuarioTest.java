package com.proyectoavanzada.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas unitarias simples - no necesita Spring Boot
class UsuarioTest {

    private Validator validator;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("password123");
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setRol("Usuario");
        usuario.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        usuario.setTelefono("1234567890");
    }

    @Test
    void testUsuarioValido() {
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    void testNombreObligatorio() {
        usuario.setNombre(null);
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void testNombreMuyCorto() {
        usuario.setNombre("A");
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("2 y 50 caracteres")));
    }

    @Test
    void testEmailObligatorio() {
        usuario.setEmail(null);
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));
    }

    @Test
    void testEmailInvalido() {
        usuario.setEmail("email-invalido");
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("formato válido")));
    }

    @Test
    void testPasswordObligatorio() {
        usuario.setPassword(null);
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatoria")));
    }

    @Test
    void testPasswordMuyCorto() {
        usuario.setPassword("123");
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("6 caracteres")));
    }

    @Test
    void testValoresPorDefecto() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Test");
        nuevoUsuario.setEmail("test@example.com");
        nuevoUsuario.setPassword("password123");
        
        assertTrue(nuevoUsuario.getActivo(), "Activo debería ser true por defecto");
        assertEquals("Usuario", nuevoUsuario.getRol(), "Rol debería ser 'Usuario' por defecto");
    }

    @Test
    void testGettersYSetters() {
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        
        usuario.setId(1L);
        usuario.setFechaCreacion(fechaCreacion);
        usuario.setFechaNacimiento(fechaNacimiento);
        
        assertEquals(1L, usuario.getId());
        assertEquals(fechaCreacion, usuario.getFechaCreacion());
        assertEquals(fechaNacimiento, usuario.getFechaNacimiento());
        assertEquals("1234567890", usuario.getTelefono());
    }
}
