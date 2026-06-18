package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoDetalleResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoResumenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/postulantes/{postulanteId}/avisos")
@RequiredArgsConstructor
public class AvisoController {

    private final AvisoService avisoService;

    @GetMapping("/disponibles")
    public List<AvisoResumenResponse> listarDisponibles(@PathVariable Long postulanteId) {
        return avisoService.listarDisponiblesParaPostulante(postulanteId);
    }

    @GetMapping("/disponibles/{avisoId}")
    public AvisoDetalleResponse obtenerDetalle(
            @PathVariable Long postulanteId,
            @PathVariable Long avisoId
    ) {
        return avisoService.obtenerDetalleDisponibleParaPostulante(postulanteId, avisoId);
    }
}
