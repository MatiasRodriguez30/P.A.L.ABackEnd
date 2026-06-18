package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoResponse;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoUpdateRequest;

@RestController
@RequestMapping("/api/reclutadores/{reclutadorId}/avisos")
@RequiredArgsConstructor
public class AvisoReclutadorController {

    private final AvisoService avisoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvisoResponse crearAviso(
            @PathVariable Long reclutadorId,
            @RequestBody @Valid AvisoCreateRequest request
    ) {
        return avisoService.crearAviso(reclutadorId, request);
    }

    @GetMapping("/{avisoId}")
    public AvisoResponse obtenerAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.obtenerAvisoDelReclutador(reclutadorId, avisoId);
    }

    @PutMapping("/{avisoId}")
    public AvisoResponse actualizarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId,
            @RequestBody @Valid AvisoUpdateRequest request
    ) {
        return avisoService.actualizarAviso(reclutadorId, avisoId, request);
    }

    @PatchMapping("/{avisoId}/pausar")
    public AvisoResponse pausarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.pausarAviso(reclutadorId, avisoId);
    }

    @PatchMapping("/{avisoId}/reanudar")
    public AvisoResponse reanudarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.reanudarAviso(reclutadorId, avisoId);
    }

    @PatchMapping("/{avisoId}/cancelar")
    public AvisoResponse cancelarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.cancelarAviso(reclutadorId, avisoId);
    }
}
