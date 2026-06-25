package com.facultad.sistemaavisos.administrador;

import com.facultad.sistemaavisos.administrador.dto.AdministradorPerfilResponse;
import com.facultad.sistemaavisos.administrador.dto.AdministradorPerfilUpdateRequest;
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
@RequestMapping("/api/administrador/me")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_ADMINISTRADOR')")
public class AdministradorPerfilController {

    private final AdministradorPerfilService administradorPerfilService;

    @GetMapping
    public AdministradorPerfilResponse obtenerPerfil(Authentication authentication) {
        return administradorPerfilService.obtenerPerfil(authentication);
    }

    @PatchMapping
    public AdministradorPerfilResponse actualizarPerfil(
            Authentication authentication,
            @RequestBody @Valid AdministradorPerfilUpdateRequest request
    ) {
        return administradorPerfilService.actualizarPerfil(authentication, request);
    }
}
