package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Obtener todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    
    /**
     * Obtener todos los clientes activos
     */
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }
    
    /**
     * Obtener cliente por ID
     */
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Obtener cliente por email
     */
    public Optional<Cliente> obtenerClientePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    /**
     * Obtener cliente por número de documento
     */
    public Optional<Cliente> obtenerClientePorDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento);
    }
    
    /**
     * Buscar clientes por nombre o apellido
     */
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(nombre, nombre);
    }
    
    /**
     * Obtener clientes por ciudad
     */
    public List<Cliente> obtenerClientesPorCiudad(String ciudad) {
        return clienteRepository.findByCiudad(ciudad);
    }
    
    /**
     * Obtener clientes con puntos de fidelidad
     */
    public List<Cliente> obtenerClientesConPuntos(Integer puntosMinimos) {
        return clienteRepository.findByPuntosFidelidadGreaterThan(puntosMinimos);
    }
    
    /**
     * Obtener clientes registrados en un rango de fechas
     */
    public List<Cliente> obtenerClientesPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return clienteRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener clientes por tipo de documento
     */
    public List<Cliente> obtenerClientesPorTipoDocumento(String tipoDocumento) {
        return clienteRepository.findByTipoDocumento(tipoDocumento);
    }
    
    /**
     * Obtener top clientes por puntos de fidelidad
     */
    public List<Cliente> obtenerTopClientesPorPuntos() {
        return clienteRepository.findTopClientesByPuntosFidelidad();
    }
    
    /**
     * Obtener clientes sin compras en un período
     */
    public List<Cliente> obtenerClientesSinComprasEnPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return clienteRepository.findClientesSinComprasEnPeriodo(fechaInicio, fechaFin);
    }
    
    /**
     * Guardar cliente
     */
    public Cliente guardarCliente(Cliente cliente) {
        // Verificar si el email ya existe
        if (cliente.getEmail() != null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con este email");
        }
        
        // Verificar si el número de documento ya existe
        if (cliente.getNumeroDocumento() != null && clienteRepository.existsByNumeroDocumento(cliente.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un cliente con este número de documento");
        }
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Actualizar cliente
     */
    public Cliente actualizarCliente(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new RuntimeException("Cliente no encontrado");
        }
        
        // Verificar si el email ya existe en otro cliente
        if (cliente.getEmail() != null) {
            Optional<Cliente> clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
            if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Ya existe un cliente con este email");
            }
        }
        
        // Verificar si el número de documento ya existe en otro cliente
        if (cliente.getNumeroDocumento() != null) {
            Optional<Cliente> clienteExistente = clienteRepository.findByNumeroDocumento(cliente.getNumeroDocumento());
            if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Ya existe un cliente con este número de documento");
            }
        }
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Eliminar cliente (soft delete)
     */
    public void eliminarCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setActivo(false);
            clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }
    
    /**
     * Eliminar cliente permanentemente
     */
    public void eliminarClientePermanentemente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }
    
    /**
     * Activar cliente
     */
    public Cliente activarCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setActivo(true);
            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }
    
    /**
     * Desactivar cliente
     */
    public Cliente desactivarCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setActivo(false);
            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }
    
    /**
     * Agregar puntos de fidelidad
     */
    public Cliente agregarPuntosFidelidad(Long id, Integer puntos) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setPuntosFidelidad(cliente.getPuntosFidelidad() + puntos);
            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }
    
    /**
     * Usar puntos de fidelidad
     */
    public Cliente usarPuntosFidelidad(Long id, Integer puntos) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (cliente.getPuntosFidelidad() >= puntos) {
                cliente.setPuntosFidelidad(cliente.getPuntosFidelidad() - puntos);
                return clienteRepository.save(cliente);
            } else {
                throw new RuntimeException("Puntos insuficientes");
            }
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }
    
    /**
     * Verificar si existe un cliente con el email
     */
    public boolean existeClienteConEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
    
    /**
     * Verificar si existe un cliente con el número de documento
     */
    public boolean existeClienteConDocumento(String numeroDocumento) {
        return clienteRepository.existsByNumeroDocumento(numeroDocumento);
    }
    
    /**
     * Contar clientes activos
     */
    public long contarClientesActivos() {
        return clienteRepository.countByActivoTrue();
    }
    
    /**
     * Contar clientes por ciudad
     */
    public long contarClientesPorCiudad(String ciudad) {
        return clienteRepository.countByCiudad(ciudad);
    }
    
    /**
     * Buscar cliente por email (alias para obtenerClientePorEmail)
     */
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    /**
     * Buscar cliente por teléfono
     */
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return clienteRepository.findByTelefono(telefono);
    }
    
    /**
     * Buscar clientes por nombre (alias para buscarClientesPorNombre)
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Crear cliente (alias para guardarCliente)
     */
    public Cliente crearCliente(Cliente cliente) {
        return guardarCliente(cliente);
    }
    
    /**
     * Actualizar cliente por ID
     */
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        cliente.setId(id);
        return actualizarCliente(cliente);
    }
    
    /**
     * Obtener clientes con puntos (sin parámetro)
     */
    public List<Cliente> obtenerClientesConPuntos() {
        return clienteRepository.findByPuntosFidelidadGreaterThan(0);
    }
    
    /**
     * Obtener cliente por ID (versión que lanza excepción si no existe)
     */
    public Cliente obtenerClientePorIdObligatorio(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }
}
