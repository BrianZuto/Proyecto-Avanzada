package com.proyectoavanzada.backend.repository;

import com.proyectoavanzada.backend.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
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
    void testGuardarCliente() {
        Cliente clienteGuardado = clienteRepository.save(cliente);
        
        assertNotNull(clienteGuardado.getId());
        assertEquals("María", clienteGuardado.getNombre());
        assertEquals("maria@example.com", clienteGuardado.getEmail());
        assertTrue(clienteGuardado.getActivo());
        assertEquals(0, clienteGuardado.getPuntosFidelidad());
    }

    @Test
    void testBuscarPorEmail() {
        entityManager.persistAndFlush(cliente);
        
        Optional<Cliente> clienteEncontrado = clienteRepository.findByEmail("maria@example.com");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("María", clienteEncontrado.get().getNombre());
        assertEquals("García", clienteEncontrado.get().getApellido());
    }

    @Test
    void testBuscarPorTelefono() {
        entityManager.persistAndFlush(cliente);
        
        Optional<Cliente> clienteEncontrado = clienteRepository.findByTelefono("9876543210");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("María", clienteEncontrado.get().getNombre());
        assertEquals("García", clienteEncontrado.get().getApellido());
    }

    @Test
    void testBuscarClientesActivos() {
        Cliente clienteInactivo = new Cliente();
        clienteInactivo.setNombre("Cliente");
        clienteInactivo.setApellido("Inactivo");
        clienteInactivo.setEmail("inactivo@example.com");
        clienteInactivo.setTelefono("1111111111");
        clienteInactivo.setActivo(false);
        
        entityManager.persistAndFlush(cliente);
        entityManager.persistAndFlush(clienteInactivo);
        
        List<Cliente> clientesActivos = clienteRepository.findByActivoTrue();
        
        assertEquals(1, clientesActivos.size());
        assertEquals("María", clientesActivos.get(0).getNombre());
    }

    @Test
    void testBuscarPorNombreContaining() {
        Cliente cliente2 = new Cliente();
        cliente2.setNombre("María José");
        cliente2.setApellido("García");
        cliente2.setEmail("mariajose@example.com");
        cliente2.setTelefono("2222222222");
        cliente2.setActivo(true);
        
        entityManager.persistAndFlush(cliente);
        entityManager.persistAndFlush(cliente2);
        
        List<Cliente> clientes = clienteRepository.findByNombreContainingIgnoreCase("María");
        
        assertEquals(2, clientes.size());
    }

    @Test
    void testBuscarClientesConPuntos() {
        Cliente clienteConPuntos = new Cliente();
        clienteConPuntos.setNombre("Cliente");
        clienteConPuntos.setApellido("VIP");
        clienteConPuntos.setEmail("vip@example.com");
        clienteConPuntos.setTelefono("3333333333");
        clienteConPuntos.setPuntosFidelidad(100);
        clienteConPuntos.setActivo(true);
        
        entityManager.persistAndFlush(cliente);
        entityManager.persistAndFlush(clienteConPuntos);
        
        List<Cliente> clientesConPuntos = clienteRepository.findByPuntosFidelidadGreaterThan(0);
        
        assertEquals(1, clientesConPuntos.size());
        assertEquals("Cliente", clientesConPuntos.get(0).getNombre());
        assertEquals(100, clientesConPuntos.get(0).getPuntosFidelidad());
    }

    @Test
    void testContarClientesActivos() {
        Cliente clienteInactivo = new Cliente();
        clienteInactivo.setNombre("Cliente");
        clienteInactivo.setApellido("Inactivo");
        clienteInactivo.setEmail("inactivo@example.com");
        clienteInactivo.setTelefono("1111111111");
        clienteInactivo.setActivo(false);
        
        entityManager.persistAndFlush(cliente);
        entityManager.persistAndFlush(clienteInactivo);
        
        long count = clienteRepository.countByActivoTrue();
        
        assertEquals(1, count);
    }

    @Test
    void testEliminarCliente() {
        Cliente clienteGuardado = entityManager.persistAndFlush(cliente);
        Long id = clienteGuardado.getId();
        
        clienteRepository.deleteById(id);
        entityManager.flush();
        
        Optional<Cliente> clienteEliminado = clienteRepository.findById(id);
        assertFalse(clienteEliminado.isPresent());
    }

    @Test
    void testActualizarCliente() {
        Cliente clienteGuardado = entityManager.persistAndFlush(cliente);
        
        clienteGuardado.setNombre("María José García");
        clienteGuardado.setPuntosFidelidad(50);
        
        Cliente clienteActualizado = clienteRepository.save(clienteGuardado);
        
        assertEquals("María José García", clienteActualizado.getNombre());
        assertEquals(50, clienteActualizado.getPuntosFidelidad());
    }
}
