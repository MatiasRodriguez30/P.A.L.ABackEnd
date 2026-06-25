package com.facultad.sistemaavisos.experienciaacademica;

import com.facultad.sistemaavisos.experienciaacademica.dto.ExperienciaAcademicaRequest;
import com.facultad.sistemaavisos.experienciaacademica.dto.ExperienciaAcademicaResponse;
import com.facultad.sistemaavisos.postulante.Postulante;
import com.facultad.sistemaavisos.postulante.PostulantePerfilService;
import com.facultad.sistemaavisos.postulante.PostulanteRepository;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExperienciaAcademicaService {

    private final PostulantePerfilService postulantePerfilService;
    private final PostulanteRepository postulanteRepository;

    @Transactional
    public ExperienciaAcademicaResponse agregar(Authentication authentication, ExperienciaAcademicaRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        final ExperienciaAcademica nueva = ExperienciaAcademica.builder()
                .nombreInstitucionExpAcademica(request.nombreInstitucionExpAcademica())
                .tituloExpAcademica(request.tituloExpAcademica())
                .fechaDesdeExpAcademica(request.fechaDesdeExpAcademica())
                .fechaHastaExpAcademica(request.fechaHastaExpAcademica())
                .build();

        postulante.getExperienciasAcademicas().add(nueva);
        postulanteRepository.flush();

        return mapear(nueva);
    }

    @Transactional
    public ExperienciaAcademicaResponse actualizar(Authentication authentication, Long id, ExperienciaAcademicaRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final ExperienciaAcademica experiencia = buscarActiva(postulante, id);

        experiencia.setNombreInstitucionExpAcademica(request.nombreInstitucionExpAcademica());
        experiencia.setTituloExpAcademica(request.tituloExpAcademica());
        experiencia.setFechaDesdeExpAcademica(request.fechaDesdeExpAcademica());
        experiencia.setFechaHastaExpAcademica(request.fechaHastaExpAcademica());

        postulanteRepository.save(postulante);

        return mapear(experiencia);
    }

    @Transactional
    public void eliminar(Authentication authentication, Long id) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final ExperienciaAcademica experiencia = buscarActiva(postulante, id);

        experiencia.darDeBaja();
        postulanteRepository.save(postulante);
    }

    private ExperienciaAcademica buscarActiva(Postulante postulante, Long id) {
        return postulante.getExperienciasAcademicas().stream()
                .filter(e -> !e.estaDadoDeBaja())
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la experiencia academica del postulante"));
    }

    private ExperienciaAcademicaResponse mapear(ExperienciaAcademica experiencia) {
        return new ExperienciaAcademicaResponse(
                experiencia.getId(),
                experiencia.getNombreInstitucionExpAcademica(),
                experiencia.getTituloExpAcademica(),
                experiencia.getFechaDesdeExpAcademica(),
                experiencia.getFechaHastaExpAcademica()
        );
    }
}
