package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.Direccion;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.repository.DireccionRepository;
import com.proyectoavanzada.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Obtiene todas las direcciones de un usuario
     * @param usuarioId el ID del usuario
     * @return lista de direcciones del usuario
     */
    public List<Direccion> obtenerDireccionesPorUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId);
    }
    
    /**
     * Obtiene direcciones activas de un usuario
     * @param usuarioId el ID del usuario
     * @return lista de direcciones activas del usuario
     */
    public List<Direccion> obtenerDireccionesActivasPorUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioIdAndActivo(usuarioId, true);
    }
    
    /**
     * Obtiene una dirección por su ID
     * @param id el ID de la dirección
     * @return Optional con la dirección si existe
     */
    public Optional<Direccion> obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id);
    }
    
    /**
     * Obtiene una dirección por ID y usuario
     * @param id el ID de la dirección
     * @param usuarioId el ID del usuario
     * @return Optional con la dirección si existe
     */
    public Optional<Direccion> obtenerDireccionPorIdYUsuario(Long id, Long usuarioId) {
        return direccionRepository.findByIdAndUsuarioId(id, usuarioId);
    }
    
    /**
     * Guarda una nueva dirección
     * @param direccion la dirección a guardar
     * @return la dirección guardada
     */
    @Transactional
    public Direccion guardarDireccion(Direccion direccion) {
        // Verificar que el usuario existe y cargarlo desde la base de datos
        if (direccion.getUsuario() != null && direccion.getUsuario().getId() != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(direccion.getUsuario().getId());
            if (usuarioOpt.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado con ID: " + direccion.getUsuario().getId());
            }
            // Establecer el usuario completo desde la base de datos
            direccion.setUsuario(usuarioOpt.get());
        } else {
            throw new RuntimeException("El usuario es obligatorio para crear una dirección");
        }
        
        // Asegurar que activo esté establecido
        if (direccion.getActivo() == null) {
            direccion.setActivo(true);
        }
        
        return direccionRepository.save(direccion);
    }
    
    /**
     * Actualiza una dirección existente
     * @param direccion la dirección con los datos actualizados
     * @return la dirección actualizada
     */
    @Transactional
    public Direccion actualizarDireccion(Direccion direccion) {
        // Verificar que la dirección existe
        Optional<Direccion> direccionOpt = direccionRepository.findById(direccion.getId());
        if (direccionOpt.isEmpty()) {
            throw new RuntimeException("Dirección no encontrada con ID: " + direccion.getId());
        }
        
        Direccion direccionExistente = direccionOpt.get();
        
        // Actualizar campos
        direccionExistente.setDireccion(direccion.getDireccion());
        direccionExistente.setCiudad(direccion.getCiudad());
        direccionExistente.setDepartamento(direccion.getDepartamento());
        direccionExistente.setCodigoPostal(direccion.getCodigoPostal());
        direccionExistente.setPais(direccion.getPais());
        
        // Si se proporciona un nuevo usuario, cargarlo desde la base de datos
        if (direccion.getUsuario() != null && direccion.getUsuario().getId() != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(direccion.getUsuario().getId());
            if (usuarioOpt.isPresent()) {
                direccionExistente.setUsuario(usuarioOpt.get());
            }
        }
        
        // Asegurar que activo esté establecido
        if (direccion.getActivo() != null) {
            direccionExistente.setActivo(direccion.getActivo());
        }
        
        return direccionRepository.save(direccionExistente);
    }
    
    /**
     * Elimina una dirección por su ID
     * @param id el ID de la dirección a eliminar
     */
    @Transactional
    public void eliminarDireccion(Long id) {
        direccionRepository.deleteById(id);
    }
    
    /**
     * Desactiva una dirección (soft delete)
     * @param id el ID de la dirección a desactivar
     * @return la dirección desactivada
     */
    @Transactional
    public Direccion desactivarDireccion(Long id) {
        Optional<Direccion> direccionOpt = direccionRepository.findById(id);
        if (direccionOpt.isPresent()) {
            Direccion direccion = direccionOpt.get();
            direccion.setActivo(false);
            return direccionRepository.save(direccion);
        }
        throw new RuntimeException("Dirección no encontrada con ID: " + id);
    }
    
    /**
     * Verifica si existe una dirección para un usuario
     * @param usuarioId el ID del usuario
     * @return true si existe al menos una dirección, false en caso contrario
     */
    public boolean existeDireccionParaUsuario(Long usuarioId) {
        return direccionRepository.existsByUsuarioId(usuarioId);
    }
}

