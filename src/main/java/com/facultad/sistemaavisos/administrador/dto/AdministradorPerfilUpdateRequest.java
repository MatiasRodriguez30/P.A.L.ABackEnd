package com.facultad.sistemaavisos.administrador.dto;

import jakarta.validation.constraints.NotBlank;

public record AdministradorPerfilUpdateRequest(
        @NotBlank String nombreAdministrador,
        @NotBlank String apellidoAdministrador
) {
}
