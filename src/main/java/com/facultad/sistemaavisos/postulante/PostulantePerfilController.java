package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.postulante.dto.PostulantePerfilResponse;
import com.facultad.sistemaavisos.postulante.dto.PostulantePerfilUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postulante/me")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
public class PostulantePerfilController {

    private final PostulantePerfilService postulantePerfilService;

    @GetMapping
    public PostulantePerfilResponse obtenerPerfil(Authentication authentication) {
        return postulantePerfilService.obtenerPerfil(authentication);
    }

    @PatchMapping
    public PostulantePerfilResponse actualizarPerfil(
            Authentication authentication,
            @RequestBody @Valid PostulantePerfilUpdateRequest request
    ) {
        return postulantePerfilService.actualizarPerfil(authentication, request);
    }
}
