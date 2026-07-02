package com.facultad.sistemaavisos.solicitudasociacion;

import com.facultad.sistemaavisos.administrador.Administrador;
import com.facultad.sistemaavisos.administrador.AdministradorRepository;
import com.facultad.sistemaavisos.auth.PalaRol;
import com.facultad.sistemaavisos.empresa.Empresa;
import com.facultad.sistemaavisos.empresa.EmpresaRepository;
import com.facultad.sistemaavisos.estadosolicitud.EstadoSolicitud;
import com.facultad.sistemaavisos.estadosolicitud.EstadoSolicitudRepository;
import com.facultad.sistemaavisos.reclutador.Reclutador;
import com.facultad.sistemaavisos.reclutador.ReclutadorRepository;
import com.facultad.sistemaavisos.security.JwtService;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionCreateRequest;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudAsociacionServiceImpl implements SolicitudAsociacionService {

    private static final List<String> ESTADOS_PENDIENTES = List.of("ENVIADA", "EN_EVALUACION");

    private final SolicitudAsociacionRepository solicitudAsociacionRepository;
    private final ReclutadorRepository reclutadorRepository;
    private final EmpresaRepository empresaRepository;
    private final AdministradorRepository administradorRepository;
    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final JwtService jwtService;

    @Override
    public SolicitudAsociacionResponse crearSolicitud(
            String bearerToken,
            String mailUsuario,
            SolicitudAsociacionCreateRequest request
    ) {
        if (bearerToken == null) {
            throw new OperacionInvalidaException("No se encontro una sesion autenticada");
        }

        final Long usuarioSistemaId = jwtService.extraerSubjectId(bearerToken);
        final Reclutador reclutador = reclutadorRepository.findByUsuarioSeguridadId(usuarioSistemaId)
                .orElseThrow(() -> new OperacionInvalidaException("Necesitas un perfil de reclutador para solicitar asociacion"));

        if (solicitudAsociacionRepository.existsByReclutador_IdAndCuitEmpresaSolicitudAndFechaBajaSolicitudAsociacionIsNullAndEstadoActual_CodigoInternoIn(
                reclutador.getId(),
                normalizarCuit(request.cuitEmpresaSolicitud()),
                ESTADOS_PENDIENTES
        )) {
            throw new OperacionInvalidaException("Ya existe una solicitud de asociacion pendiente para esa empresa");
        }

        final String cuit = normalizarCuit(request.cuitEmpresaSolicitud());
        if (cuit == null || cuit.length() != 11) {
            throw new OperacionInvalidaException("El CUIT ingresado es inconsistente. Ingrese 11 digitos numericos sin guiones.");
        }

        final Empresa empresaExistente = empresaRepository.findByCuitEmpresaNormalizado(cuit)
                .or(() -> empresaRepository.findByCuitEmpresa(cuit))
                .orElse(null);
        final String razonSocial = empresaExistente != null
                ? empresaExistente.getRazonSocialEmpresa()
                : normalizarTextoObligatorio(request.razonSocialEmpresaSolicitud(), "razon social");
        final String mailEmpresa = empresaExistente != null
                ? empresaExistente.getMailEmpresa()
                : normalizarTextoObligatorio(request.mailEmpresaSolicitud(), "mail de la empresa");
        final String telefonoEmpresa = empresaExistente != null
                ? empresaExistente.getTelefonoEmpresa()
                : normalizarTextoObligatorio(request.telefonoEmpresaSolicitud(), "telefono de la empresa");

        if (empresaExistente != null && empresaExistente.getFechaBajaEmpresa() != null) {
            throw new OperacionInvalidaException("No podemos asociar una empresa dada de baja");
        }

        final EstadoSolicitud estadoEnviada = estadoSolicitudRepository.findByCodigoInterno("ENVIADA")
                .orElseThrow(() -> new OperacionInvalidaException("No se encontro el estado inicial de solicitud"));

        final Administrador administrador = administradorRepository.findFirstByFechaBajaAdministradorIsNullOrderByIdAsc()
                .orElse(null);

        final SolicitudAsociacion solicitud = SolicitudAsociacion.builder()
                .cuitEmpresaSolicitud(cuit)
                .razonSocialEmpresaSolicitud(razonSocial)
                .mailEmpresaSolicitud(mailEmpresa)
                .telefonoEmpresaSolicitud(telefonoEmpresa)
                .fechaEnvioSolicitud(Instant.now())
                .fechaResolucion(null)
                .estadoActual(estadoEnviada)
                .reclutador(reclutador)
                .administrador(administrador)
                .build();

        final SolicitudAsociacion guardada = solicitudAsociacionRepository.save(solicitud);

        return new SolicitudAsociacionResponse(
                guardada.getId(),
                guardada.getCuitEmpresaSolicitud(),
                guardada.getRazonSocialEmpresaSolicitud(),
                guardada.getMailEmpresaSolicitud(),
                guardada.getTelefonoEmpresaSolicitud(),
                guardada.getEstadoActual().getNombreEstadoSolicitud(),
                guardada.getFechaEnvioSolicitud()
        );
    }

    private String normalizarCuit(String cuit) {
        if (cuit == null) {
            return null;
        }
        final String cleaned = cuit.trim().replace("-", "");
        return cleaned.chars().allMatch(Character::isDigit) ? cleaned : null;
    }

    private String normalizarTextoObligatorio(String valor, String campo) {
        final String cleaned = valor == null ? null : valor.trim();
        if (cleaned == null || cleaned.isBlank()) {
            throw new OperacionInvalidaException("Debe informar " + campo + " de la empresa");
        }
        return cleaned;
    }
}
