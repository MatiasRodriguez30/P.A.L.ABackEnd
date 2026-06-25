package com.facultad.sistemaavisos.experiencia.dto;

import java.time.LocalDate;

public record ExperienciaResponse(
        Long id,
        String descripcionExperiencia,
        LocalDate fechaDesdeExp,
        LocalDate fechaHastaExp,
        String nombreCargoExperiencia,
        String nombreEmpresaExperiencia
) {
}
