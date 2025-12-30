package com.example.vuelos.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Es donde se crea la entidad Vuelo, con sus campos propios (id, nombreVuelo, empresa etc...)
// Usamos Lombok para generar Getters, setters y toString.
// Estos campos se usan en la capa de persistencia Repository y en el service.
// No se expone directamente en la API, si no que se manejan a partir de los DTOs

// Use lombok en el proyecto porque facilita mucho la vida.
// Data genera los getters, setters y toString automaticamente.
// NoArgsConstructor genera el constructor vacío.
@Data
@NoArgsConstructor
public class Vuelo {
    private int id;
    private String nombreVuelo;
    private String empresa;
    private String lugarSalida;
    private String lugarLlegada;
    private LocalDate fechaSalida;
    private LocalDate fechaLlegada;

    private Vuelo(int id, String nombreVuelo, String empresa, String lugarSalida, String lugarLlegada, LocalDate fechaSalida, LocalDate fechaLlegada) {
        this.id = id;
        this.nombreVuelo = nombreVuelo;
        this.empresa = empresa;
        this.lugarSalida = lugarSalida;
        this.lugarLlegada = lugarLlegada;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
    }
}
