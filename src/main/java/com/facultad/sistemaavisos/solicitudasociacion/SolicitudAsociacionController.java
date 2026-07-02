package com.facultad.sistemaavisos.solicitudasociacion;

import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionCreateRequest;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solicitudes-asociacion")
public class SolicitudAsociacionController {

    private final SolicitudAsociacionService solicitudAsociacionService;

    public SolicitudAsociacionController(SolicitudAsociacionService solicitudAsociacionService) {
        this.solicitudAsociacionService = solicitudAsociacionService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SOLICITAR_ASOCIACION_RECLUTADOR')")
    public ResponseEntity<SolicitudAsociacionResponse> crear(
            Authentication authentication,
            @RequestBody @Valid SolicitudAsociacionCreateRequest request
    ) {
        final String bearer = authentication == null || !(authentication.getCredentials() instanceof String)
                ? null
                : (String) authentication.getCredentials();

        return ResponseEntity.ok(
                solicitudAsociacionService.crearSolicitud(
                        bearer,
                        authentication != null ? authentication.getName() : null,
                        request
                )
        );
    }
}
