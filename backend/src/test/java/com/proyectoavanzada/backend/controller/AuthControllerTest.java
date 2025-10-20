package com.proyectoavanzada.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("$2a$10$encoded_password");
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setRol("Usuario");
        usuario.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        usuario.setTelefono("1234567890");
    }

    @Test
    void testLoginExitoso() throws Exception {
        // Given
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail("juan@example.com");
        loginRequest.setPassword("password123");

        when(usuarioService.obtenerUsuarioPorEmail("juan@example.com")).thenReturn(Optional.of(usuario));
        when(usuarioService.verificarContraseña("password123", "$2a$10$encoded_password")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login exitoso"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.user.email").value("juan@example.com"));

        verify(usuarioService).obtenerUsuarioPorEmail("juan@example.com");
        verify(usuarioService).verificarContraseña("password123", "$2a$10$encoded_password");
    }

    @Test
    void testLoginCredencialesIncorrectas() throws Exception {
        // Given
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail("juan@example.com");
        loginRequest.setPassword("password_incorrecta");

        when(usuarioService.obtenerUsuarioPorEmail("juan@example.com")).thenReturn(Optional.of(usuario));
        when(usuarioService.verificarContraseña("password_incorrecta", "$2a$10$encoded_password")).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Contraseña incorrecta"));

        verify(usuarioService).obtenerUsuarioPorEmail("juan@example.com");
        verify(usuarioService).verificarContraseña("password_incorrecta", "$2a$10$encoded_password");
    }

    @Test
    void testLoginUsuarioNoExiste() throws Exception {
        // Given
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail("noexiste@example.com");
        loginRequest.setPassword("password123");

        when(usuarioService.obtenerUsuarioPorEmail("noexiste@example.com")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));

        verify(usuarioService).obtenerUsuarioPorEmail("noexiste@example.com");
        verify(usuarioService, never()).verificarPassword(anyString(), anyString());
    }

    @Test
    void testRegistroExitoso() throws Exception {
        // Given
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest();
        registerRequest.setNombre("Nuevo Usuario");
        registerRequest.setEmail("nuevo@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFechaNacimiento(LocalDate.of(1995, 3, 20));
        registerRequest.setTelefono("9876543210");

        when(usuarioService.existeUsuarioConEmail("nuevo@example.com")).thenReturn(false);
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"))
                .andExpect(jsonPath("$.user.nombre").value("Juan Pérez"));

        verify(usuarioService).existeUsuarioConEmail("nuevo@example.com");
        verify(usuarioService).guardarUsuario(any(Usuario.class));
    }

    @Test
    void testRegistroEmailYaExiste() throws Exception {
        // Given
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest();
        registerRequest.setNombre("Nuevo Usuario");
        registerRequest.setEmail("juan@example.com");
        registerRequest.setPassword("password123");

        when(usuarioService.existeUsuarioConEmail("juan@example.com")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));

        verify(usuarioService).existeUsuarioConEmail("juan@example.com");
        verify(usuarioService, never()).crearUsuario(any(Usuario.class));
    }

    @Test
    void testCheckEmailExiste() throws Exception {
        // Given
        AuthController.EmailCheckRequest emailRequest = new AuthController.EmailCheckRequest();
        emailRequest.setEmail("juan@example.com");

        when(usuarioService.existeUsuarioConEmail("juan@example.com")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/check-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.message").value("Email ya registrado"));

        verify(usuarioService).existeUsuarioConEmail("juan@example.com");
    }

    @Test
    void testCheckEmailNoExiste() throws Exception {
        // Given
        AuthController.EmailCheckRequest emailRequest = new AuthController.EmailCheckRequest();
        emailRequest.setEmail("noexiste@example.com");

        when(usuarioService.existeUsuarioConEmail("noexiste@example.com")).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/auth/check-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false))
                .andExpect(jsonPath("$.message").value("Email disponible"));

        verify(usuarioService).existeUsuarioConEmail("noexiste@example.com");
    }

    @Test
    void testGeneratePasswordHash() throws Exception {
        // Given
        AuthController.PasswordHashRequest passwordRequest = new AuthController.PasswordHashRequest();
        passwordRequest.setPassword("password123");

        when(usuarioService.verificarContraseña("password123", "dummy")).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/auth/generate-hash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.hashedPassword").exists());

        verify(usuarioService).verificarContraseña("password123", "dummy");
    }

    @Test
    void testGetProfile() throws Exception {
        // Given
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(get("/api/auth/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.user.email").value("juan@example.com"));

        verify(usuarioService).obtenerUsuarioPorId(1L);
    }

    @Test
    void testUpdateProfile() throws Exception {
        // Given
        AuthController.UpdateProfileRequest updateRequest = new AuthController.UpdateProfileRequest();
        updateRequest.setId(1L);
        updateRequest.setNombre("Juan Carlos Pérez");
        updateRequest.setTelefono("9876543210");
        updateRequest.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Carlos Pérez");
        usuarioActualizado.setEmail("juan@example.com");
        usuarioActualizado.setTelefono("9876543210");
        usuarioActualizado.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.actualizarUsuario(any(Usuario.class))).thenReturn(usuarioActualizado);

        // When & Then
        mockMvc.perform(put("/api/auth/update-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Perfil actualizado exitosamente"))
                .andExpect(jsonPath("$.user.nombre").value("Juan Carlos Pérez"));

        verify(usuarioService).obtenerUsuarioPorId(1L);
        verify(usuarioService).actualizarUsuario(any(Usuario.class));
    }
}
