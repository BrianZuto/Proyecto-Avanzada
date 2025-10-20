package com.proyectoavanzada.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoavanzada.backend.exception.ResourceNotFoundException;
import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
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
    void testObtenerTodosLosClientes() throws Exception {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.obtenerTodosLosClientes()).thenReturn(clientes);

        // When & Then
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nombre").value("María"))
                .andExpect(jsonPath("$.data[0].email").value("maria@example.com"));

        verify(clienteService).obtenerTodosLosClientes();
    }

    @Test
    void testObtenerClientePorId() throws Exception {
        // Given
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(cliente));

        // When & Then
        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombre").value("María"))
                .andExpect(jsonPath("$.data.email").value("maria@example.com"));

        verify(clienteService).obtenerClientePorId(1L);
    }

    @Test
    void testObtenerClientePorIdNoExiste() throws Exception {
        // Given
        when(clienteService.obtenerClientePorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"));

        verify(clienteService).obtenerClientePorId(999L);
    }

    @Test
    void testCrearCliente() throws Exception {
        // Given
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Nuevo");
        nuevoCliente.setApellido("Cliente");
        nuevoCliente.setEmail("nuevo@example.com");
        nuevoCliente.setTelefono("1234567890");
        nuevoCliente.setDireccion("Nueva Dirección");

        when(clienteService.guardarCliente(any(Cliente.class))).thenReturn(cliente);

        // When & Then
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente creado exitosamente"))
                .andExpect(jsonPath("$.data.nombre").value("María"));

        verify(clienteService).guardarCliente(any(Cliente.class));
    }

    @Test
    void testActualizarCliente() throws Exception {
        // Given
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("María José");
        clienteActualizado.setApellido("García");
        clienteActualizado.setEmail("maria@example.com");
        clienteActualizado.setTelefono("9876543210");
        clienteActualizado.setPuntosFidelidad(50);

        when(clienteService.actualizarCliente(any(Cliente.class))).thenReturn(clienteActualizado);

        // When & Then
        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente actualizado exitosamente"))
                .andExpect(jsonPath("$.data.nombre").value("María José"));

        verify(clienteService).actualizarCliente(any(Cliente.class));
    }

    @Test
    void testEliminarCliente() throws Exception {
        // Given
        // No need to mock obtenerClientePorId since controller calls eliminarCliente directly

        // When & Then
        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente eliminado exitosamente"));

        verify(clienteService).eliminarCliente(1L);
    }

    @Test
    void testBuscarPorEmail() throws Exception {
        // Given
        when(clienteService.existeClienteConEmail("maria@example.com")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/clientes/verificar-email?email=maria@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.existe").value(true))
                .andExpect(jsonPath("$.message").value("Email ya registrado"));

        verify(clienteService).existeClienteConEmail("maria@example.com");
    }

    // @Test - No endpoint exists for phone search
    void testBuscarPorTelefono() throws Exception {
        // Given
        when(clienteService.buscarPorTelefono("9876543210")).thenReturn(Optional.of(cliente));

        // When & Then
        mockMvc.perform(get("/api/clientes/telefono/9876543210"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cliente.nombre").value("María García"));

        verify(clienteService).buscarPorTelefono("9876543210");
    }

    @Test
    void testObtenerClientesActivos() throws Exception {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.obtenerClientesActivos()).thenReturn(clientes);

        // When & Then
        mockMvc.perform(get("/api/clientes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].activo").value(true));

        verify(clienteService).obtenerClientesActivos();
    }

    @Test
    void testBuscarPorNombre() throws Exception {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.buscarClientesPorNombre("María")).thenReturn(clientes);

        // When & Then
        mockMvc.perform(get("/api/clientes/buscar?nombre=María"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nombre").value("María"));

        verify(clienteService).buscarClientesPorNombre("María");
    }

    @Test
    void testObtenerClientesConPuntos() throws Exception {
        // Given
        Cliente clienteConPuntos = new Cliente();
        clienteConPuntos.setId(2L);
        clienteConPuntos.setNombre("Cliente VIP");
        clienteConPuntos.setEmail("vip@example.com");
        clienteConPuntos.setTelefono("1111111111");
        clienteConPuntos.setPuntosFidelidad(100);
        clienteConPuntos.setActivo(true);

        List<Cliente> clientes = Arrays.asList(clienteConPuntos);
        when(clienteService.obtenerClientesConPuntos(50)).thenReturn(clientes);

        // When & Then
        mockMvc.perform(get("/api/clientes/puntos/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].puntosFidelidad").value(100));

        verify(clienteService).obtenerClientesConPuntos(50);
    }

    @Test
    void testAgregarPuntosFidelidad() throws Exception {
        // Given
        when(clienteService.agregarPuntosFidelidad(1L, 50)).thenReturn(cliente);

        // When & Then
        mockMvc.perform(put("/api/clientes/1/agregar-puntos")
                .param("puntos", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Puntos agregados exitosamente"));

        verify(clienteService).agregarPuntosFidelidad(1L, 50);
    }

    @Test
    void testUsarPuntosFidelidad() throws Exception {
        // Given
        when(clienteService.usarPuntosFidelidad(1L, 30)).thenReturn(cliente);

        // When & Then
        mockMvc.perform(put("/api/clientes/1/usar-puntos")
                .param("puntos", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Puntos usados exitosamente"));

        verify(clienteService).usarPuntosFidelidad(1L, 30);
    }

    @Test
    void testContarClientesActivos() throws Exception {
        // Given
        when(clienteService.contarClientesActivos()).thenReturn(10L);

        // When & Then
        mockMvc.perform(get("/api/clientes/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalActivos").value(10));

        verify(clienteService).contarClientesActivos();
    }
}
