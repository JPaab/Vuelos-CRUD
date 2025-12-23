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

@RestController
@RequestMapping("/vuelos")
public class VueloController {

    private final VueloService service;

    public VueloController(VueloService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VueloResponseDTO>>> listar(
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String lugarLlegada,
            @RequestParam(required = false) String fechaSalida,
            @RequestParam(required = false) String ordenarPor,
            @RequestParam(required = false) String ordenar
    ) {
        LocalDate fecha = DateUtils.parseLocalDateOrThrow(fechaSalida, "fechaSalida");

        List<VueloResponseDTO> data = service
                .listar(empresa, lugarLlegada, fecha, ordenarPor, ordenar)
                .stream()
                .map(VueloMapper::toDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Listado de vuelos", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VueloResponseDTO>> obtenerPorId(@PathVariable int id) {
        Vuelo v = service.obtenerPorId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Vuelo encontrado", VueloMapper.toDTO(v)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VueloResponseDTO>> crear(@Valid @RequestBody VueloRequestDTO dto) {
        Vuelo creado = service.crear(VueloMapper.toModel(dto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Vuelo creado", VueloMapper.toDTO(creado)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VueloResponseDTO>> actualizar(@PathVariable int id, @Valid @RequestBody VueloRequestDTO dto) {
        Vuelo actualizado = service.actualizar(id, VueloMapper.toModel(dto));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Vuelo actualizado correctamente", VueloMapper.toDTO(actualizado)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<VueloResponseDTO>> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Vuelo eliminado correctamente", null));
    }
}
