package com.facultad.sistemaavisos.shared.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant fechaHora,
        int estado,
        String error,
        String mensaje
) {
}
