package com.facultad.sistemaavisos.auth.dto;

public record AuthRegisterStartResponse(
        Long usuarioId,
        String mailUsuario,
        boolean verificationRequired,
        String message
) {
}
