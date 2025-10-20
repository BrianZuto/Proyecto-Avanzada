package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Marca;
import com.proyectoavanzada.backend.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MarcaService {
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    /**
     * Obtener todas las marcas
     */
    public List<Marca> obtenerTodasLasMarcas() {
        return marcaRepository.findAll();
    }
    
    /**
     * Obtener todas las marcas activas
     */
    public List<Marca> obtenerMarcasActivas() {
        return marcaRepository.findByActivoTrue();
    }
    
    /**
     * Obtener marcas activas ordenadas por nombre
     */
    public List<Marca> obtenerMarcasActivasOrdenadas() {
        return marcaRepository.findByActivoTrueOrderByNombreAsc();
    }
    
    /**
     * Obtener marca por ID
     */
    public Optional<Marca> obtenerMarcaPorId(Long id) {
        return marcaRepository.findById(id);
    }
    
    /**
     * Obtener marca por nombre
     */
    public Optional<Marca> obtenerMarcaPorNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }
    
    /**
     * Obtener marca por nombre (ignorando mayúsculas/minúsculas)
     */
    public Optional<Marca> obtenerMarcaPorNombreIgnoreCase(String nombre) {
        return marcaRepository.findByNombreIgnoreCase(nombre);
    }
    
    /**
     * Buscar marcas por nombre que contenga texto
     */
    public List<Marca> buscarMarcasPorNombre(String nombre) {
        return marcaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Obtener marcas por país de origen
     */
    public List<Marca> obtenerMarcasPorPais(String paisOrigen) {
        return marcaRepository.findByPaisOrigen(paisOrigen);
    }
    
    /**
     * Obtener marcas por país ordenadas por nombre
     */
    public List<Marca> obtenerMarcasPorPaisOrdenadas(String paisOrigen) {
        return marcaRepository.findByPaisOrigenOrderByNombreAsc(paisOrigen);
    }
    
    /**
     * Obtener marcas fundadas en un año específico
     */
    public List<Marca> obtenerMarcasPorAñoFundacion(Integer fechaFundacion) {
        return marcaRepository.findByFechaFundacion(fechaFundacion);
    }
    
    /**
     * Obtener marcas fundadas en un rango de años
     */
    public List<Marca> obtenerMarcasPorRangoAños(Integer añoInicio, Integer añoFin) {
        return marcaRepository.findByFechaFundacionBetween(añoInicio, añoFin);
    }
    
    /**
     * Obtener marcas con productos
     */
    public List<Marca> obtenerMarcasConProductos() {
        return marcaRepository.findMarcasConProductos();
    }
    
    /**
     * Obtener marcas sin productos
     */
    public List<Marca> obtenerMarcasSinProductos() {
        return marcaRepository.findMarcasSinProductos();
    }
    
    /**
     * Obtener conteo de productos por marca
     */
    public List<Object[]> obtenerConteoProductosPorMarca() {
        return marcaRepository.countProductosPorMarca();
    }
    
    /**
     * Guardar marca
     */
    public Marca guardarMarca(Marca marca) {
        // Verificar si el nombre ya existe
        if (marcaRepository.existsByNombreIgnoreCase(marca.getNombre())) {
            throw new RuntimeException("Ya existe una marca con este nombre");
        }
        
        return marcaRepository.save(marca);
    }
    
    /**
     * Actualizar marca
     */
    public Marca actualizarMarca(Marca marca) {
        if (!marcaRepository.existsById(marca.getId())) {
            throw new RuntimeException("Marca no encontrada");
        }
        
        // Verificar si el nombre ya existe en otra marca
        Optional<Marca> marcaExistente = marcaRepository.findByNombreIgnoreCase(marca.getNombre());
        if (marcaExistente.isPresent() && !marcaExistente.get().getId().equals(marca.getId())) {
            throw new RuntimeException("Ya existe una marca con este nombre");
        }
        
        return marcaRepository.save(marca);
    }
    
    /**
     * Eliminar marca (soft delete)
     */
    public void eliminarMarca(Long id) {
        Optional<Marca> marcaOpt = marcaRepository.findById(id);
        if (marcaOpt.isPresent()) {
            Marca marca = marcaOpt.get();
            
            // Verificar si la marca tiene productos
            if (marca.getProductos() != null && !marca.getProductos().isEmpty()) {
                throw new RuntimeException("No se puede eliminar una marca que tiene productos asociados");
            }
            
            marca.setActivo(false);
            marcaRepository.save(marca);
        } else {
            throw new RuntimeException("Marca no encontrada");
        }
    }
    
    /**
     * Eliminar marca permanentemente
     */
    public void eliminarMarcaPermanentemente(Long id) {
        Optional<Marca> marcaOpt = marcaRepository.findById(id);
        if (marcaOpt.isPresent()) {
            Marca marca = marcaOpt.get();
            
            // Verificar si la marca tiene productos
            if (marca.getProductos() != null && !marca.getProductos().isEmpty()) {
                throw new RuntimeException("No se puede eliminar una marca que tiene productos asociados");
            }
            
            marcaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Marca no encontrada");
        }
    }
    
    /**
     * Activar marca
     */
    public Marca activarMarca(Long id) {
        Optional<Marca> marcaOpt = marcaRepository.findById(id);
        if (marcaOpt.isPresent()) {
            Marca marca = marcaOpt.get();
            marca.setActivo(true);
            return marcaRepository.save(marca);
        } else {
            throw new RuntimeException("Marca no encontrada");
        }
    }
    
    /**
     * Desactivar marca
     */
    public Marca desactivarMarca(Long id) {
        Optional<Marca> marcaOpt = marcaRepository.findById(id);
        if (marcaOpt.isPresent()) {
            Marca marca = marcaOpt.get();
            marca.setActivo(false);
            return marcaRepository.save(marca);
        } else {
            throw new RuntimeException("Marca no encontrada");
        }
    }
    
    /**
     * Verificar si existe una marca con el nombre
     */
    public boolean existeMarcaConNombre(String nombre) {
        return marcaRepository.existsByNombre(nombre);
    }
    
    /**
     * Verificar si existe una marca con el nombre (ignorando mayúsculas/minúsculas)
     */
    public boolean existeMarcaConNombreIgnoreCase(String nombre) {
        return marcaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    /**
     * Contar marcas activas
     */
    public long contarMarcasActivas() {
        return marcaRepository.countByActivoTrue();
    }
    
    /**
     * Obtener marcas inactivas
     */
    public List<Marca> obtenerMarcasInactivas() {
        return marcaRepository.findByActivoFalse();
    }
}
