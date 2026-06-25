package com.facultad.sistemaavisos.postulantecarrera.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PostulanteCarreraCreateRequest(
        @NotNull Long carreraId,
        LocalDate fechaDesdePostulanteCarrera,
        LocalDate fechaHastaPostulanteCarrera
) {
}
