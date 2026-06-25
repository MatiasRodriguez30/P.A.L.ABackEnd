package com.facultad.sistemaavisos.experiencia;

import com.facultad.sistemaavisos.experiencia.dto.ExperienciaRequest;
import com.facultad.sistemaavisos.experiencia.dto.ExperienciaResponse;
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
@RequestMapping("/api/postulante/me/experiencias")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
public class ExperienciaController {

    private final ExperienciaService experienciaService;

    @PostMapping
    public ExperienciaResponse agregar(Authentication authentication, @RequestBody @Valid ExperienciaRequest request) {
        return experienciaService.agregar(authentication, request);
    }

    @PatchMapping("/{id}")
    public ExperienciaResponse actualizar(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid ExperienciaRequest request
    ) {
        return experienciaService.actualizar(authentication, id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(Authentication authentication, @PathVariable Long id) {
        experienciaService.eliminar(authentication, id);
    }
}
