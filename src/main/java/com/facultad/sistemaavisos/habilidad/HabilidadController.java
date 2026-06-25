package com.facultad.sistemaavisos.habilidad;

import com.facultad.sistemaavisos.habilidad.dto.HabilidadRequest;
import com.facultad.sistemaavisos.habilidad.dto.HabilidadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postulante/me/habilidades")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
public class HabilidadController {

    private final HabilidadService habilidadService;

    @PostMapping
    public HabilidadResponse agregar(Authentication authentication, @RequestBody @Valid HabilidadRequest request) {
        return habilidadService.agregar(authentication, request);
    }

    @PatchMapping("/{id}")
    public HabilidadResponse actualizar(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid HabilidadRequest request
    ) {
        return habilidadService.actualizar(authentication, id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(Authentication authentication, @PathVariable Long id) {
        habilidadService.eliminar(authentication, id);
    }
}
