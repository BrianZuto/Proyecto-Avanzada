package com.proyectoavanzada.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utilidad para el manejo de contraseñas con BCrypt
 */
@Component
public class PasswordUtil {
    
    private final PasswordEncoder passwordEncoder;
    
    public PasswordUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * Encripta una contraseña usando BCrypt
     * @param rawPassword contraseña en texto plano
     * @return contraseña encriptada
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * Verifica si una contraseña en texto plano coincide con una contraseña encriptada
     * @param rawPassword contraseña en texto plano
     * @param encodedPassword contraseña encriptada
     * @return true si coinciden, false en caso contrario
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * Obtiene el encoder de contraseñas
     * @return PasswordEncoder
     */
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
