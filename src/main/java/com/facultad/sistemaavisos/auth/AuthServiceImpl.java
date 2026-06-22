package com.facultad.sistemaavisos.auth;

import com.facultad.sistemaavisos.administrador.Administrador;
import com.facultad.sistemaavisos.administrador.AdministradorRepository;
import com.facultad.sistemaavisos.auth.dto.AuthCompleteProfileRequest;
import com.facultad.sistemaavisos.auth.dto.AuthLoginRequest;
import com.facultad.sistemaavisos.auth.dto.AuthRegisterRequest;
import com.facultad.sistemaavisos.auth.dto.AuthResponse;
import com.facultad.sistemaavisos.auth.dto.SecuritySubsystemExternalRegisterRequest;
import com.facultad.sistemaavisos.auth.dto.SecuritySubsystemLoginResponse;
import com.facultad.sistemaavisos.postulante.Postulante;
import com.facultad.sistemaavisos.postulante.PostulanteRepository;
import com.facultad.sistemaavisos.reclutador.Reclutador;
import com.facultad.sistemaavisos.reclutador.ReclutadorRepository;
import com.facultad.sistemaavisos.security.JwtService;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import com.facultad.sistemaavisos.tipoestudiante.TipoEstudiante;
import com.facultad.sistemaavisos.tipoestudiante.TipoEstudianteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {

    private final RestClient restClient;
    private final JwtService jwtService;
    private final String systemKey;
    private final AdministradorRepository administradorRepository;
    private final PostulanteRepository postulanteRepository;
    private final ReclutadorRepository reclutadorRepository;
    private final TipoEstudianteRepository tipoEstudianteRepository;

    public AuthServiceImpl(
            RestClient.Builder restClientBuilder,
            JwtService jwtService,
            AdministradorRepository administradorRepository,
            PostulanteRepository postulanteRepository,
            ReclutadorRepository reclutadorRepository,
            TipoEstudianteRepository tipoEstudianteRepository,
            @Value("${security.subsystem.base-url}") String baseUrl,
            @Value("${security.subsystem.system-key}") String systemKey
    ) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.jwtService = jwtService;
        this.administradorRepository = administradorRepository;
        this.postulanteRepository = postulanteRepository;
        this.reclutadorRepository = reclutadorRepository;
        this.tipoEstudianteRepository = tipoEstudianteRepository;
        this.systemKey = systemKey;
    }

    @Override
    public AuthResponse login(AuthLoginRequest request) {
        final AuthLoginRequest requestNormalizado = new AuthLoginRequest(
                normalizarMail(request.mailUsuario()),
                request.passwordUsuario()
        );
        final SecuritySubsystemLoginResponse response = invocarSubsistema("/api/auth/external/login", requestNormalizado);
        sincronizarActorLocalPostLogin(response);
        return mapResponse(response);
    }

    @Override
    public AuthResponse register(AuthRegisterRequest request) {
        final AuthRegisterRequest requestNormalizado = normalizarRegistro(request);
        validarRegistro(requestNormalizado);

        final SecuritySubsystemLoginResponse response = invocarSubsistema(
                "/api/auth/external/register",
                new SecuritySubsystemExternalRegisterRequest(
                        requestNormalizado.mailUsuario(),
                        requestNormalizado.passwordUsuario(),
                        requestNormalizado.rolSolicitado().getClave()
                )
        );

        final Long usuarioSistemaId = jwtService.extraerSubjectId(response.token());
        crearActorLocalSiCorresponde(requestNormalizado, usuarioSistemaId);

        return mapResponse(response);
    }

    @Override
    public AuthResponse completarPerfilInicial(String bearerToken, AuthCompleteProfileRequest request) {
        if (bearerToken == null) {
            throw new OperacionInvalidaException("No se encontro el token autenticado para completar el perfil");
        }

        final Long usuarioSistemaId = jwtService.extraerSubjectId(bearerToken);
        final String mailUsuario = normalizarMail(jwtService.extraerMail(bearerToken));
        final List<String> roles = jwtService.extraerRoles(bearerToken);
        final AuthCompleteProfileRequest requestNormalizado = normalizarPerfil(request);

        if (tieneRol(roles, PalaRol.ADMINISTRADOR)) {
            completarAdministrador(usuarioSistemaId, mailUsuario, requestNormalizado);
        }

        if (tieneRol(roles, PalaRol.RECLUTADOR)) {
            completarReclutador(usuarioSistemaId, mailUsuario, requestNormalizado);
        }

        if (tieneRol(roles, PalaRol.POSTULANTE)) {
            completarPostulante(usuarioSistemaId, mailUsuario, requestNormalizado);
        }

        return construirSesionActual(bearerToken, mailUsuario);
    }

    @Override
    public AuthResponse construirSesionActual(String bearerToken, String mailUsuario) {
        final List<String> roles = bearerToken == null ? List.of() : jwtService.extraerRoles(bearerToken);
        final List<String> permisos = bearerToken == null ? List.of() : jwtService.extraerPermisos(bearerToken);
        final Long usuarioSistemaId = bearerToken == null ? null : jwtService.extraerSubjectId(bearerToken);
        final String perfilPendiente = bearerToken == null ? null : resolverPerfilPendiente(usuarioSistemaId, roles);

        return new AuthResponse(
                null,
                "Bearer",
                usuarioSistemaId,
                mailUsuario,
                roles,
                permisos,
                perfilPendiente == null,
                perfilPendiente
        );
    }

    private SecuritySubsystemLoginResponse invocarSubsistema(String path, Object request) {
        try {
            return restClient.post()
                    .uri(path)
                    .header("X-System-Key", systemKey)
                    .body(request)
                    .retrieve()
                    .body(SecuritySubsystemLoginResponse.class);
        } catch (RestClientResponseException ex) {
            throw new OperacionInvalidaException(extraerMensajeSubsistema(ex));
        } catch (RestClientException ex) {
            throw new OperacionInvalidaException(
                    "No se pudo establecer comunicacion con el subsistema de seguridad"
            );
        }
    }

    private String extraerMensajeSubsistema(RestClientResponseException ex) {
        final String responseBody = ex.getResponseBodyAsString();
        if (responseBody == null || responseBody.isBlank()) {
            return "No se pudo autenticar contra el subsistema de seguridad";
        }

        final String mensaje = extraerCampoJson(responseBody, "mensaje");
        if (mensaje != null) {
            return mensaje;
        }

        final String message = extraerCampoJson(responseBody, "message");
        if (message != null) {
            return message;
        }

        return responseBody;
    }

    private String extraerCampoJson(String responseBody, String campo) {
        final String patron = "\"" + campo + "\":\"";
        final int inicio = responseBody.indexOf(patron);
        if (inicio < 0) {
            return null;
        }

        final int desde = inicio + patron.length();
        final int hasta = responseBody.indexOf('"', desde);
        if (hasta < 0) {
            return null;
        }

        return responseBody.substring(desde, hasta);
    }

    private AuthResponse mapResponse(SecuritySubsystemLoginResponse response) {
        if (response == null || response.token() == null) {
            throw new OperacionInvalidaException("El subsistema de seguridad devolvio una respuesta invalida");
        }

        final Long usuarioSistemaId = jwtService.extraerSubjectId(response.token());

        return new AuthResponse(
                response.token(),
                response.tipo(),
                usuarioSistemaId,
                response.mailUsuario(),
                response.roles() == null ? List.of() : response.roles(),
                response.permisos() == null ? List.of() : response.permisos(),
                resolverPerfilPendiente(usuarioSistemaId, response.roles() == null ? List.of() : response.roles()) == null,
                resolverPerfilPendiente(usuarioSistemaId, response.roles() == null ? List.of() : response.roles())
        );
    }

    private void sincronizarActorLocalPostLogin(SecuritySubsystemLoginResponse response) {
        final Long usuarioSistemaId = jwtService.extraerSubjectId(response.token());
        final List<String> roles = response.roles() == null ? List.of() : response.roles();
        final String mailUsuario = normalizarMail(response.mailUsuario());

        if (roles.stream().anyMatch(rol -> PalaRol.ADMINISTRADOR.getNombreVisible().equalsIgnoreCase(rol))) {
            sincronizarAdministrador(mailUsuario, usuarioSistemaId);
        }

        if (roles.stream().anyMatch(rol -> PalaRol.RECLUTADOR.getNombreVisible().equalsIgnoreCase(rol))) {
            sincronizarReclutador(mailUsuario, usuarioSistemaId);
        }

        if (roles.stream().anyMatch(rol -> PalaRol.POSTULANTE.getNombreVisible().equalsIgnoreCase(rol))) {
            sincronizarPostulante(mailUsuario, usuarioSistemaId);
        }
    }

    private void sincronizarAdministrador(String mailUsuario, Long usuarioSistemaId) {
        final Administrador administradorExistente = administradorRepository
                .findByMailAdministradorIgnoreCaseAndFechaBajaAdministradorIsNull(mailUsuario)
                .orElse(null);

        if (administradorExistente != null) {
            administradorExistente.setUsuarioSeguridadId(usuarioSistemaId);
            administradorRepository.save(administradorExistente);
        }
    }

    private void sincronizarReclutador(String mailUsuario, Long usuarioSistemaId) {
        final Reclutador reclutador = reclutadorRepository
                .findByMailReclutadorIgnoreCaseAndFechaBajaReclutadorIsNull(mailUsuario)
                .orElseThrow(() -> new OperacionInvalidaException(
                        "El usuario autenticado tiene rol de reclutador, pero no existe su perfil local en PALA. Debe registrarse con el formulario completo."
                ));

        reclutador.setUsuarioSeguridadId(usuarioSistemaId);
        reclutadorRepository.save(reclutador);
    }

    private void sincronizarPostulante(String mailUsuario, Long usuarioSistemaId) {
        final Postulante postulante = postulanteRepository
                .findByMailAcademicoPostulanteIgnoreCaseAndFechaBajaPostulanteIsNull(mailUsuario)
                .or(() -> postulanteRepository.findByMailPersonalPostulanteIgnoreCaseAndFechaBajaPostulanteIsNull(mailUsuario))
                .orElseThrow(() -> new OperacionInvalidaException(
                        "El usuario autenticado tiene rol de postulante, pero no existe su perfil local en PALA. Debe registrarse con el formulario completo."
                ));

        postulante.setUsuarioSeguridadId(usuarioSistemaId);
        postulanteRepository.save(postulante);
    }

    private String extraerNombreBase(String mailUsuario) {
        final String localPart = mailUsuario.split("@")[0];
        if (localPart.isBlank()) {
            return "Administrador";
        }

        final String normalized = localPart.replace('.', ' ').replace('_', ' ').trim();
        if (normalized.isBlank()) {
            return "Administrador";
        }

        final String capitalized = normalized.substring(0, 1).toUpperCase(Locale.ROOT) +
                normalized.substring(1);
        return capitalized.length() > 100 ? capitalized.substring(0, 100) : capitalized;
    }

    private void validarRegistro(AuthRegisterRequest request) {
        final String rolSolicitado = request.rolSolicitado() == null
                ? null
                : request.rolSolicitado().getClave();

        if (PalaRol.RECLUTADOR.getClave().equals(rolSolicitado)) {
            if (request.reclutador() == null) {
                throw new OperacionInvalidaException(
                        "Debe informar los datos del reclutador para registrarse con ese rol"
                );
            }
            return;
        }

        if (PalaRol.POSTULANTE.getClave().equals(rolSolicitado)) {
            if (request.postulante() == null) {
                throw new OperacionInvalidaException(
                        "Debe informar los datos del postulante para registrarse con ese rol"
                );
            }
            return;
        }

        throw new OperacionInvalidaException("El rol solicitado no es valido para el registro externo en PALA");
    }

    private void crearActorLocalSiCorresponde(AuthRegisterRequest request, Long usuarioSistemaId) {
        final String rolSolicitado = request.rolSolicitado().getClave();

        if (PalaRol.RECLUTADOR.getClave().equals(rolSolicitado)) {
            crearReclutador(request, usuarioSistemaId);
            return;
        }

        if (PalaRol.POSTULANTE.getClave().equals(rolSolicitado)) {
            crearPostulante(request, usuarioSistemaId);
        }
    }

    private void crearReclutador(AuthRegisterRequest request, Long usuarioSistemaId) {
        if (reclutadorRepository.findByUsuarioSeguridadId(usuarioSistemaId).isPresent()) {
            return;
        }

        final AuthRegisterRequest.ReclutadorRegisterData reclutadorRequest = request.reclutador();

        final Reclutador reclutador = Reclutador.builder()
                .usuarioSeguridadId(usuarioSistemaId)
                .mailReclutador(normalizarMail(request.mailUsuario()))
                .nombreReclutador(reclutadorRequest.nombreReclutador())
                .cuilReclutador(reclutadorRequest.cuilReclutador())
                .descripcionReclutador(reclutadorRequest.descripcionReclutador())
                .build();

        reclutadorRepository.save(reclutador);
    }

    private void crearPostulante(AuthRegisterRequest request, Long usuarioSistemaId) {
        if (postulanteRepository.findByUsuarioSeguridadId(usuarioSistemaId).isPresent()) {
            return;
        }

        final AuthRegisterRequest.PostulanteRegisterData postulanteRequest = request.postulante();
        final TipoEstudiante tipoEstudiante = tipoEstudianteRepository
                .findByIdAndFechaBajaTipoEstudianteIsNull(postulanteRequest.tipoEstudianteId())
                .orElseThrow(() -> new OperacionInvalidaException(
                        "No se encontro el tipo de estudiante indicado para el registro"
                ));

        final Postulante postulante = Postulante.builder()
                .usuarioSeguridadId(usuarioSistemaId)
                .nombrePostulante(postulanteRequest.nombrePostulante())
                .apellidoPostulante(postulanteRequest.apellidoPostulante())
                .fechaNacimientoPostulante(postulanteRequest.fechaNacimientoPostulante())
                .legajoAcademicoPostulante(postulanteRequest.legajoAcademicoPostulante())
                .mailAcademicoPostulante(null)
                .mailPersonalPostulante(normalizarMail(request.mailUsuario()))
                .tipoEstudiante(tipoEstudiante)
                .build();

        postulanteRepository.save(postulante);
    }

    private void completarReclutador(
            Long usuarioSistemaId,
            String mailUsuario,
            AuthCompleteProfileRequest request
    ) {
        if (reclutadorRepository.findByUsuarioSeguridadId(usuarioSistemaId).isPresent()) {
            return;
        }

        if (request.reclutador() == null) {
            throw new OperacionInvalidaException("Debe completar los datos del reclutador");
        }

        final AuthCompleteProfileRequest.ReclutadorProfileData reclutadorRequest = request.reclutador();
        final Reclutador reclutador = Reclutador.builder()
                .usuarioSeguridadId(usuarioSistemaId)
                .mailReclutador(normalizarMail(mailUsuario))
                .nombreReclutador(reclutadorRequest.nombreReclutador())
                .cuilReclutador(reclutadorRequest.cuilReclutador())
                .descripcionReclutador(reclutadorRequest.descripcionReclutador())
                .build();

        reclutadorRepository.save(reclutador);
    }

    private void completarAdministrador(
            Long usuarioSistemaId,
            String mailUsuario,
            AuthCompleteProfileRequest request
    ) {
        if (administradorRepository.findByUsuarioSeguridadId(usuarioSistemaId).isPresent()) {
            return;
        }

        final Administrador administradorExistente = administradorRepository
                .findByMailAdministradorIgnoreCaseAndFechaBajaAdministradorIsNull(mailUsuario)
                .orElse(null);

        if (administradorExistente != null) {
            administradorExistente.setUsuarioSeguridadId(usuarioSistemaId);
            administradorRepository.save(administradorExistente);
            return;
        }

        if (request.administrador() == null) {
            throw new OperacionInvalidaException("Debe completar los datos del administrador");
        }

        final AuthCompleteProfileRequest.AdministradorProfileData administradorRequest = request.administrador();
        final Administrador administrador = Administrador.builder()
                .usuarioSeguridadId(usuarioSistemaId)
                .nombreAdministrador(administradorRequest.nombreAdministrador())
                .apellidoAdministrador(administradorRequest.apellidoAdministrador())
                .mailAdministrador(normalizarMail(mailUsuario))
                .legajoAdministrador(administradorRequest.legajoAdministrador())
                .fechaAltaAdministrador(Instant.now())
                .build();

        administradorRepository.save(administrador);
    }

    private void completarPostulante(
            Long usuarioSistemaId,
            String mailUsuario,
            AuthCompleteProfileRequest request
    ) {
        if (postulanteRepository.findByUsuarioSeguridadId(usuarioSistemaId).isPresent()) {
            return;
        }

        if (request.postulante() == null) {
            throw new OperacionInvalidaException("Debe completar los datos del postulante");
        }

        final AuthCompleteProfileRequest.PostulanteProfileData postulanteRequest = request.postulante();
        final TipoEstudiante tipoEstudiante = tipoEstudianteRepository
                .findByIdAndFechaBajaTipoEstudianteIsNull(postulanteRequest.tipoEstudianteId())
                .orElseThrow(() -> new OperacionInvalidaException(
                        "No se encontro el tipo de estudiante indicado para el perfil inicial"
                ));

        final Postulante postulante = Postulante.builder()
                .usuarioSeguridadId(usuarioSistemaId)
                .nombrePostulante(postulanteRequest.nombrePostulante())
                .apellidoPostulante(postulanteRequest.apellidoPostulante())
                .fechaNacimientoPostulante(postulanteRequest.fechaNacimientoPostulante())
                .legajoAcademicoPostulante(postulanteRequest.legajoAcademicoPostulante())
                .mailAcademicoPostulante(normalizarMail(postulanteRequest.mailAcademicoPostulante()))
                .mailPersonalPostulante(
                        postulanteRequest.mailPersonalPostulante() != null
                                ? normalizarMail(postulanteRequest.mailPersonalPostulante())
                                : normalizarMail(mailUsuario)
                )
                .tipoEstudiante(tipoEstudiante)
                .build();

        postulanteRepository.save(postulante);
    }

    private String resolverPerfilPendiente(Long usuarioSistemaId, List<String> roles) {
        if (usuarioSistemaId == null) {
            return null;
        }

        if (tieneRol(roles, PalaRol.ADMINISTRADOR) &&
                administradorRepository.findByUsuarioSeguridadId(usuarioSistemaId).isEmpty()) {
            return PalaRol.ADMINISTRADOR.getClave();
        }

        return null;
    }

    private boolean tieneRol(List<String> roles, PalaRol rol) {
        return roles.stream().anyMatch(nombre -> rol.getNombreVisible().equalsIgnoreCase(nombre));
    }

    private AuthRegisterRequest normalizarRegistro(AuthRegisterRequest request) {
        if (request == null) {
            return null;
        }

        final AuthRegisterRequest.PostulanteRegisterData postulante = request.postulante() == null
                ? null
                : new AuthRegisterRequest.PostulanteRegisterData(
                        request.postulante().nombrePostulante(),
                        request.postulante().apellidoPostulante(),
                        request.postulante().fechaNacimientoPostulante(),
                        request.postulante().legajoAcademicoPostulante(),
                        request.postulante().tipoEstudianteId()
                );

        return new AuthRegisterRequest(
                normalizarMail(request.mailUsuario()),
                request.passwordUsuario(),
                request.rolSolicitado(),
                request.reclutador(),
                postulante
        );
    }

    private AuthCompleteProfileRequest normalizarPerfil(AuthCompleteProfileRequest request) {
        if (request == null) {
            return request;
        }

        return new AuthCompleteProfileRequest(
                request.administrador(),
                request.reclutador(),
                request.postulante() == null
                        ? null
                        : new AuthCompleteProfileRequest.PostulanteProfileData(
                                request.postulante().nombrePostulante(),
                                request.postulante().apellidoPostulante(),
                                request.postulante().fechaNacimientoPostulante(),
                                request.postulante().legajoAcademicoPostulante(),
                                normalizarMail(request.postulante().mailAcademicoPostulante()),
                                normalizarMail(request.postulante().mailPersonalPostulante()),
                                request.postulante().tipoEstudianteId()
                        )
        );
    }

    private String normalizarMail(String mail) {
        return mail == null ? null : mail.trim().toLowerCase(Locale.ROOT);
    }
}
