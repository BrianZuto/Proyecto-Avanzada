package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Categoria;
import com.proyectoavanzada.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    /**
     * Obtener todas las categorías
     */
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }
    
    /**
     * Obtener todas las categorías activas
     */
    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepository.findByActivoTrue();
    }
    
    /**
     * Obtener categorías activas ordenadas por nombre
     */
    public List<Categoria> obtenerCategoriasActivasOrdenadas() {
        return categoriaRepository.findByActivoTrueOrderByNombreAsc();
    }
    
    /**
     * Obtener categoría por ID
     */
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    
    /**
     * Obtener categoría por nombre
     */
    public Optional<Categoria> obtenerCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
    
    /**
     * Obtener categoría por nombre (ignorando mayúsculas/minúsculas)
     */
    public Optional<Categoria> obtenerCategoriaPorNombreIgnoreCase(String nombre) {
        return categoriaRepository.findByNombreIgnoreCase(nombre);
    }
    
    /**
     * Buscar categorías por nombre que contenga texto
     */
    public List<Categoria> buscarCategoriasPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Obtener categorías con productos
     */
    public List<Categoria> obtenerCategoriasConProductos() {
        return categoriaRepository.findCategoriasConProductos();
    }
    
    /**
     * Obtener categorías sin productos
     */
    public List<Categoria> obtenerCategoriasSinProductos() {
        return categoriaRepository.findCategoriasSinProductos();
    }
    
    /**
     * Obtener conteo de productos por categoría
     */
    public List<Object[]> obtenerConteoProductosPorCategoria() {
        return categoriaRepository.countProductosPorCategoria();
    }
    
    /**
     * Guardar categoría
     */
    public Categoria guardarCategoria(Categoria categoria) {
        // Verificar si el nombre ya existe
        if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con este nombre");
        }
        
        return categoriaRepository.save(categoria);
    }
    
    /**
     * Actualizar categoría
     */
    public Categoria actualizarCategoria(Categoria categoria) {
        if (!categoriaRepository.existsById(categoria.getId())) {
            throw new RuntimeException("Categoría no encontrada");
        }
        
        // Verificar si el nombre ya existe en otra categoría
        Optional<Categoria> categoriaExistente = categoriaRepository.findByNombreIgnoreCase(categoria.getNombre());
        if (categoriaExistente.isPresent() && !categoriaExistente.get().getId().equals(categoria.getId())) {
            throw new RuntimeException("Ya existe una categoría con este nombre");
        }
        
        return categoriaRepository.save(categoria);
    }
    
    /**
     * Eliminar categoría (soft delete)
     */
    public void eliminarCategoria(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            
            // Verificar si la categoría tiene productos
            if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
                throw new RuntimeException("No se puede eliminar una categoría que tiene productos asociados");
            }
            
            categoria.setActivo(false);
            categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }
    
    /**
     * Eliminar categoría permanentemente
     */
    public void eliminarCategoriaPermanentemente(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            
            // Verificar si la categoría tiene productos
            if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
                throw new RuntimeException("No se puede eliminar una categoría que tiene productos asociados");
            }
            
            categoriaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }
    
    /**
     * Activar categoría
     */
    public Categoria activarCategoria(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setActivo(true);
            return categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }
    
    /**
     * Desactivar categoría
     */
    public Categoria desactivarCategoria(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setActivo(false);
            return categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }
    
    /**
     * Verificar si existe una categoría con el nombre
     */
    public boolean existeCategoriaConNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }
    
    /**
     * Verificar si existe una categoría con el nombre (ignorando mayúsculas/minúsculas)
     */
    public boolean existeCategoriaConNombreIgnoreCase(String nombre) {
        return categoriaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    /**
     * Contar categorías activas
     */
    public long contarCategoriasActivas() {
        return categoriaRepository.countByActivoTrue();
    }
    
    /**
     * Obtener categorías inactivas
     */
    public List<Categoria> obtenerCategoriasInactivas() {
        return categoriaRepository.findByActivoFalse();
    }
}
