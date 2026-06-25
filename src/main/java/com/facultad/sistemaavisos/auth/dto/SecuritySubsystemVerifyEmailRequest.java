package com.facultad.sistemaavisos.auth.dto;

public record SecuritySubsystemVerifyEmailRequest(
        String mailUsuario,
        String codigo
) {
}
