package com.facultad.sistemaavisos.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthResetPasswordRequest(
        @Email @NotBlank String mailUsuario,
        @NotBlank String codigo,
        @NotBlank String nuevaPassword
) {
}
