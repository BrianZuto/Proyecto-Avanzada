package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.exception.ResourceNotFoundException;
import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("María García");
        cliente.setEmail("maria@example.com");
        cliente.setTelefono("9876543210");
        cliente.setDireccion("Calle Principal 123");
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);
        cliente.setPuntosFidelidad(0);
    }

    @Test
    void testObtenerTodosLosClientes() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // When
        List<Cliente> resultado = clienteService.obtenerTodosLosClientes();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("María García", resultado.get(0).getNombre());
        verify(clienteRepository).findAll();
    }

    @Test
    void testObtenerClientePorId() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> resultado = clienteService.obtenerClientePorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("María García", resultado.get().getNombre());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void testObtenerClientePorIdNoExiste() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Cliente> resultado = clienteService.obtenerClientePorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(clienteRepository).findById(999L);
    }

    @Test
    void testCrearCliente() {
        // Given
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Nuevo Cliente");
        nuevoCliente.setEmail("nuevo@example.com");
        nuevoCliente.setTelefono("1234567890");
        
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente resultado = clienteService.crearCliente(nuevoCliente);

        // Then
        assertNotNull(resultado);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testActualizarCliente() {
        // Given
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("María José García");
        clienteActualizado.setEmail("maria@example.com");
        clienteActualizado.setTelefono("9876543210");
        clienteActualizado.setPuntosFidelidad(50);
        
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        // When
        Cliente resultado = clienteService.actualizarCliente(clienteActualizado);

        // Then
        assertNotNull(resultado);
        verify(clienteRepository).existsById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testActualizarClienteNoExiste() {
        // Given
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(999L);
        clienteActualizado.setNombre("Cliente No Existe");
        
        when(clienteRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            clienteService.actualizarCliente(clienteActualizado);
        });
        verify(clienteRepository).existsById(999L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testEliminarCliente() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        clienteService.eliminarCliente(1L);

        // Then
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class)); // Soft delete
    }

    @Test
    void testEliminarClienteNoExiste() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            clienteService.eliminarCliente(999L);
        });
        verify(clienteRepository).findById(999L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void testBuscarPorEmail() {
        // Given
        when(clienteRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> resultado = clienteService.buscarPorEmail("maria@example.com");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("María García", resultado.get().getNombre());
        verify(clienteRepository).findByEmail("maria@example.com");
    }

    @Test
    void testBuscarPorTelefono() {
        // Given
        when(clienteRepository.findByTelefono("9876543210")).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> resultado = clienteService.buscarPorTelefono("9876543210");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("María García", resultado.get().getNombre());
        verify(clienteRepository).findByTelefono("9876543210");
    }

    @Test
    void testObtenerClientesActivos() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findByActivoTrue()).thenReturn(clientes);

        // When
        List<Cliente> resultado = clienteService.obtenerClientesActivos();

        // Then
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(clienteRepository).findByActivoTrue();
    }

    @Test
    void testBuscarPorNombre() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findByNombreContainingIgnoreCase("María")).thenReturn(clientes);

        // When
        List<Cliente> resultado = clienteService.buscarPorNombre("María");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("María García", resultado.get(0).getNombre());
        verify(clienteRepository).findByNombreContainingIgnoreCase("María");
    }

    @Test
    void testObtenerClientesConPuntos() {
        // Given
        Cliente clienteConPuntos = new Cliente();
        clienteConPuntos.setId(2L);
        clienteConPuntos.setNombre("Cliente VIP");
        clienteConPuntos.setEmail("vip@example.com");
        clienteConPuntos.setTelefono("1111111111");
        clienteConPuntos.setPuntosFidelidad(100);
        clienteConPuntos.setActivo(true);
        
        List<Cliente> clientes = Arrays.asList(clienteConPuntos);
        when(clienteRepository.findByPuntosFidelidadGreaterThan(0)).thenReturn(clientes);

        // When
        List<Cliente> resultado = clienteService.obtenerClientesConPuntos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(100, resultado.get(0).getPuntosFidelidad());
        verify(clienteRepository).findByPuntosFidelidadGreaterThan(0);
    }

    @Test
    void testAgregarPuntosFidelidad() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente resultado = clienteService.agregarPuntosFidelidad(1L, 50);

        // Then
        assertNotNull(resultado);
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testUsarPuntosFidelidad() {
        // Given
        cliente.setPuntosFidelidad(100);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente resultado = clienteService.usarPuntosFidelidad(1L, 30);

        // Then
        assertNotNull(resultado);
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testContarClientesActivos() {
        // Given
        when(clienteRepository.countByActivoTrue()).thenReturn(10L);

        // When
        long resultado = clienteService.contarClientesActivos();

        // Then
        assertEquals(10L, resultado);
        verify(clienteRepository).countByActivoTrue();
    }
}
