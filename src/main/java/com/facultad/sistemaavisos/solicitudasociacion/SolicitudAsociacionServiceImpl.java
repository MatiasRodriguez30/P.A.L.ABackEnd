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
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionDetalleResponse;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionGestionRequest;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudEstadoHistorialResponse;
import com.facultad.sistemaavisos.solicitudestado.SolicitudEstado;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Comparator;

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

        final SolicitudAsociacion solicitud = SolicitudAsociacion.builder()
                .cuitEmpresaSolicitud(cuit)
                .razonSocialEmpresaSolicitud(razonSocial)
                .mailEmpresaSolicitud(mailEmpresa)
                .telefonoEmpresaSolicitud(telefonoEmpresa)
                .empresaExistenteAlSolicitar(empresaExistente != null)
                .fechaEnvioSolicitud(Instant.now())
                .fechaResolucion(null)
                .estadoActual(estadoEnviada)
                .reclutador(reclutador)
                .administrador(null)
                .build();

        solicitud.getSolicitudEstados().add(SolicitudEstado.builder()
                .solicitudAsociacion(solicitud)
                .estadoSolicitud(estadoEnviada)
                .fechaInicioVigenciaSolicitudEstado(solicitud.getFechaEnvioSolicitud())
                .build());

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

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudAsociacionDetalleResponse> listar() {
        return solicitudAsociacionRepository
                .findAllByFechaBajaSolicitudAsociacionIsNullOrderByFechaEnvioSolicitudDesc()
                .stream().map(this::toDetalleSinHistorial).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudAsociacionDetalleResponse obtenerDetalle(Long id) {
        return toDetalle(buscarDetalle(id));
    }

    @Override
    public SolicitudAsociacionDetalleResponse tomar(
            Long id, String bearerToken, SolicitudAsociacionGestionRequest request) {
        final SolicitudAsociacion solicitud = buscarDetalle(id);
        validarEstado(solicitud, "ENVIADA", "Solo se puede tomar una solicitud enviada");
        solicitud.setAdministrador(buscarAdministrador(bearerToken));
        aplicarObservacion(solicitud, request);
        cambiarEstado(solicitud, "EN_EVALUACION", false);
        return toDetalle(solicitudAsociacionRepository.save(solicitud));
    }

    @Override
    public SolicitudAsociacionDetalleResponse aceptar(
            Long id, String bearerToken, SolicitudAsociacionGestionRequest request) {
        return resolver(id, bearerToken, request, "ACEPTADA");
    }

    @Override
    public SolicitudAsociacionDetalleResponse rechazar(
            Long id, String bearerToken, SolicitudAsociacionGestionRequest request) {
        return resolver(id, bearerToken, request, "RECHAZADA");
    }

    private SolicitudAsociacionDetalleResponse resolver(
            Long id, String bearerToken, SolicitudAsociacionGestionRequest request, String estado) {
        final SolicitudAsociacion solicitud = buscarDetalle(id);
        validarEstado(solicitud, "EN_EVALUACION", "Solo se puede resolver una solicitud en evaluacion");
        solicitud.setAdministrador(buscarAdministrador(bearerToken));
        aplicarObservacion(solicitud, request);
        cambiarEstado(solicitud, estado, true);
        return toDetalle(solicitudAsociacionRepository.save(solicitud));
    }

    private void cambiarEstado(SolicitudAsociacion solicitud, String codigo, boolean resuelta) {
        final Instant ahora = Instant.now();
        solicitud.getSolicitudEstados().stream()
                .filter(item -> item.getFechaFinVigenciaSolicitud() == null)
                .forEach(item -> item.setFechaFinVigenciaSolicitud(ahora));

        final EstadoSolicitud nuevoEstado = estadoSolicitudRepository.findByCodigoInterno(codigo)
                .orElseThrow(() -> new OperacionInvalidaException("No se encontro el estado " + codigo));
        solicitud.setEstadoActual(nuevoEstado);
        solicitud.getSolicitudEstados().add(SolicitudEstado.builder()
                .solicitudAsociacion(solicitud)
                .estadoSolicitud(nuevoEstado)
                .fechaInicioVigenciaSolicitudEstado(ahora)
                .build());
        if (resuelta) solicitud.setFechaResolucion(ahora);
    }

    private Administrador buscarAdministrador(String bearerToken) {
        if (bearerToken == null) throw new OperacionInvalidaException("No se encontro una sesion autenticada");
        final Long usuarioId = jwtService.extraerSubjectId(bearerToken);
        return administradorRepository.findByUsuarioSeguridadId(usuarioId)
                .filter(admin -> admin.getFechaBajaAdministrador() == null)
                .orElseThrow(() -> new OperacionInvalidaException("Necesitas un perfil de administrador activo"));
    }

    private SolicitudAsociacion buscarDetalle(Long id) {
        return solicitudAsociacionRepository.findWithDetailsById(id)
                .filter(item -> item.getFechaBajaSolicitudAsociacion() == null)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud de asociacion no encontrada"));
    }

    private void validarEstado(SolicitudAsociacion solicitud, String esperado, String mensaje) {
        if (!esperado.equals(solicitud.getEstadoActual().getCodigoInterno())) {
            throw new OperacionInvalidaException(mensaje);
        }
    }

    private void aplicarObservacion(SolicitudAsociacion solicitud, SolicitudAsociacionGestionRequest request) {
        if (request != null && request.observacionesInternas() != null) {
            final String observacion = request.observacionesInternas().trim();
            solicitud.setObservacionesInternas(observacion.isBlank() ? null : observacion);
        }
    }

    private SolicitudAsociacionDetalleResponse toDetalleSinHistorial(SolicitudAsociacion solicitud) {
        return toDetalle(solicitud, List.of());
    }

    private SolicitudAsociacionDetalleResponse toDetalle(SolicitudAsociacion solicitud) {
        final List<SolicitudEstadoHistorialResponse> historial = solicitud.getSolicitudEstados().stream()
                .sorted(Comparator.comparing(SolicitudEstado::getFechaInicioVigenciaSolicitudEstado))
                .map(item -> new SolicitudEstadoHistorialResponse(
                        item.getEstadoSolicitud().getCodigoInterno(),
                        item.getEstadoSolicitud().getNombreEstadoSolicitud(),
                        item.getFechaInicioVigenciaSolicitudEstado(),
                        item.getFechaFinVigenciaSolicitud()))
                .toList();
        return toDetalle(solicitud, historial);
    }

    private SolicitudAsociacionDetalleResponse toDetalle(
            SolicitudAsociacion solicitud, List<SolicitudEstadoHistorialResponse> historial) {
        final Reclutador reclutador = solicitud.getReclutador();
        final Administrador admin = solicitud.getAdministrador();
        return new SolicitudAsociacionDetalleResponse(
                solicitud.getId(), solicitud.getFechaEnvioSolicitud(), solicitud.getFechaResolucion(),
                solicitud.getEstadoActual().getCodigoInterno(),
                solicitud.getEstadoActual().getNombreEstadoSolicitud(),
                new SolicitudAsociacionDetalleResponse.ReclutadorDetalle(
                        reclutador.getId(), reclutador.getNombreReclutador(), reclutador.getMailReclutador(),
                        reclutador.getCuilReclutador(), reclutador.getDescripcionReclutador()),
                new SolicitudAsociacionDetalleResponse.EmpresaDetalle(
                        solicitud.getRazonSocialEmpresaSolicitud(), solicitud.getCuitEmpresaSolicitud(),
                        solicitud.getMailEmpresaSolicitud(), solicitud.getTelefonoEmpresaSolicitud(),
                        Boolean.TRUE.equals(solicitud.getEmpresaExistenteAlSolicitar())),
                solicitud.getObservacionesInternas(),
                admin == null || solicitud.getFechaResolucion() == null ? null : new SolicitudAsociacionDetalleResponse.AdministradorDetalle(
                        admin.getId(), admin.getNombreAdministrador(), admin.getApellidoAdministrador(),
                        admin.getMailAdministrador()),
                historial);
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
