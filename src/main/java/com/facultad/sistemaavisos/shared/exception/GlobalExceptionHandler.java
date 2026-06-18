package com.facultad.sistemaavisos.shared.exception;

import com.facultad.sistemaavisos.shared.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Recurso no encontrado",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> manejarDomainException(DomainException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        "Conflicto de negocio",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
        final String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " +
                        (fieldError.getDefaultMessage() == null ? "invalido" : fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Validacion incorrecta",
                        mensaje
                )
        );
    }
}
