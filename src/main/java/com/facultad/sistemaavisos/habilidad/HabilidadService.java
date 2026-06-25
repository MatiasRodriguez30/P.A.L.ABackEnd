package com.facultad.sistemaavisos.habilidad;

import com.facultad.sistemaavisos.habilidad.dto.HabilidadRequest;
import com.facultad.sistemaavisos.habilidad.dto.HabilidadResponse;
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
public class HabilidadService {

    private final PostulantePerfilService postulantePerfilService;
    private final PostulanteRepository postulanteRepository;

    @Transactional
    public HabilidadResponse agregar(Authentication authentication, HabilidadRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        final Habilidad nueva = Habilidad.builder()
                .nombreHabilidad(request.nombreHabilidad())
                .build();

        postulante.getHabilidades().add(nueva);
        postulanteRepository.flush();

        return mapear(nueva);
    }

    @Transactional
    public HabilidadResponse actualizar(Authentication authentication, Long id, HabilidadRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final Habilidad habilidad = buscarActiva(postulante, id);

        habilidad.setNombreHabilidad(request.nombreHabilidad());
        postulanteRepository.save(postulante);

        return mapear(habilidad);
    }

    @Transactional
    public void eliminar(Authentication authentication, Long id) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final Habilidad habilidad = buscarActiva(postulante, id);

        habilidad.darDeBaja();
        postulanteRepository.save(postulante);
    }

    private Habilidad buscarActiva(Postulante postulante, Long id) {
        return postulante.getHabilidades().stream()
                .filter(h -> !h.estaDadoDeBaja())
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la habilidad del postulante"));
    }

    private HabilidadResponse mapear(Habilidad habilidad) {
        return new HabilidadResponse(habilidad.getId(), habilidad.getNombreHabilidad());
    }
}
