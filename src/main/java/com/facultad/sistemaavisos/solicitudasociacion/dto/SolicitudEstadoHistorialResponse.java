package com.facultad.sistemaavisos.solicitudasociacion.dto;

import java.time.Instant;

public record SolicitudEstadoHistorialResponse(
        String codigo,
        String estado,
        Instant fechaInicio,
        Instant fechaFin
) {
}
