package com.facultad.sistemaavisos.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record AuthCompleteProfileRequest(
        AdministradorProfileData administrador,
        ReclutadorProfileData reclutador,
        PostulanteProfileData postulante
) {

    public record AdministradorProfileData(
            @NotBlank String nombreAdministrador,
            @NotBlank String apellidoAdministrador,
            @NotNull @Positive Long legajoAdministrador
    ) {
    }

    public record ReclutadorProfileData(
            @NotBlank String nombreReclutador,
            @NotBlank String cuilReclutador,
            String descripcionReclutador
    ) {
    }

    public record PostulanteProfileData(
            @NotBlank String nombrePostulante,
            @NotBlank String apellidoPostulante,
            LocalDate fechaNacimientoPostulante,
            @NotNull @Positive Long legajoAcademicoPostulante,
            @Email String mailAcademicoPostulante,
            String mailPersonalPostulante,
            @NotNull @Positive Long tipoEstudianteId
    ) {
    }
}
