package com.example.vuelos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// Este es el DTO de salida, es lo que devuelve la API
// Define el formato de datos que la API devuelve en GET/POST/PUT

@Getter
@Setter
@AllArgsConstructor
public class VueloResponseDTO {
    private int id;
    private String nombreVuelo;
    private String empresa;
    private String lugarSalida;
    private String lugarLlegada;
    private LocalDate fechaSalida;
    private LocalDate fechaLlegada;

    // Duración del vuelo en días
    // No se almacena en el repo, si no que se calcula en cada respuesta
    // Si la fechaSalida y fechaLlegada es la misma, el valor será 0
    private Long duracionDias;
}
