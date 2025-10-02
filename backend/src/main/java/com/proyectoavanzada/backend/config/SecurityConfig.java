package com.proyectoavanzada.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para desarrollo
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/usuarios/**").permitAll() // Permitir acceso a todos los endpoints de usuarios
                .requestMatchers("/api/**").permitAll() // Permitir acceso a todos los endpoints de la API
                .requestMatchers("/usuarios/**").permitAll() // Permitir acceso directo a usuarios
                .anyRequest().permitAll() // Permitir todo para desarrollo
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // Deshabilitar autenticación básica
            .formLogin(formLogin -> formLogin.disable()); // Deshabilitar formulario de login

        return http.build();
    }
}
