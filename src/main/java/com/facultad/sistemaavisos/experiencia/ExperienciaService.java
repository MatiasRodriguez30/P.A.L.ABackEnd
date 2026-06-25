package com.facultad.sistemaavisos.experiencia;

import com.facultad.sistemaavisos.experiencia.dto.ExperienciaRequest;
import com.facultad.sistemaavisos.experiencia.dto.ExperienciaResponse;
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
public class ExperienciaService {

    private final PostulantePerfilService postulantePerfilService;
    private final PostulanteRepository postulanteRepository;

    @Transactional
    public ExperienciaResponse agregar(Authentication authentication, ExperienciaRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        final Experiencia nueva = Experiencia.builder()
                .descripcionExperiencia(request.descripcionExperiencia())
                .fechaDesdeExp(request.fechaDesdeExp())
                .fechaHastaExp(request.fechaHastaExp())
                .nombreCargoExperiencia(request.nombreCargoExperiencia())
                .nombreEmpresaExperiencia(request.nombreEmpresaExperiencia())
                .build();

        postulante.getExperiencias().add(nueva);
        postulanteRepository.flush();

        return mapear(nueva);
    }

    @Transactional
    public ExperienciaResponse actualizar(Authentication authentication, Long id, ExperienciaRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final Experiencia experiencia = buscarActiva(postulante, id);

        experiencia.setDescripcionExperiencia(request.descripcionExperiencia());
        experiencia.setFechaDesdeExp(request.fechaDesdeExp());
        experiencia.setFechaHastaExp(request.fechaHastaExp());
        experiencia.setNombreCargoExperiencia(request.nombreCargoExperiencia());
        experiencia.setNombreEmpresaExperiencia(request.nombreEmpresaExperiencia());

        postulanteRepository.save(postulante);

        return mapear(experiencia);
    }

    @Transactional
    public void eliminar(Authentication authentication, Long id) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final Experiencia experiencia = buscarActiva(postulante, id);

        experiencia.darDeBaja();
        postulanteRepository.save(postulante);
    }

    private Experiencia buscarActiva(Postulante postulante, Long id) {
        return postulante.getExperiencias().stream()
                .filter(e -> !e.estaDadoDeBaja())
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la experiencia del postulante"));
    }

    private ExperienciaResponse mapear(Experiencia experiencia) {
        return new ExperienciaResponse(
                experiencia.getId(),
                experiencia.getDescripcionExperiencia(),
                experiencia.getFechaDesdeExp(),
                experiencia.getFechaHastaExp(),
                experiencia.getNombreCargoExperiencia(),
                experiencia.getNombreEmpresaExperiencia()
        );
    }
}
