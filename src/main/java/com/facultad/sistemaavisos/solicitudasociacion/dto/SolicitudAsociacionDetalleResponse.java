package com.facultad.sistemaavisos.solicitudasociacion.dto;

import java.time.Instant;
import java.util.List;

public record SolicitudAsociacionDetalleResponse(
        Long id,
        Instant fechaEnvio,
        Instant fechaResolucion,
        String codigoEstado,
        String estado,
        ReclutadorDetalle reclutador,
        EmpresaDetalle empresa,
        String observacionesInternas,
        AdministradorDetalle resueltaPor,
        List<SolicitudEstadoHistorialResponse> historial
) {
    public record ReclutadorDetalle(Long id, String nombre, String mail, String cuil, String descripcion) {}
    public record EmpresaDetalle(String razonSocial, String cuit, String mail, String telefono, boolean existente) {}
    public record AdministradorDetalle(Long id, String nombre, String apellido, String mail) {}
}
