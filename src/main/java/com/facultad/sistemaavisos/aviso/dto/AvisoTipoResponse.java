package com.facultad.sistemaavisos.aviso.dto;

import java.util.List;

public record AvisoTipoResponse(
        String nombreTipoAviso,
        List<String> subTiposAviso
) {
}
