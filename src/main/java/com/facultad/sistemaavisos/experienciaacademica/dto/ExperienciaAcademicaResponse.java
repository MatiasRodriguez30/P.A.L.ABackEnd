package com.facultad.sistemaavisos.experienciaacademica.dto;

import java.time.LocalDate;

public record ExperienciaAcademicaResponse(
        Long id,
        String nombreInstitucionExpAcademica,
        String tituloExpAcademica,
        LocalDate fechaDesdeExpAcademica,
        LocalDate fechaHastaExpAcademica
) {
}
