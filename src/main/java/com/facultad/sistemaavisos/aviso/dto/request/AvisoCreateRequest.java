package com.facultad.sistemaavisos.aviso.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.List;

public record AvisoCreateRequest(
        @NotBlank String nombreAviso,
        @NotBlank String descripcionAviso,
        Instant fechaPublicacionAviso,
        @NotNull Instant fechaCierreAviso,
        String imagenUrlAviso,
        @NotNull @Positive Long empresaId,
        @NotNull Boolean guardarComoBorrador,
        @Valid @NotEmpty List<CarreraAvisoRequest> carreras,
        @Valid @NotEmpty List<TipoAvisoSeleccionRequest> tiposAviso
) {

    public record CarreraAvisoRequest(
            @NotNull @Positive Long carreraId,
            @NotNull @Positive Integer prioridad
    ) {
    }

    public record TipoAvisoSeleccionRequest(
            @NotNull @Positive Long tipoAvisoId,
            @NotEmpty List<@NotNull @Positive Long> subTipoAvisoIds
    ) {
    }
}
