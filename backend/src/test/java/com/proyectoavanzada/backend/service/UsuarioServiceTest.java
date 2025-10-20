package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.exception.ResourceNotFoundException;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.repository.UsuarioRepository;
import com.proyectoavanzada.backend.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// PasswordEncoder import removido

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
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
    void testObtenerTodosLosUsuarios() {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testObtenerUsuarioPorId() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void testObtenerUsuarioPorIdNoExiste() {
        // Given
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(usuarioRepository).findById(999L);
    }

    @Test
    void testCrearUsuario() {
        // Given
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setEmail("nuevo@example.com");
        nuevoUsuario.setPassword("password123");
        
        when(passwordUtil.encodePassword("password123")).thenReturn("encoded_password");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.crearUsuario(nuevoUsuario);

        // Then
        assertNotNull(resultado);
        verify(passwordUtil).encodePassword("password123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuario() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Carlos Pérez");
        usuarioActualizado.setEmail("juan@example.com");
        usuarioActualizado.setTelefono("9876543210");
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // When
        Usuario resultado = usuarioService.actualizarUsuario(usuarioActualizado);

        // Then
        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuarioNoExiste() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(999L);
        usuarioActualizado.setNombre("Usuario No Existe");
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // When
        Usuario resultado = usuarioService.actualizarUsuario(usuarioActualizado);

        // Then - No debería lanzar excepción porque el servicio no valida existencia
        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario() {
        // When
        usuarioService.eliminarUsuario(1L);

        // Then
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testEliminarUsuarioNoExiste() {
        // When
        usuarioService.eliminarUsuario(999L);

        // Then - No debería lanzar excepción porque el servicio no valida existencia
        verify(usuarioRepository).deleteById(999L);
    }

    @Test
    void testBuscarPorEmail() {
        // Given
        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> resultado = usuarioService.buscarPorEmail("juan@example.com");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        verify(usuarioRepository).findByEmail("juan@example.com");
    }

    @Test
    void testBuscarPorEmailNoExiste() {
        // Given
        when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = usuarioService.buscarPorEmail("noexiste@example.com");

        // Then
        assertFalse(resultado.isPresent());
        verify(usuarioRepository).findByEmail("noexiste@example.com");
    }

    @Test
    void testObtenerUsuariosPorRol() {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByRol("Usuario")).thenReturn(usuarios);

        // When
        List<Usuario> resultado = usuarioService.obtenerUsuariosPorRol("Usuario");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Usuario", resultado.get(0).getRol());
        verify(usuarioRepository).findByRol("Usuario");
    }

    @Test
    void testObtenerUsuariosActivos() {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByActivo(true)).thenReturn(usuarios);

        // When
        List<Usuario> resultado = usuarioService.obtenerUsuariosActivos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(usuarioRepository).findByActivo(true);
    }

    @Test
    void testContarUsuariosPorRol() {
        // Given
        when(usuarioRepository.countByRol("Usuario")).thenReturn(5L);

        // When
        long resultado = usuarioService.contarUsuariosPorRol("Usuario");

        // Then
        assertEquals(5L, resultado);
        verify(usuarioRepository).countByRol("Usuario");
    }

    @Test
    void testBuscarPorNombre() {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByNombreContainingIgnoreCase("Juan")).thenReturn(usuarios);

        // When
        List<Usuario> resultado = usuarioService.buscarPorNombre("Juan");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        verify(usuarioRepository).findByNombreContainingIgnoreCase("Juan");
    }
}
