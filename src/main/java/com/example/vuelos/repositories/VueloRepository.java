package com.example.vuelos.repositories;

import com.example.vuelos.models.Vuelo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

// En esta clase se encuentra el repositorio en memoria (Asi podemos usar el programa sin una base de datos)
// Basicamente simula una persistencia usando un Map (id -> vuelo)

@Repository
public class VueloRepository {

    // "Base de datos" en memoria
    private final Map<Integer, Vuelo> vuelos = new HashMap<>();
    // Secuencia simple para generar IDs incrementales, cada vez que se guarda un vuelo, se asigna un nuevo ID al siguiente.
    private int idVuelo = 1;

    // Constructor del repositorio
    // se cargan 10 vuelos precargados en la seed, se usa el metodo save para que se les asigne una ID automaticamente.
    public VueloRepository() {
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

    // Creamos un vuelo base sin ID
    // Solo se usa para el seed, el ID se asignará al llamar a save()
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

    // Guarda un vuelo en el Map asignandole un id incremental.
    public Vuelo save(Vuelo vuelo) {
        vuelo.setId(idVuelo++);
        vuelos.put(vuelo.getId(), vuelo);
        return vuelo;
    }

    // Devuelve todos los vuelos existentes.
    // Devolvemos lista los valores del Map
    public List<Vuelo> findAll() {
        return new ArrayList<>(vuelos.values());
    }


    // Buscar por ID.
    // Devolvemos un Optional con el vuelo si existe, o vacio si no existe.
    public Optional<Vuelo> findById(int id) {
        return Optional.ofNullable(vuelos.get(id));
    }

    // Se verifica si el nombreVuelo esta duplicado
    // Basicamente, si se esta creando un vuelo cualquier coincidencia es duplicado.
    // Pero si se esta actualizando y la ID coincide con el vuelo que se esta cambiando, ahi si permite usar el mismo nombreVuelo
    public boolean existsNombreVuelo(String nombreVuelo, Integer id) {
        if (nombreVuelo == null) {
            return false;
        }
        return vuelos.values().stream().anyMatch(v ->
                v.getNombreVuelo() != null
                        && v.getNombreVuelo().equalsIgnoreCase(nombreVuelo)
                        // Si ID es null (crear), siempre da true para cualquier vuelo encontrado
                        // si ID no es null (actualizar), evita comparar contra si mismo.
                        && (!Integer.valueOf(v.getId()).equals(id)));

    }

    // Elimina un vuelo por ID
    // Devolvemos true si existe y se ha eliminado, false si no existía.
    public boolean delete(int id) {
        return vuelos.remove(id) != null;
    }
}