package com.example.vuelos.services;

import com.example.vuelos.exceptions.BadRequestException;
import com.example.vuelos.exceptions.ConflictException;
import com.example.vuelos.exceptions.NotFoundException;
import com.example.vuelos.models.Vuelo;
import com.example.vuelos.repositories.VueloRepository;
import com.example.vuelos.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

// Capa de negocio para Vuelos
// Basicamente aplica reglas de negocios (Validaciones, duplicados, coherencia en fechas)
// Implemente filtros combinables para listar (empresa, lugarLlegada, fechaSalida)
// Por default los vuelos se listan en el el orden de "fechaSalida", y como Bonus, tambien se pueden listar segun "empresa" y "lugarLlegada"
// Aqui se decide que excepcion lanzar para que el Handler genere la respuesta HTTP.

@Service
public class VueloService {

    // Dependencia de la persistencia en memoria
    private final VueloRepository repository;

    public VueloService(VueloRepository repository) {
        this.repository = repository;
    }

    // Crear un vuelo nuevo
    // Basicamente valida datos básicos y coherencia de fechas.
    // Comprueba si hay duplicados
    // Si esta correcto, lo guarda en el repositorio (y le asigna un ID)
    public Vuelo crear(Vuelo vuelo) {
        validar(vuelo);

        if (repository.existsNombreVuelo(vuelo.getNombreVuelo(), null)) {
            throw new ConflictException("El vuelo ya existe (nombreVuelo repetido)");
        }
        return repository.save(vuelo);
    }

    // Devuelve un Stream de vuelos aplicando filtros combinables.
    // Se usa Stream para poder usar varios filtros de manera limpia y eficiente.
    // Los filtros no son obligatorios, si el parámetro no esta, no se filtra por ese campo.
    public Stream<Vuelo> listarFiltrados(String empresa, String lugarLlegada, LocalDate fechaSalida) {
        // Se buscan todos lso vuelos en el repo
        Stream<Vuelo> s = repository.findAll().stream();

        // Para el filtro empresa (normalizamos para comparar sin importar MAYUS o MINUS
        if (empresa != null && !empresa.isBlank()) {
            String e = empresa.trim().toLowerCase(Locale.ROOT);
            s = s.filter(v -> v.getEmpresa() != null && v.getEmpresa().toLowerCase(Locale.ROOT).equals(e));
        }

        // Para el filtro lugarLlegada
        if (lugarLlegada != null && !lugarLlegada.isBlank()) {
            String ll = lugarLlegada.trim().toLowerCase(Locale.ROOT);
            s = s.filter(v -> v.getLugarLlegada() != null && v.getLugarLlegada().toLowerCase(Locale.ROOT).equals(ll));

        }

        // Filtro por fecha de salida exacta.
        if (fechaSalida != null) {
            s = s.filter(v -> fechaSalida.equals(v.getFechaSalida()));
        }

        return s;
    }

    // Lista los vuelos filtrados y ordenados
    // Si no se indica ordenarPor, el listado viene por default ordenado por fechaSalida.
    public List<Vuelo> listar(String empresa,
                              String lugarLlegada,
                              LocalDate fechaSalida,
                              String ordenarPor) {

        // Comparador según el criterio default
        Comparator<Vuelo> comp = buildComparator(ordenarPor);

        // Aplicamos filtros y finalmente ordena con el comparador construido.
        return listarFiltrados(empresa, lugarLlegada, fechaSalida)
                .sorted(comp)
                .toList();

    }

    // Actualizar un vuelo existente
    public Vuelo actualizar(int id, Vuelo vuelo) {
        // Verifica si el vuelo existe, si no lanza un 404.
        Vuelo vueloEncontrado = obtenerPorId(id);
        // Valida los datos nuevos
        validar(vuelo);

        // Pasamos el ID actual para excluirlo del chequeo de duplicados
        if (repository.existsNombreVuelo(vuelo.getNombreVuelo(), id)) {
            throw new ConflictException("nombreVuelo ya utilizado");
        }

        // Copiamos los datos al objeto existente.
        vueloEncontrado.setNombreVuelo(vuelo.getNombreVuelo());
        vueloEncontrado.setEmpresa(vuelo.getEmpresa());
        vueloEncontrado.setLugarSalida(vuelo.getLugarSalida());
        vueloEncontrado.setLugarLlegada(vuelo.getLugarLlegada());
        vueloEncontrado.setFechaSalida(vuelo.getFechaSalida());
        vueloEncontrado.setFechaLlegada(vuelo.getFechaLlegada());

        return vueloEncontrado;
    }

    // Eliminar un vuelo por ID
    public void eliminar(int id) {
        boolean eliminado = repository.delete(id);
        if (!eliminado) {
            throw new NotFoundException("Vuelo no encontrado o eliminado recientemente");
        }
    }

    // Validaciones básica del modelo.
    // Valida que el objeto no sea null, que las strings obligatorias no sean null/vacíos
    // que fechas no sean null
    // y que fechaSalida no sea posterior a fechaLlegada
    public Vuelo obtenerPorId(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Vuelo no encontrado"));
    }

    private void validar(Vuelo vuelo) {
        if (vuelo == null) {
            throw new BadRequestException("Datos incorrectos");
        }
        if (vuelo.getNombreVuelo() == null || vuelo.getNombreVuelo().isBlank()
                || vuelo.getEmpresa() == null || vuelo.getEmpresa().isBlank()
                || vuelo.getLugarSalida() == null || vuelo.getLugarSalida().isBlank()
                || vuelo.getLugarLlegada() == null || vuelo.getLugarLlegada().isBlank()
                || vuelo.getFechaSalida() == null
                || vuelo.getFechaLlegada() == null) {
            throw new BadRequestException("Datos incorrectos");
        }

        // la validacion para que fechaSalida no sea posterior a fechaLlegada
        DateUtils.validarRango(vuelo.getFechaSalida(), vuelo.getFechaLlegada());
    }

    // El comparador para ordenar el listado
    // Tambien permite ordenar por empresa y lugarLlegada
    // Si se esta usando algo diferente a esos 3, lanza una excepción
    private Comparator<Vuelo> buildComparator(String ordenarPor) {
        // Aqui hacemos caso a la consigna. Al listar los vuelos, estaran ordenados por fechaSalida.
        if (ordenarPor == null || ordenarPor.isBlank()) {
            return Comparator.comparing(Vuelo::getFechaSalida)
                    .thenComparing(Vuelo::getId);
        }

        // Switch con criterios para el filtro permitidos.
        return switch (ordenarPor.trim()) {
            case "fechaSalida" -> Comparator.comparing(Vuelo::getFechaSalida);
            case "empresa" -> Comparator.comparing(Vuelo::getEmpresa, String.CASE_INSENSITIVE_ORDER);
            case "lugarLlegada" -> Comparator.comparing(Vuelo::getLugarLlegada, String.CASE_INSENSITIVE_ORDER);
            default -> throw new BadRequestException("ordenarPor inválido. Usa empresa, lugarLlegada o fechaSalida");
        };
    }
}
