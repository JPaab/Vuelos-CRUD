package com.example.vuelos.exceptions;

// Excepcion custom para los errores de tipo 409 (Not Found)
// Se usa cuando el recurso solicitado no existe en el sistema, por ejemplo buscar, actualizar o eliminar un vuelo por ID inexistente.
// La excepcion es capturada por el Handler Global y este devuelve una respuesta dentro del envoltorio (ApiResponse) con código 404.

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
