package com.facultad.sistemaavisos.aviso.dto;

import java.time.Instant;
import java.util.List;

public record AvisoResponse(
        Long id,
        String nombreAviso,
        String descripcionAviso,
        Instant fechaCreacionAviso,
        Instant fechaPublicacionAviso,
        Instant fechaCierreAviso,
        Instant fechaBajaAviso,
        String imagenUrlAviso,
        EstadoAvisoResponse estadoActual,
        EmpresaResponse empresa,
        ReclutadorResponse reclutador,
        List<CarreraResponse> carreras,
        List<TipoAvisoResponse> tiposAviso
) {

    public record EstadoAvisoResponse(
            Long id,
            String codigoInterno,
            String nombreEstadoAviso
    ) {
    }

    public record EmpresaResponse(
            Long id,
            String cuitEmpresa,
            String razonSocialEmpresa
    ) {
    }

    public record ReclutadorResponse(
            Long id,
            String cuilReclutador,
            String nombreReclutador
    ) {
    }

    public record CarreraResponse(
            Long id,
            String nombreCarrera,
            Integer prioridad
    ) {
    }

    public record TipoAvisoResponse(
            Long id,
            String nombreTipoAviso,
            List<SubTipoAvisoResponse> subTiposAviso
    ) {
    }

    public record SubTipoAvisoResponse(
            Long id,
            String nombreSubTipoAviso
    ) {
    }
}
