package com.example.vuelos.utils;

import com.example.vuelos.exceptions.BadRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Este utils basicamente maneja el tema de las fechas, si la fecha se da en un formato invalido se lanza una excepcion.
// Convierte la fechaSalida a LocalDate
// Valida las fechas para que no sea incoherente, orden etc.

public class DateUtils {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    public static LocalDate parseLocalDateOrThrow(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        } try {
            return LocalDate.parse(value.trim(), ISO);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato inválido para " + fieldName + ". Usa yyyy-MM-dd.");
        }
    }

    public static void validarRango (LocalDate salida, LocalDate llegada) {
        if (salida != null && llegada != null && salida.isAfter(llegada)) {
            throw new BadRequestException("fechaSalida no puede ser posterior a fechaLlegada");
        }
    }
}
