package com.proyectoavanzada.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(com.proyectoavanzada.backend.config.TestSecurityConfig.class)
class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
        
        cliente = new Cliente();
        cliente.setNombre("María García");
        cliente.setEmail("maria@example.com");
        cliente.setTelefono("9876543210");
        cliente.setDireccion("Calle Principal 123");
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);
        cliente.setPuntosFidelidad(0);
    }

    @Test
    void testFlujoCompletoCliente() throws Exception {
        // 1. Crear cliente
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Cliente Test");
        nuevoCliente.setApellido("Apellido Test");
        nuevoCliente.setEmail("cliente@example.com");
        nuevoCliente.setTelefono("1234567890");
        nuevoCliente.setDireccion("Dirección Test");

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente creado exitosamente"));

        // 2. Verificar que se guardó en la base de datos
        assertTrue(clienteRepository.findByEmail("cliente@example.com").isPresent());
        Cliente clienteGuardado = clienteRepository.findByEmail("cliente@example.com").get();

        // 3. Obtener cliente por ID
        mockMvc.perform(get("/api/clientes/" + clienteGuardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Cliente Test"));

        // 4. Actualizar cliente
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(clienteGuardado.getId());
        clienteActualizado.setNombre("Cliente Test Actualizado");
        clienteActualizado.setApellido("Apellido Actualizado");
        clienteActualizado.setEmail("cliente@example.com");
        clienteActualizado.setTelefono("9876543210");
        clienteActualizado.setDireccion("Nueva Dirección");
        clienteActualizado.setPuntosFidelidad(50);

        mockMvc.perform(put("/api/clientes/" + clienteGuardado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente actualizado exitosamente"));

        // 5. Verificar que se actualizó en la base de datos
        Cliente clienteVerificado = clienteRepository.findById(clienteGuardado.getId()).get();
        assertEquals("Cliente Test Actualizado", clienteVerificado.getNombre());
        assertEquals(50, clienteVerificado.getPuntosFidelidad());

        // 6. Agregar puntos de fidelidad
        mockMvc.perform(put("/api/clientes/" + clienteGuardado.getId() + "/agregar-puntos")
                .param("puntos", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Puntos agregados exitosamente"));

        // 7. Verificar puntos actualizados
        Cliente clienteConPuntos = clienteRepository.findById(clienteGuardado.getId()).get();
        assertEquals(75, clienteConPuntos.getPuntosFidelidad());

        // 8. Usar puntos de fidelidad
        mockMvc.perform(put("/api/clientes/" + clienteGuardado.getId() + "/usar-puntos")
                .param("puntos", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Puntos usados exitosamente"));

        // 9. Verificar puntos después del uso
        Cliente clienteFinal = clienteRepository.findById(clienteGuardado.getId()).get();
        assertEquals(55, clienteFinal.getPuntosFidelidad());

        // 10. Eliminar cliente
        mockMvc.perform(delete("/api/clientes/" + clienteGuardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente eliminado exitosamente"));

        // 11. Verificar que el cliente está inactivo (soft delete)
        assertTrue(clienteRepository.findById(clienteFinal.getId()).isPresent());
        assertFalse(clienteRepository.findById(clienteFinal.getId()).get().getActivo());
    }

    @Test
    void testBuscarClientes() throws Exception {
        // Crear varios clientes
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("María");
        cliente1.setApellido("García");
        cliente1.setEmail("maria@example.com");
        cliente1.setTelefono("1111111111");
        cliente1.setActivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNombre("María José");
        cliente2.setApellido("López");
        cliente2.setEmail("mariajose@example.com");
        cliente2.setTelefono("2222222222");
        cliente2.setActivo(true);

        Cliente cliente3 = new Cliente();
        cliente3.setNombre("Juan");
        cliente3.setApellido("Pérez");
        cliente3.setEmail("juan@example.com");
        cliente3.setTelefono("3333333333");
        cliente3.setActivo(false);

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
        clienteRepository.save(cliente3);

        // Buscar por nombre
        mockMvc.perform(get("/api/clientes/buscar?nombre=María"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        // Buscar por email
        mockMvc.perform(get("/api/clientes/verificar-email?email=maria@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.existe").value(true));

        // Buscar por teléfono - endpoint no disponible, saltamos esta prueba

        // Obtener clientes activos
        mockMvc.perform(get("/api/clientes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        // Contar clientes activos
        mockMvc.perform(get("/api/clientes/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalActivos").value(2));
    }

    @Test
    void testPuntosFidelidad() throws Exception {
        // Crear cliente con puntos
        Cliente clienteConPuntos = new Cliente();
        clienteConPuntos.setNombre("Cliente");
        clienteConPuntos.setApellido("VIP");
        clienteConPuntos.setEmail("vip@example.com");
        clienteConPuntos.setTelefono("4444444444");
        clienteConPuntos.setPuntosFidelidad(100);
        clienteConPuntos.setActivo(true);

        clienteRepository.save(clienteConPuntos);

        // Obtener clientes con puntos
        mockMvc.perform(get("/api/clientes/puntos/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].puntosFidelidad").value(100));

        // Agregar más puntos
        mockMvc.perform(put("/api/clientes/" + clienteConPuntos.getId() + "/agregar-puntos")
                .param("puntos", "50"))
                .andExpect(status().isOk());

        // Verificar puntos actualizados
        Cliente clienteActualizado = clienteRepository.findById(clienteConPuntos.getId()).get();
        assertEquals(150, clienteActualizado.getPuntosFidelidad());

        // Usar puntos
        mockMvc.perform(put("/api/clientes/" + clienteConPuntos.getId() + "/usar-puntos")
                .param("puntos", "75"))
                .andExpect(status().isOk());

        // Verificar puntos después del uso
        Cliente clienteFinal = clienteRepository.findById(clienteConPuntos.getId()).get();
        assertEquals(75, clienteFinal.getPuntosFidelidad());
    }

    @Test
    void testValidacionesCliente() throws Exception {
        // Test con datos inválidos
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setNombre(""); // Nombre vacío
        clienteInvalido.setApellido(""); // Apellido vacío
        clienteInvalido.setEmail("email-invalido"); // Email inválido
        clienteInvalido.setTelefono(""); // Teléfono vacío

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());
    }
}
