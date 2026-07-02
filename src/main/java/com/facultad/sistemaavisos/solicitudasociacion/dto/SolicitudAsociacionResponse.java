package com.facultad.sistemaavisos.solicitudasociacion.dto;

import java.time.Instant;

public record SolicitudAsociacionResponse(
        Long id,
        String cuitEmpresaSolicitud,
        String razonSocialEmpresaSolicitud,
        String mailEmpresaSolicitud,
        String telefonoEmpresaSolicitud,
        String estadoSolicitud,
        Instant fechaEnvioSolicitud
) {
}
