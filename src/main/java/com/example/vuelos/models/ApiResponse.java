package com.example.vuelos.models;

import lombok.Getter;
import lombok.Setter;

// Esto basicamente es el envoltorio para todas las respuestas de la API
// Permite manejar un formato de respuesta consistente en todos los endpoints
// Tanto en casos de exito como de error.

@Getter
@Setter
public class ApiResponse<T> {
    // Indica si la operación fue exitosa
    private boolean success;
    // mensaje descriptivo para el cliente (Desde PostMan o en el front)
    private String message;
    // Es la información de la respuesta que pedimos a la API
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
