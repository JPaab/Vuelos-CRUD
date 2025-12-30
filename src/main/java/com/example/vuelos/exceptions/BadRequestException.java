package com.example.vuelos.exceptions;

// Excepcion custom para los errores de tipo 400 (Bad Request)
// Se usa cuando el cliente envía una peticion valida pero con datos incorrectos
// La excepcion es capturada por el Handler Global y este devuelve una respuesta dentro del envoltorio (ApiResponse) con código 400

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
