package com.facultad.sistemaavisos.postulantecarrera.dto;

import java.time.LocalDate;

public record PostulanteCarreraResponse(
        Long id,
        Long carreraId,
        String nombreCarrera,
        LocalDate fechaDesdePostulanteCarrera,
        LocalDate fechaHastaPostulanteCarrera
) {
}
