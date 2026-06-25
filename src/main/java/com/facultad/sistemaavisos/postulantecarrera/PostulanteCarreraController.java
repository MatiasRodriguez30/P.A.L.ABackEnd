package com.facultad.sistemaavisos.postulantecarrera;

import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraCreateRequest;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraResponse;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraUpdateRequest;
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
@RequestMapping("/api/postulante/me/carreras")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
public class PostulanteCarreraController {

    private final PostulanteCarreraService postulanteCarreraService;

    @PostMapping
    public PostulanteCarreraResponse agregar(Authentication authentication, @RequestBody @Valid PostulanteCarreraCreateRequest request) {
        return postulanteCarreraService.agregar(authentication, request);
    }

    @PatchMapping("/{id}")
    public PostulanteCarreraResponse actualizar(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid PostulanteCarreraUpdateRequest request
    ) {
        return postulanteCarreraService.actualizar(authentication, id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(Authentication authentication, @PathVariable Long id) {
        postulanteCarreraService.eliminar(authentication, id);
    }
}
