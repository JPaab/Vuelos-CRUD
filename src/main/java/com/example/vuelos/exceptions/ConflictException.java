package com.example.vuelos.exceptions;

// Excepcion custom para los errores de tipo 409 (Conflict)
// Se usa cuando el estado del sistema entra en conflicto por la operación solicitada,
// por ejemplo intentar crear o actualizar un vuelo con nombreVuelo igual al de otro vuelo.
// La excepcion es capturada por el Handler Global y este devuelve una respuesta dentro del envoltorio (ApiResponse) con código 409.

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
