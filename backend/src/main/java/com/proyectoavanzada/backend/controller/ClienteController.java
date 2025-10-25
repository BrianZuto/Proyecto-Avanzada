package com.proyectoavanzada.backend.controller;

import com.proyectoavanzada.backend.model.Cliente;
import com.proyectoavanzada.backend.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Gestión de Clientes", description = "Endpoints para la gestión completa de clientes del sistema")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    /**
     * Obtener todos los clientes
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosClientes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener clientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener todos los clientes activos
     */
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerClientesActivos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener clientes activos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerClientePorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Cliente> clienteOpt = clienteService.obtenerClientePorId(id);
            if (clienteOpt.isPresent()) {
                response.put("success", true);
                response.put("data", clienteOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Cliente no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Buscar clientes por nombre o apellido
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarClientesPorNombre(@RequestParam String nombre) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.buscarClientesPorNombre(nombre);
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar clientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener clientes por ciudad
     */
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<Map<String, Object>> obtenerClientesPorCiudad(@PathVariable String ciudad) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.obtenerClientesPorCiudad(ciudad);
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener clientes por ciudad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener clientes con puntos de fidelidad
     */
    @GetMapping("/puntos/{puntosMinimos}")
    public ResponseEntity<Map<String, Object>> obtenerClientesConPuntos(@PathVariable Integer puntosMinimos) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.obtenerClientesConPuntos(puntosMinimos);
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener clientes con puntos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener top clientes por puntos de fidelidad
     */
    @GetMapping("/top-puntos")
    public ResponseEntity<Map<String, Object>> obtenerTopClientesPorPuntos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cliente> clientes = clienteService.obtenerTopClientesPorPuntos();
            response.put("success", true);
            response.put("data", clientes);
            response.put("total", clientes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener top clientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Crear nuevo cliente
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCliente(@Valid @RequestBody Cliente cliente) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente clienteGuardado = clienteService.guardarCliente(cliente);
            response.put("success", true);
            response.put("message", "Cliente creado exitosamente");
            response.put("data", clienteGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        Map<String, Object> response = new HashMap<>();
        try {
            cliente.setId(id);
            Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
            response.put("success", true);
            response.put("message", "Cliente actualizado exitosamente");
            response.put("data", clienteActualizado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar cliente (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCliente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            clienteService.eliminarCliente(id);
            response.put("success", true);
            response.put("message", "Cliente eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Activar cliente
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Map<String, Object>> activarCliente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.activarCliente(id);
            response.put("success", true);
            response.put("message", "Cliente activado exitosamente");
            response.put("data", cliente);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al activar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Desactivar cliente
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivarCliente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.desactivarCliente(id);
            response.put("success", true);
            response.put("message", "Cliente desactivado exitosamente");
            response.put("data", cliente);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al desactivar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Agregar puntos de fidelidad
     */
    @PutMapping("/{id}/agregar-puntos")
    public ResponseEntity<Map<String, Object>> agregarPuntosFidelidad(@PathVariable Long id, @RequestParam Integer puntos) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.agregarPuntosFidelidad(id, puntos);
            response.put("success", true);
            response.put("message", "Puntos agregados exitosamente");
            response.put("data", cliente);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al agregar puntos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Usar puntos de fidelidad
     */
    @PutMapping("/{id}/usar-puntos")
    public ResponseEntity<Map<String, Object>> usarPuntosFidelidad(@PathVariable Long id, @RequestParam Integer puntos) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.usarPuntosFidelidad(id, puntos);
            response.put("success", true);
            response.put("message", "Puntos usados exitosamente");
            response.put("data", cliente);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al usar puntos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Verificar si existe un cliente con el email
     */
    @GetMapping("/verificar-email")
    public ResponseEntity<Map<String, Object>> verificarEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean existe = clienteService.existeClienteConEmail(email);
            response.put("success", true);
            response.put("existe", existe);
            response.put("message", existe ? "Email ya registrado" : "Email disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al verificar email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Verificar si existe un cliente con el número de documento
     */
    @GetMapping("/verificar-documento")
    public ResponseEntity<Map<String, Object>> verificarDocumento(@RequestParam String numeroDocumento) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean existe = clienteService.existeClienteConDocumento(numeroDocumento);
            response.put("success", true);
            response.put("existe", existe);
            response.put("message", existe ? "Documento ya registrado" : "Documento disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al verificar documento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Obtener estadísticas de clientes
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalActivos = clienteService.contarClientesActivos();
            response.put("success", true);
            response.put("data", Map.of(
                "totalActivos", totalActivos
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
