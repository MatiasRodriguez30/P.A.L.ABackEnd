package com.facultad.sistemaavisos.experienciaacademica;

import com.facultad.sistemaavisos.experienciaacademica.dto.ExperienciaAcademicaRequest;
import com.facultad.sistemaavisos.experienciaacademica.dto.ExperienciaAcademicaResponse;
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
@RequestMapping("/api/postulante/me/experiencias-academicas")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
public class ExperienciaAcademicaController {

    private final ExperienciaAcademicaService experienciaAcademicaService;

    @PostMapping
    public ExperienciaAcademicaResponse agregar(Authentication authentication, @RequestBody @Valid ExperienciaAcademicaRequest request) {
        return experienciaAcademicaService.agregar(authentication, request);
    }

    @PatchMapping("/{id}")
    public ExperienciaAcademicaResponse actualizar(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid ExperienciaAcademicaRequest request
    ) {
        return experienciaAcademicaService.actualizar(authentication, id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(Authentication authentication, @PathVariable Long id) {
        experienciaAcademicaService.eliminar(authentication, id);
    }
}
