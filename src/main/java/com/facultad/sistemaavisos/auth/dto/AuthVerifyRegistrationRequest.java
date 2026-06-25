package com.facultad.sistemaavisos.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthVerifyRegistrationRequest(
        @NotBlank String mailUsuario,
        @NotBlank String codigo,
        @Valid @NotNull AuthRegisterRequest registro
) {
}
