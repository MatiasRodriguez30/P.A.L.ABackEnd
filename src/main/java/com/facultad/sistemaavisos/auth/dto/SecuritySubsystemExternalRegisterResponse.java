package com.facultad.sistemaavisos.auth.dto;

public record SecuritySubsystemExternalRegisterResponse(
        Long usuarioId,
        String mailUsuario,
        String systemKey,
        boolean verificationRequired,
        String message
) {
}
