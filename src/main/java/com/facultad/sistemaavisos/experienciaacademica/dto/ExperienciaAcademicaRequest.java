package com.facultad.sistemaavisos.experienciaacademica.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ExperienciaAcademicaRequest(
        @NotBlank String nombreInstitucionExpAcademica,
        @NotBlank String tituloExpAcademica,
        LocalDate fechaDesdeExpAcademica,
        LocalDate fechaHastaExpAcademica
) {
}
