package com.facultad.sistemaavisos.experiencia.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ExperienciaRequest(
        String descripcionExperiencia,
        LocalDate fechaDesdeExp,
        LocalDate fechaHastaExp,
        @NotBlank String nombreCargoExperiencia,
        @NotBlank String nombreEmpresaExperiencia
) {
}
