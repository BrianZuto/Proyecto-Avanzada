package com.proyectoavanzada.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoavanzada.backend.controller.AuthController;
import com.proyectoavanzada.backend.model.Usuario;
import com.proyectoavanzada.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(com.proyectoavanzada.backend.config.TestSecurityConfig.class)
class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        
        usuario = new Usuario();
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
    void testCrearYBuscarUsuario() throws Exception {
        // Given
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setEmail("nuevo@example.com");
        nuevoUsuario.setPassword("password123");
        nuevoUsuario.setFechaNacimiento(LocalDate.of(1995, 3, 20));
        nuevoUsuario.setTelefono("9876543210");

        // When & Then - Crear usuario
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"));

        // Verificar que se guardó en la base de datos
        assertTrue(usuarioRepository.findByEmail("nuevo@example.com").isPresent());
    }

    @Test
    void testFlujoCompletoUsuario() throws Exception {
        // 1. Crear usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Usuario Test");
        nuevoUsuario.setEmail("test@example.com");
        nuevoUsuario.setPassword("password123");
        nuevoUsuario.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        nuevoUsuario.setTelefono("1234567890");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated());

        // 2. Verificar que el usuario existe
        assertTrue(usuarioRepository.findByEmail("test@example.com").isPresent());
        Usuario usuarioGuardado = usuarioRepository.findByEmail("test@example.com").get();

        // 3. Obtener perfil del usuario
        mockMvc.perform(get("/api/auth/profile/" + usuarioGuardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.nombre").value("Usuario Test"));

        // 4. Actualizar perfil
        AuthController.UpdateProfileRequest updateRequest = new AuthController.UpdateProfileRequest();
        updateRequest.setId(usuarioGuardado.getId());
        updateRequest.setNombre("Usuario Test Actualizado");
        updateRequest.setTelefono("9876543210");

        mockMvc.perform(put("/api/auth/update-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Perfil actualizado exitosamente"));

        // 5. Verificar que se actualizó en la base de datos
        Usuario usuarioActualizado = usuarioRepository.findById(usuarioGuardado.getId()).get();
        assertEquals("Usuario Test Actualizado", usuarioActualizado.getNombre());
        assertEquals("9876543210", usuarioActualizado.getTelefono());
    }

    @Test
    void testValidacionesUsuario() throws Exception {
        // Test con datos inválidos
        AuthController.RegisterRequest requestInvalido = new AuthController.RegisterRequest();
        requestInvalido.setNombre(""); // Nombre vacío
        requestInvalido.setEmail("email-invalido"); // Email inválido
        requestInvalido.setPassword("123"); // Password muy corto

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testEmailDuplicado() throws Exception {
        // Crear primer usuario
        Usuario primerUsuario = new Usuario();
        primerUsuario.setNombre("Primer Usuario");
        primerUsuario.setEmail("duplicado@example.com");
        primerUsuario.setPassword("password123");
        primerUsuario.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        primerUsuario.setTelefono("1111111111");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(primerUsuario)))
                .andExpect(status().isCreated());

        // Intentar crear segundo usuario con el mismo email
        Usuario segundoUsuario = new Usuario();
        segundoUsuario.setNombre("Segundo Usuario");
        segundoUsuario.setEmail("duplicado@example.com");
        segundoUsuario.setPassword("password123");
        segundoUsuario.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        segundoUsuario.setTelefono("2222222222");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(segundoUsuario)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));
    }

    @Test
    void testCheckEmail() throws Exception {
        // Guardar usuario en la base de datos
        usuarioRepository.save(usuario);

        // Verificar que el email existe
        AuthController.EmailCheckRequest emailRequest = new AuthController.EmailCheckRequest();
        emailRequest.setEmail("juan@example.com");

        mockMvc.perform(post("/api/auth/check-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));

        // Verificar que el email no existe
        emailRequest.setEmail("noexiste@example.com");

        mockMvc.perform(post("/api/auth/check-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }
}
