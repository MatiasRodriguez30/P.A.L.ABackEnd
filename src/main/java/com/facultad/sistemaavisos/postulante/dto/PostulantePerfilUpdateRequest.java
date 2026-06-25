package com.facultad.sistemaavisos.postulante.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostulantePerfilUpdateRequest(
        @NotBlank String nombrePostulante,
        @NotBlank String apellidoPostulante,
        @NotNull @Positive Long legajoAcademicoPostulante,
        @Email String mailAcademicoPostulante
) {
}
