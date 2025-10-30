package com.proyectoavanzada.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.proyectoavanzada.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Configurar CORS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/api/usuarios/register").permitAll()
                .requestMatchers("/api/usuarios/check-email").permitAll()
                .requestMatchers("/api/usuarios/generate-hash").permitAll()
                
                // Swagger/OpenAPI endpoints (públicos para desarrollo)
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                
                // Endpoints que requieren autenticación
                .requestMatchers("/api/usuarios/**").authenticated()
                .requestMatchers("/api/clientes/**").authenticated()
                .requestMatchers("/api/categorias/**").authenticated()
                .requestMatchers("/api/marcas/**").authenticated()
                .requestMatchers("/api/productos/**").authenticated()
                .requestMatchers("/api/presentaciones/**").authenticated()
                .requestMatchers("/api/proveedores/**").authenticated()
                .requestMatchers("/api/compras/**").authenticated()
                .requestMatchers("/api/ventas/**").authenticated()
                .requestMatchers("/api/reportes/**").authenticated()
                
                // Permitir acceso a recursos estáticos
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // Deshabilitar autenticación básica
            .formLogin(formLogin -> formLogin.disable()) // Deshabilitar formulario de login
            .logout(logout -> logout.disable()); // Deshabilitar logout por defecto

        // Filtro JWT
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
