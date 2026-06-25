package com.facultad.sistemaavisos.habilidad.dto;

import jakarta.validation.constraints.NotBlank;

public record HabilidadRequest(
        @NotBlank String nombreHabilidad
) {
}
