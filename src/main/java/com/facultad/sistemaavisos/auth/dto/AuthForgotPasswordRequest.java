package com.facultad.sistemaavisos.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthForgotPasswordRequest(
        @Email @NotBlank String mailUsuario
) {
}
