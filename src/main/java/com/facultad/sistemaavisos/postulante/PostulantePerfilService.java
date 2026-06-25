package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.experiencia.dto.ExperienciaResponse;
import com.facultad.sistemaavisos.experienciaacademica.dto.ExperienciaAcademicaResponse;
import com.facultad.sistemaavisos.habilidad.dto.HabilidadResponse;
import com.facultad.sistemaavisos.postulante.dto.PostulantePerfilResponse;
import com.facultad.sistemaavisos.postulante.dto.PostulantePerfilUpdateRequest;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraResponse;
import com.facultad.sistemaavisos.security.JwtService;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import com.facultad.sistemaavisos.tipoestudiante.dto.TipoEstudianteOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostulantePerfilService {

    private final PostulanteRepository postulanteRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public PostulantePerfilResponse obtenerPerfil(Authentication authentication) {
        return mapPerfil(resolverPostulante(authentication));
    }

    @Transactional
    public PostulantePerfilResponse actualizarPerfil(Authentication authentication, PostulantePerfilUpdateRequest request) {
        final Postulante postulante = resolverPostulante(authentication);

        postulante.setNombrePostulante(request.nombrePostulante());
        postulante.setApellidoPostulante(request.apellidoPostulante());
        postulante.setLegajoAcademicoPostulante(request.legajoAcademicoPostulante());
        postulante.setMailAcademicoPostulante(request.mailAcademicoPostulante());

        return mapPerfil(postulanteRepository.save(postulante));
    }

    public Postulante resolverPostulante(Authentication authentication) {
        final String token = authentication != null && authentication.getCredentials() instanceof String credenciales
                ? credenciales
                : null;

        if (token == null) {
            throw new RecursoNoEncontradoException("No se pudo resolver la sesion del postulante");
        }

        final Long usuarioSeguridadId = jwtService.extraerSubjectId(token);

        return postulanteRepository.findByUsuarioSeguridadId(usuarioSeguridadId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el perfil del postulante"));
    }

    static PostulantePerfilResponse mapPerfil(Postulante postulante) {
        final var carreras = postulante.getPostulanteCarreras().stream()
                .filter(pc -> !pc.estaDadoDeBaja())
                .map(pc -> new PostulanteCarreraResponse(
                        pc.getId(),
                        pc.getCarrera().getId(),
                        pc.getCarrera().getNombreCarrera(),
                        pc.getFechaDesdePostulanteCarrera(),
                        pc.getFechaHastaPostulanteCarrera()
                ))
                .toList();

        final var experiencias = postulante.getExperiencias().stream()
                .filter(e -> !e.estaDadoDeBaja())
                .map(e -> new ExperienciaResponse(
                        e.getId(),
                        e.getDescripcionExperiencia(),
                        e.getFechaDesdeExp(),
                        e.getFechaHastaExp(),
                        e.getNombreCargoExperiencia(),
                        e.getNombreEmpresaExperiencia()
                ))
                .toList();

        final var experienciasAcademicas = postulante.getExperienciasAcademicas().stream()
                .filter(e -> !e.estaDadoDeBaja())
                .map(e -> new ExperienciaAcademicaResponse(
                        e.getId(),
                        e.getNombreInstitucionExpAcademica(),
                        e.getTituloExpAcademica(),
                        e.getFechaDesdeExpAcademica(),
                        e.getFechaHastaExpAcademica()
                ))
                .toList();

        final var habilidades = postulante.getHabilidades().stream()
                .filter(h -> !h.estaDadoDeBaja())
                .map(h -> new HabilidadResponse(h.getId(), h.getNombreHabilidad()))
                .toList();

        final var tipoEstudiante = postulante.getTipoEstudiante() == null
                ? null
                : new TipoEstudianteOptionResponse(
                        postulante.getTipoEstudiante().getId(),
                        postulante.getTipoEstudiante().getNombreTipoEstudiante()
                );

        return new PostulantePerfilResponse(
                postulante.getId(),
                postulante.getNombrePostulante(),
                postulante.getApellidoPostulante(),
                postulante.getFechaNacimientoPostulante(),
                postulante.getLegajoAcademicoPostulante(),
                postulante.getMailAcademicoPostulante(),
                postulante.getMailPersonalPostulante(),
                postulante.getUrlCVGuardado(),
                postulante.getCvNombreArchivo(),
                tipoEstudiante,
                carreras,
                experiencias,
                experienciasAcademicas,
                habilidades
        );
    }
}
