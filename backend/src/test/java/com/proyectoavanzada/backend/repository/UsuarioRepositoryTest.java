package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
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
    void testGuardarUsuario() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        assertNotNull(usuarioGuardado.getId());
        assertEquals("Juan Pérez", usuarioGuardado.getNombre());
        assertEquals("juan@example.com", usuarioGuardado.getEmail());
        assertTrue(usuarioGuardado.getActivo());
    }

    @Test
    void testBuscarPorEmail() {
        entityManager.persistAndFlush(usuario);
        
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("juan@example.com");
        
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Juan Pérez", usuarioEncontrado.get().getNombre());
    }

    @Test
    void testBuscarPorEmailNoExiste() {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("noexiste@example.com");
        
        assertFalse(usuarioEncontrado.isPresent());
    }

    @Test
    void testBuscarPorRol() {
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin123");
        admin.setRol("Admin");
        admin.setActivo(true);
        
        entityManager.persistAndFlush(usuario);
        entityManager.persistAndFlush(admin);
        
        List<Usuario> usuarios = usuarioRepository.findByRol("Usuario");
        
        assertEquals(1, usuarios.size());
        assertEquals("Juan Pérez", usuarios.get(0).getNombre());
    }

    @Test
    void testBuscarUsuariosActivos() {
        Usuario usuarioInactivo = new Usuario();
        usuarioInactivo.setNombre("Usuario Inactivo");
        usuarioInactivo.setEmail("inactivo@example.com");
        usuarioInactivo.setPassword("password123");
        usuarioInactivo.setActivo(false);
        
        entityManager.persistAndFlush(usuario);
        entityManager.persistAndFlush(usuarioInactivo);
        
        List<Usuario> usuariosActivos = usuarioRepository.findByActivoTrue();
        
        assertEquals(1, usuariosActivos.size());
        assertEquals("Juan Pérez", usuariosActivos.get(0).getNombre());
    }

    @Test
    void testContarUsuariosPorRol() {
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin123");
        admin.setRol("Admin");
        admin.setActivo(true);
        
        entityManager.persistAndFlush(usuario);
        entityManager.persistAndFlush(admin);
        
        long countUsuarios = usuarioRepository.countByRol("Usuario");
        long countAdmins = usuarioRepository.countByRol("Admin");
        
        assertEquals(1, countUsuarios);
        assertEquals(1, countAdmins);
    }

    @Test
    void testBuscarPorNombreContaining() {
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Juan Carlos");
        usuario2.setEmail("juancarlos@example.com");
        usuario2.setPassword("password123");
        usuario2.setActivo(true);
        
        entityManager.persistAndFlush(usuario);
        entityManager.persistAndFlush(usuario2);
        
        List<Usuario> usuarios = usuarioRepository.findByNombreContainingIgnoreCase("Juan");
        
        assertEquals(2, usuarios.size());
    }

    @Test
    void testEliminarUsuario() {
        Usuario usuarioGuardado = entityManager.persistAndFlush(usuario);
        Long id = usuarioGuardado.getId();
        
        usuarioRepository.deleteById(id);
        entityManager.flush();
        
        Optional<Usuario> usuarioEliminado = usuarioRepository.findById(id);
        assertFalse(usuarioEliminado.isPresent());
    }

    @Test
    void testActualizarUsuario() {
        Usuario usuarioGuardado = entityManager.persistAndFlush(usuario);
        
        usuarioGuardado.setNombre("Juan Carlos Pérez");
        usuarioGuardado.setTelefono("9876543210");
        
        Usuario usuarioActualizado = usuarioRepository.save(usuarioGuardado);
        
        assertEquals("Juan Carlos Pérez", usuarioActualizado.getNombre());
        assertEquals("9876543210", usuarioActualizado.getTelefono());
    }
}
