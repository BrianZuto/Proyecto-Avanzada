package com.proyectoavanzada.backend.security;

import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Normalizar email a minúsculas
        String emailNormalizado = username != null ? username.trim().toLowerCase() : null;
        
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(emailNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + emailNormalizado));

        // Verificar que el usuario tenga contraseña
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin contraseña configurada");
        }

        // Verificar que el usuario esté activo
        boolean activo = usuario.getActivo() != null && usuario.getActivo();

        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + (usuario.getRol() != null ? usuario.getRol() : "Usuario"))
        );

        return new User(usuario.getEmail(), usuario.getPassword(), activo, true, true, true, authorities);
    }
}


