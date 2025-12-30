package com.example.vuelos.controllers;

import com.example.vuelos.dtos.VueloMapper;
import com.example.vuelos.dtos.VueloRequestDTO;
import com.example.vuelos.dtos.VueloResponseDTO;
import com.example.vuelos.models.ApiResponse;
import com.example.vuelos.models.Vuelo;
import com.example.vuelos.services.VueloService;
import com.example.vuelos.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


// Es el controlador para gestionar los vuelos.
// Expone los end-points HTTP en la ruta base "/vuelos".
// Valida las entradas (query params y body) y delega la logica al service.


@RestController
@RequestMapping("/vuelos")
public class VueloController {

    // Capa de negocio.
    // Se inyecta por constructor (inyección recomendada en Spring).

    private final VueloService service;

    public VueloController(VueloService service) {
        this.service = service;
    }

    // GET /vuelos
    // Lista los vuelos con filtros combinables y ordenamiento (son opcionales pero puedes usar los filtros)
    @GetMapping
    public ResponseEntity<ApiResponse<List<VueloResponseDTO>>> listar(
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String lugarLlegada,
            @RequestParam(required = false) String fechaSalida,
            @RequestParam(required = false) String ordenarPor
    ) {

        // Convierte el query param "fechaSalida" (Si es valido) a LocalDate usando la utilidad en DateUtils.
        // Si el formato es inválido, la utilidad lanza una excepcion personalizada - BadRequestException
        LocalDate fecha = DateUtils.parseLocalDateOrThrow(fechaSalida, "fechaSalida");

        List<VueloResponseDTO> data = service
                .listar(empresa, lugarLlegada, fecha, ordenarPor)
                .stream()
                .map(VueloMapper::toDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Listado de vuelos", data));
    }

    // GET /vuelos/{id}
    // Devuelve un vuelo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VueloResponseDTO>> obtenerPorId(@PathVariable int id) {
        // Si el ID no existe, el service lanza una excepcion personalizada - NotFoundException
        Vuelo v = service.obtenerPorId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Vuelo encontrado por ID", VueloMapper.toDTO(v)));
    }

    // POST /vuelos
    // Crear un vuelo nuevo
    // @Valid obliga a que las validaciones del VueloRequestDTO se cumplan (Campos obligatorios)
    // Ademas el service aplica reglas como fechas coherentes y nombreVuelo duplicados.
    @PostMapping
    public ResponseEntity<ApiResponse<VueloResponseDTO>> crear(@Valid @RequestBody VueloRequestDTO dto) {
        // Se convierte el DTO al modelo interno
        Vuelo creado = service.crear(VueloMapper.toModel(dto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Vuelo creado correctamente", VueloMapper.toDTO(creado)));
    }

    // PUT /vuelos/{id}
    // Actualizar un vuelo existente
    // @Valid valida el body como hizo en el "crear"
    // Si el ID no existe, lanza la excepcion personalizada - NotFoundException
    // Si hay conflictos porque el nombre concuerda con el de otro vuelo, lanza la excepcion personalizada - ConflictException
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VueloResponseDTO>> actualizar(@PathVariable int id, @Valid @RequestBody VueloRequestDTO dto) {
        Vuelo actualizado = service.actualizar(id, VueloMapper.toModel(dto));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Vuelo actualizado correctamente", VueloMapper.toDTO(actualizado)));
    }

    // DELETE /vuelos/{id}
    // Eliminar un vuelo por ID
    // Si el ID no existe, el service lanza el NotFoundException
    // Si se elimina correctamente, el ApiResponse (Basicamente el envoltorio de la información), devolvemos success=true y data=null
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<VueloResponseDTO>> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Vuelo eliminado correctamente", null));
    }
}
