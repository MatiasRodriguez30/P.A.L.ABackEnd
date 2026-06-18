package com.facultad.sistemaavisos.aviso.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.List;

public record AvisoUpdateRequest(
        @NotBlank String nombreAviso,
        @NotBlank String descripcionAviso,
        Instant fechaPublicacionAviso,
        @NotNull Instant fechaCierreAviso,
        String imagenUrlAviso,
        @NotNull @Positive Long empresaId,
        @NotNull Boolean guardarComoBorrador,
        @Valid @NotEmpty List<AvisoCreateRequest.CarreraAvisoRequest> carreras,
        @Valid @NotEmpty List<AvisoCreateRequest.TipoAvisoSeleccionRequest> tiposAviso
) {
}
