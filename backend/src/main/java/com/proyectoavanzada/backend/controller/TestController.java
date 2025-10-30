package com.proyectoavanzada.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Endpoints de Prueba", description = "Endpoints para verificar el funcionamiento del sistema")
public class TestController {
    
    @Operation(
        summary = "Prueba del sistema",
        description = "Endpoint para verificar que el backend está funcionando correctamente"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Sistema funcionando correctamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = "{\"message\": \"Backend funcionando correctamente\", \"status\": \"OK\", \"timestamp\": \"2024-01-01T12:00:00\"}"
            )
        )
    )
    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backend funcionando correctamente");
        response.put("status", "OK");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
    
    @Operation(
        summary = "Estado de salud del sistema",
        description = "Endpoint para verificar el estado de salud del sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Sistema en buen estado",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = "{\"status\": \"UP\", \"service\": \"Proyecto Avanzada Backend\"}"
            )
        )
    )
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Proyecto Avanzada Backend");
        return response;
    }
}
