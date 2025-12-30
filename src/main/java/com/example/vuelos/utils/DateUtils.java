package com.example.vuelos.utils;

import com.example.vuelos.exceptions.BadRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


// Utilidad de fecha para la APP
// Parsea fechas recibidas como String (las enviadas desde el query params) a LocalDate
// Valida reglas relacionadas con las fechas (rangos coherentes, etc).
// Esto tambien permite matener la logica de las fechas en una misma clase y asi evitar codigo redundante y duplicado.

public class DateUtils {

    // Formato de fecha estandar, es el que uso en este proyecto.
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    private DateUtils() {
        // Evita instanciar esta clase: solo contiene métodos estáticos.
    }

    // Convierte un String a LocalDate usando el formato ISO
    public static LocalDate parseLocalDateOrThrow(String value, String fieldName) {
        // Si el valor viene null o vacío devuelve un null.
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim(), ISO);
        } catch (DateTimeParseException e) {
            // Si el valor viene con formato inválido, lanza una BadRequestException.
            throw new BadRequestException("Formato inválido para " + fieldName + ". Usa yyyy-MM-dd.");
        }
    }

    // Valida la coherencia del rango de fechas
    public static void validarRango(LocalDate salida, LocalDate llegada) {
        // fechaSalida no puede ser posterior a fechaLlegada
        // si una de las fechas es null no se valida aqui.
        // La validación de null se hace en el service
        if (salida != null && llegada != null && salida.isAfter(llegada)) {
            throw new BadRequestException("fechaSalida no puede ser posterior a fechaLlegada");
        }
    }
}
