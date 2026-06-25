package com.facultad.sistemaavisos.auth.dto;

public record SecuritySubsystemResetPasswordRequest(
        String mailUsuario,
        String codigo,
        String nuevaPassword
) {
}
