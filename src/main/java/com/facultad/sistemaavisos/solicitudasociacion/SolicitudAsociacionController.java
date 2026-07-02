package com.facultad.sistemaavisos.solicitudasociacion;

import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionCreateRequest;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionResponse;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionDetalleResponse;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionGestionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping
    @PreAuthorize("hasAuthority('GESTIONAR_SOLICITUD_ASOCIACION')")
    public ResponseEntity<java.util.List<SolicitudAsociacionDetalleResponse>> listar() {
        return ResponseEntity.ok(solicitudAsociacionService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GESTIONAR_SOLICITUD_ASOCIACION')")
    public ResponseEntity<SolicitudAsociacionDetalleResponse> detalle(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudAsociacionService.obtenerDetalle(id));
    }

    @PatchMapping("/{id}/tomar")
    @PreAuthorize("hasAuthority('GESTIONAR_SOLICITUD_ASOCIACION')")
    public ResponseEntity<SolicitudAsociacionDetalleResponse> tomar(
            @PathVariable Long id, Authentication authentication,
            @RequestBody(required = false) SolicitudAsociacionGestionRequest request) {
        return ResponseEntity.ok(solicitudAsociacionService.tomar(id, bearer(authentication), request));
    }

    @PatchMapping("/{id}/aceptar")
    @PreAuthorize("hasAuthority('GESTIONAR_SOLICITUD_ASOCIACION')")
    public ResponseEntity<SolicitudAsociacionDetalleResponse> aceptar(
            @PathVariable Long id, Authentication authentication,
            @RequestBody(required = false) SolicitudAsociacionGestionRequest request) {
        return ResponseEntity.ok(solicitudAsociacionService.aceptar(id, bearer(authentication), request));
    }

    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasAuthority('GESTIONAR_SOLICITUD_ASOCIACION')")
    public ResponseEntity<SolicitudAsociacionDetalleResponse> rechazar(
            @PathVariable Long id, Authentication authentication,
            @RequestBody(required = false) SolicitudAsociacionGestionRequest request) {
        return ResponseEntity.ok(solicitudAsociacionService.rechazar(id, bearer(authentication), request));
    }

    private String bearer(Authentication authentication) {
        return authentication != null && authentication.getCredentials() instanceof String credentials
                ? credentials : null;
    }
}
