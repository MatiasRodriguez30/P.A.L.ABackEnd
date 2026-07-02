package com.facultad.sistemaavisos.solicitudasociacion.dto;

public record SolicitudAsociacionCreateRequest(
        String cuitEmpresaSolicitud,
        String razonSocialEmpresaSolicitud,
        String mailEmpresaSolicitud,
        String telefonoEmpresaSolicitud
) {
}
