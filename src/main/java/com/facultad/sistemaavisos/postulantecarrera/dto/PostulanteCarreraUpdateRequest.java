package com.facultad.sistemaavisos.postulantecarrera.dto;

import java.time.LocalDate;

public record PostulanteCarreraUpdateRequest(
        LocalDate fechaDesdePostulanteCarrera,
        LocalDate fechaHastaPostulanteCarrera
) {
}
