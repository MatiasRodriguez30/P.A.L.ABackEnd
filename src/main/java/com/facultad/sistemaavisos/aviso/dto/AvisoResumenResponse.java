package com.facultad.sistemaavisos.aviso.dto;

import java.time.Instant;
import java.util.List;

public record AvisoResumenResponse(
        Long id,
        String nombreAviso,
        String descripcionAviso,
        Instant fechaCierreAviso,
        String razonSocialEmpresa,
        String nombreReclutador,
        List<String> carreras,
        List<AvisoTipoResponse> tiposAviso
) {
}
