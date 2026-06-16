package com.facultad.sistemaavisos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        Map<String, Object> respuesta = Map.of(
                "fechaHora", LocalDateTime.now(),
                "estado", HttpStatus.NOT_FOUND.value(),
                "error", "Recurso no encontrado",
                "mensaje", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntimeException(RuntimeException ex) {
        Map<String, Object> respuesta = Map.of(
                "fechaHora", LocalDateTime.now(),
                "estado", HttpStatus.BAD_REQUEST.value(),
                "error", "Error en la solicitud",
                "mensaje", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}