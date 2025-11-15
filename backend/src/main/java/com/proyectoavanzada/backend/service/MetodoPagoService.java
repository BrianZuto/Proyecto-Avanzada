package com.proyectoavanzada.backend.service;

import com.proyectoavanzada.backend.model.MetodoPago;
import com.proyectoavanzada.backend.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoService {
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    /**
     * Obtiene todos los métodos de pago
     * @return lista de todos los métodos de pago
     */
    public List<MetodoPago> obtenerTodosLosMetodosPago() {
        return metodoPagoRepository.findAll();
    }
    
    /**
     * Obtiene un método de pago por su ID
     * @param id el ID del método de pago
     * @return Optional con el método de pago si existe
     */
    public Optional<MetodoPago> obtenerMetodoPagoPorId(Long id) {
        return metodoPagoRepository.findById(id);
    }
    
    /**
     * Obtiene todos los métodos de pago activos de un usuario
     * @param usuarioId el ID del usuario
     * @return lista de métodos de pago activos del usuario
     */
    public List<MetodoPago> obtenerMetodosPagoPorUsuario(Long usuarioId) {
        return metodoPagoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }
    
    /**
     * Guarda un nuevo método de pago
     * Si se marca como principal, desmarca los demás métodos de pago del usuario
     * @param metodoPago el método de pago a guardar
     * @return el método de pago guardado
     */
    @Transactional
    public MetodoPago guardarMetodoPago(MetodoPago metodoPago) {
        // Si se marca como principal, desmarcar los demás
        if (metodoPago.getPrincipal() != null && metodoPago.getPrincipal()) {
            List<MetodoPago> metodosPagoUsuario = metodoPagoRepository.findByUsuarioId(metodoPago.getUsuario().getId());
            for (MetodoPago mp : metodosPagoUsuario) {
                if (!mp.getId().equals(metodoPago.getId())) {
                    mp.setPrincipal(false);
                    metodoPagoRepository.save(mp);
                }
            }
        }
        
        // Enmascarar el número de tarjeta (solo guardar últimos 4 dígitos)
        if (metodoPago.getNumeroTarjeta() != null && metodoPago.getNumeroTarjeta().length() > 4) {
            String numeroCompleto = metodoPago.getNumeroTarjeta().replaceAll("\\s", "");
            String ultimos4 = numeroCompleto.substring(numeroCompleto.length() - 4);
            metodoPago.setNumeroTarjeta(ultimos4);
        }
        
        return metodoPagoRepository.save(metodoPago);
    }
    
    /**
     * Actualiza un método de pago existente
     * Si se marca como principal, desmarca los demás métodos de pago del usuario
     * @param metodoPago el método de pago con los datos actualizados
     * @return el método de pago actualizado
     */
    @Transactional
    public MetodoPago actualizarMetodoPago(MetodoPago metodoPago) {
        // Si se marca como principal, desmarcar los demás
        if (metodoPago.getPrincipal() != null && metodoPago.getPrincipal()) {
            List<MetodoPago> metodosPagoUsuario = metodoPagoRepository.findByUsuarioId(metodoPago.getUsuario().getId());
            for (MetodoPago mp : metodosPagoUsuario) {
                if (!mp.getId().equals(metodoPago.getId())) {
                    mp.setPrincipal(false);
                    metodoPagoRepository.save(mp);
                }
            }
        }
        
        // Enmascarar el número de tarjeta si se proporciona uno nuevo
        if (metodoPago.getNumeroTarjeta() != null && metodoPago.getNumeroTarjeta().length() > 4) {
            String numeroCompleto = metodoPago.getNumeroTarjeta().replaceAll("\\s", "");
            String ultimos4 = numeroCompleto.substring(numeroCompleto.length() - 4);
            metodoPago.setNumeroTarjeta(ultimos4);
        }
        
        return metodoPagoRepository.save(metodoPago);
    }
    
    /**
     * Elimina un método de pago por su ID
     * @param id el ID del método de pago a eliminar
     */
    public void eliminarMetodoPago(Long id) {
        metodoPagoRepository.deleteById(id);
    }
    
    /**
     * Establece un método de pago como principal
     * @param id el ID del método de pago
     * @param usuarioId el ID del usuario
     * @return el método de pago actualizado
     */
    @Transactional
    public MetodoPago establecerComoPrincipal(Long id, Long usuarioId) {
        // Desmarcar todos los métodos de pago del usuario
        List<MetodoPago> metodosPagoUsuario = metodoPagoRepository.findByUsuarioId(usuarioId);
        for (MetodoPago mp : metodosPagoUsuario) {
            mp.setPrincipal(false);
            metodoPagoRepository.save(mp);
        }
        
        // Marcar el método de pago seleccionado como principal
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        metodoPago.setPrincipal(true);
        return metodoPagoRepository.save(metodoPago);
    }
}

