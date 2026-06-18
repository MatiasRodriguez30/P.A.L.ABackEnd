package com.facultad.sistemaavisos.shared.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime fechaHora,
        int estado,
        String error,
        String mensaje
) {
}
