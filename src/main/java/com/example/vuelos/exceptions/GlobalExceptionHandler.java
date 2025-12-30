package com.example.vuelos.exceptions;

import com.example.vuelos.models.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

// Basicamente que genera las respuestas HTTP de la API, para que se vean "bonitas"
// Recoge las excepciones personalizadas y las devuelve con el HttpsStatus correcto y con el envoltorio del ApiResponse
// Tambien maneja otro tipo de excepciones diferentes.
// La ventaja principal es que permite al cliente ver los errores de forma clara. (En el front o desde PostMan)


// Al usar @RestControllerAdvice, Spring aplica este handler a todos los controllers.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja erorres 404 cuando el recurso no existe.
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // Maneja errores 400 cuando la petición tiene datos incorrectos o incoherentes.
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // Maneja errores 409 cuando hay un conflicto con el estado actual del sistema.
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // Maneja rutas inexistentes 404 cuando se intenta acceder a un endpoint que no existe.
    // Es obligatorio añadair en el application.properties los parametros señalados.
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = "Ruta no encontrada " + ex.getRequestURL();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, message, null));
    }

    // Maneja errores de validación de DTOs (por ejemplo el @NotBlank @NotNull) cuando se usa @Valid.
    // Devuelve un Map con:
    // - Clave: nombre del campo
    // - Valor: mensaje de error

    // Esto facilita al cliente entender qué campo provoco el error realmente.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrorException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error de validación", errors));
    }

    // Captura cualquier otro error no gestionado.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "ERROR INTERNO: " + ex.getMessage(), null));
    }
}
