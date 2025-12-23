package com.example.vuelos.dtos;

import com.example.vuelos.models.Vuelo;

import java.time.temporal.ChronoUnit;

public class VueloMapper {

    public static Vuelo toModel(VueloRequestDTO dto) {
        Vuelo v = new Vuelo();
        v.setNombreVuelo(dto.getNombreVuelo());
        v.setEmpresa(dto.getEmpresa());
        v.setLugarSalida(dto.getLugarSalida());
        v.setLugarLlegada(dto.getLugarLlegada());
        v.setFechaSalida(dto.getFechaSalida());
        v.setFechaLlegada(dto.getFechaLlegada());
        return v;
    }

    public static VueloResponseDTO toDTO(Vuelo vuelo) {
        long duracion = ChronoUnit.DAYS.between(vuelo.getFechaSalida(), vuelo.getFechaLlegada());
        return new VueloResponseDTO(
                vuelo.getId(),
                vuelo.getNombreVuelo(),
                vuelo.getEmpresa(),
                vuelo.getLugarSalida(),
                vuelo.getLugarLlegada(),
                vuelo.getFechaSalida(),
                vuelo.getFechaLlegada(),
                duracion
        );
    }
}
