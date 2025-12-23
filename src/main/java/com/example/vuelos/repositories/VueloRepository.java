package com.example.vuelos.repositories;

import com.example.vuelos.models.Vuelo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class VueloRepository {

    private final Map<Integer, Vuelo> vuelos = new HashMap<>();
    private int idVuelo = 1;

    private VueloRepository() {
        save(seed("H001-V", "Iberia", "Madrid", "Buenos Aires", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 11)));
        save(seed("T100-V", "Turkish", "Istanbul", "New York", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 11)));
        save(seed("E777-V", "Emirates", "Dubai", "Madrid", LocalDate.of(2025, 3, 12), LocalDate.of(2025, 3, 12)));
        save(seed("A320-V", "Vueling", "Barcelona", "Paris", LocalDate.of(2025, 3, 9), LocalDate.of(2025, 3, 9)));
        save(seed("AF500-V", "Air France", "Paris", "Rome", LocalDate.of(2025, 3, 8), LocalDate.of(2025, 3, 8)));
        save(seed("LH220-V", "Lufthansa", "Frankfurt", "Lisbon", LocalDate.of(2025, 3, 15), LocalDate.of(2025, 3, 15)));
        save(seed("AZ900-V", "ITA Airways", "Rome", "Istanbul", LocalDate.of(2025, 3, 11), LocalDate.of(2025, 3, 11)));
        save(seed("UX010-V", "Air Europa", "Madrid", "New York", LocalDate.of(2025, 3, 14), LocalDate.of(2025, 3, 15)));
        save(seed("IB999-V", "Iberia", "Madrid", "London", LocalDate.of(2025, 3, 7), LocalDate.of(2025, 3, 7)));
        save(seed("TK333-V", "Turkish", "Istanbul", "Berlin", LocalDate.of(2025, 3, 13), LocalDate.of(2025, 3, 13)));
    }

    private Vuelo seed(String nombreVuelo, String empresa, String lugarSalida, String lugarLlegada,
                       LocalDate fechaSalida, LocalDate fechaLlegada) {
        Vuelo v = new Vuelo();
        v.setNombreVuelo(nombreVuelo);
        v.setEmpresa(empresa);
        v.setLugarSalida(lugarSalida);
        v.setLugarLlegada(lugarLlegada);
        v.setFechaSalida(fechaSalida);
        v.setFechaLlegada(fechaLlegada);
        return v;
    }

    public Vuelo save (Vuelo vuelo) {
        vuelo.setId(idVuelo++);
        vuelos.put(vuelo.getId(), vuelo);
        return vuelo;
    }

    public List<Vuelo> findAll() {
        return new ArrayList<>(vuelos.values());
    }

    public Optional<Vuelo> findById(int id) {
        return Optional.ofNullable(vuelos.get(id));
    }

    public boolean existsNombreVuelo(String nombreVuelo, Integer id) {
        if (nombreVuelo == null) {
            return false;
        }
        return vuelos.values().stream().anyMatch(v ->
                v.getNombreVuelo() != null && v.getNombreVuelo().equalsIgnoreCase(nombreVuelo)
                && (!Integer.valueOf(v.getId()).equals(id)));

    }

    public boolean delete(int id) {
        return vuelos.remove(id) != null;
    }
}