package com.facultad.sistemaavisos.aviso.dto;

import java.util.List;

public record AvisoFormularioSoporteResponse(
        List<EmpresaActivaResponse> empresasActivas,
        List<CarreraActivaResponse> carrerasActivas,
        List<TipoAvisoActivoResponse> tiposAvisoActivos
) {

    public record EmpresaActivaResponse(
            Long id,
            String cuitEmpresa,
            String razonSocialEmpresa
    ) {
    }

    public record CarreraActivaResponse(
            Long id,
            String nombreCarrera,
            String descripcionCarrera
    ) {
    }

    public record TipoAvisoActivoResponse(
            Long id,
            String nombreTipoAviso,
            String descripcionTipoAviso,
            List<SubTipoAvisoActivoResponse> subTiposAviso
    ) {
    }

    public record SubTipoAvisoActivoResponse(
            Long id,
            String nombreSubTipoAviso
    ) {
    }
}
