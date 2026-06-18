package com.facultad.sistemaavisos.aviso.dto;

import java.time.Instant;
import java.util.List;

public record AvisoDetalleResponse(
        Long id,
        String nombreAviso,
        String descripcionAviso,
        Instant fechaCierreAviso,
        Instant fechaCreacionAviso,
        String imagenUrlAviso,
        String razonSocialEmpresa,
        String nombreReclutador,
        List<String> carreras,
        List<AvisoTipoResponse> tiposAviso
) {
}
