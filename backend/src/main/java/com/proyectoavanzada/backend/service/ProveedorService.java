package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Proveedor;
import com.proyectoavanzada.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProveedorService {
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    /**
     * Obtener todos los proveedores
     */
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }
    
    /**
     * Obtener todos los proveedores activos
     */
    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedorRepository.findByActivoTrue();
    }
    
    /**
     * Obtener proveedores activos ordenados por nombre
     */
    public List<Proveedor> obtenerProveedoresActivosOrdenados() {
        return proveedorRepository.findByActivoTrueOrderByNombreAsc();
    }
    
    /**
     * Obtener proveedor por ID
     */
    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }
    
    /**
     * Obtener proveedor por RUC
     */
    public Optional<Proveedor> obtenerProveedorPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }
    
    /**
     * Obtener proveedor por email
     */
    public Optional<Proveedor> obtenerProveedorPorEmail(String email) {
        return proveedorRepository.findByEmail(email);
    }
    
    /**
     * Buscar proveedores por nombre
     */
    public List<Proveedor> buscarProveedoresPorNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Obtener proveedores por ciudad
     */
    public List<Proveedor> obtenerProveedoresPorCiudad(String ciudad) {
        return proveedorRepository.findByCiudad(ciudad);
    }
    
    /**
     * Obtener proveedores por ciudad ordenados por nombre
     */
    public List<Proveedor> obtenerProveedoresPorCiudadOrdenados(String ciudad) {
        return proveedorRepository.findByCiudadOrderByNombreAsc(ciudad);
    }
    
    /**
     * Obtener proveedores registrados en un rango de fechas
     */
    public List<Proveedor> obtenerProveedoresPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return proveedorRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener proveedores con compras
     */
    public List<Proveedor> obtenerProveedoresConCompras() {
        return proveedorRepository.findProveedoresConCompras();
    }
    
    /**
     * Obtener proveedores sin compras
     */
    public List<Proveedor> obtenerProveedoresSinCompras() {
        return proveedorRepository.findProveedoresSinCompras();
    }
    
    /**
     * Obtener proveedores con crédito
     */
    public List<Proveedor> obtenerProveedoresConCredito() {
        return proveedorRepository.findProveedoresConCredito();
    }
    
    /**
     * Obtener proveedores por días de crédito
     */
    public List<Proveedor> obtenerProveedoresPorDiasCredito(Integer creditoDias) {
        return proveedorRepository.findByCreditoDias(creditoDias);
    }
    
    /**
     * Obtener proveedores con límite de crédito mayor a un valor
     */
    public List<Proveedor> obtenerProveedoresConLimiteCredito(Double limite) {
        return proveedorRepository.findByLimiteCreditoGreaterThanEqual(limite);
    }
    
    /**
     * Obtener conteo de compras por proveedor
     */
    public List<Object[]> obtenerConteoComprasPorProveedor() {
        return proveedorRepository.countComprasPorProveedor();
    }
    
    /**
     * Guardar proveedor
     */
    public Proveedor guardarProveedor(Proveedor proveedor) {
        // Verificar si el RUC ya existe
        if (proveedorRepository.existsByRuc(proveedor.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con este RUC");
        }
        
        // Verificar si el email ya existe
        if (proveedor.getEmail() != null && proveedorRepository.existsByEmail(proveedor.getEmail())) {
            throw new RuntimeException("Ya existe un proveedor con este email");
        }
        
        return proveedorRepository.save(proveedor);
    }
    
    /**
     * Actualizar proveedor
     */
    public Proveedor actualizarProveedor(Proveedor proveedor) {
        if (!proveedorRepository.existsById(proveedor.getId())) {
            throw new RuntimeException("Proveedor no encontrado");
        }
        
        // Verificar si el RUC ya existe en otro proveedor
        Optional<Proveedor> proveedorExistente = proveedorRepository.findByRuc(proveedor.getRuc());
        if (proveedorExistente.isPresent() && !proveedorExistente.get().getId().equals(proveedor.getId())) {
            throw new RuntimeException("Ya existe un proveedor con este RUC");
        }
        
        // Verificar si el email ya existe en otro proveedor
        if (proveedor.getEmail() != null) {
            Optional<Proveedor> proveedorExistenteEmail = proveedorRepository.findByEmail(proveedor.getEmail());
            if (proveedorExistenteEmail.isPresent() && !proveedorExistenteEmail.get().getId().equals(proveedor.getId())) {
                throw new RuntimeException("Ya existe un proveedor con este email");
            }
        }
        
        return proveedorRepository.save(proveedor);
    }
    
    /**
     * Eliminar proveedor (soft delete)
     */
    public void eliminarProveedor(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            
            // Verificar si el proveedor tiene compras
            if (proveedor.getCompras() != null && !proveedor.getCompras().isEmpty()) {
                throw new RuntimeException("No se puede eliminar un proveedor que tiene compras asociadas");
            }
            
            proveedor.setActivo(false);
            proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Eliminar proveedor permanentemente
     */
    public void eliminarProveedorPermanentemente(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            
            // Verificar si el proveedor tiene compras
            if (proveedor.getCompras() != null && !proveedor.getCompras().isEmpty()) {
                throw new RuntimeException("No se puede eliminar un proveedor que tiene compras asociadas");
            }
            
            proveedorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Activar proveedor
     */
    public Proveedor activarProveedor(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            proveedor.setActivo(true);
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Desactivar proveedor
     */
    public Proveedor desactivarProveedor(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            proveedor.setActivo(false);
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Actualizar límite de crédito
     */
    public Proveedor actualizarLimiteCredito(Long id, Double nuevoLimite) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            proveedor.setLimiteCredito(nuevoLimite);
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Actualizar días de crédito
     */
    public Proveedor actualizarDiasCredito(Long id, Integer nuevosDias) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            proveedor.setCreditoDias(nuevosDias);
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Obtener total de compras por proveedor
     */
    public BigDecimal obtenerTotalComprasPorProveedor(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            if (proveedor.getCompras() != null && !proveedor.getCompras().isEmpty()) {
                return proveedor.getCompras().stream()
                    .map(compra -> compra.getTotal())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            return BigDecimal.ZERO;
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }
    
    /**
     * Verificar si existe un proveedor con el RUC
     */
    public boolean existeProveedorConRuc(String ruc) {
        return proveedorRepository.existsByRuc(ruc);
    }
    
    /**
     * Verificar si existe un proveedor con el email
     */
    public boolean existeProveedorConEmail(String email) {
        return proveedorRepository.existsByEmail(email);
    }
    
    /**
     * Contar proveedores activos
     */
    public long contarProveedoresActivos() {
        return proveedorRepository.countByActivoTrue();
    }
    
    /**
     * Obtener proveedores inactivos
     */
    public List<Proveedor> obtenerProveedoresInactivos() {
        return proveedorRepository.findByActivoFalse();
    }
}
