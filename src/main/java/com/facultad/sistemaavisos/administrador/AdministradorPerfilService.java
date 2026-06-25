package com.facultad.sistemaavisos.administrador;

import com.facultad.sistemaavisos.administrador.dto.AdministradorPerfilResponse;
import com.facultad.sistemaavisos.administrador.dto.AdministradorPerfilUpdateRequest;
import com.facultad.sistemaavisos.security.JwtService;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdministradorPerfilService {

    private final AdministradorRepository administradorRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public AdministradorPerfilResponse obtenerPerfil(Authentication authentication) {
        return mapear(resolverAdministrador(authentication));
    }

    @Transactional
    public AdministradorPerfilResponse actualizarPerfil(Authentication authentication, AdministradorPerfilUpdateRequest request) {
        final Administrador administrador = resolverAdministrador(authentication);

        administrador.setNombreAdministrador(request.nombreAdministrador());
        administrador.setApellidoAdministrador(request.apellidoAdministrador());

        return mapear(administradorRepository.save(administrador));
    }

    private Administrador resolverAdministrador(Authentication authentication) {
        final String token = authentication != null && authentication.getCredentials() instanceof String credenciales
                ? credenciales
                : null;

        if (token == null) {
            throw new RecursoNoEncontradoException("No se pudo resolver la sesion del administrador");
        }

        final Long usuarioSeguridadId = jwtService.extraerSubjectId(token);

        return administradorRepository.findByUsuarioSeguridadId(usuarioSeguridadId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el perfil del administrador"));
    }

    private AdministradorPerfilResponse mapear(Administrador administrador) {
        return new AdministradorPerfilResponse(
                administrador.getId(),
                administrador.getNombreAdministrador(),
                administrador.getApellidoAdministrador(),
                administrador.getLegajoAdministrador(),
                administrador.getMailAdministrador()
        );
    }
}
