package com.facultad.sistemaavisos.postulantecarrera;

import com.facultad.sistemaavisos.carrera.Carrera;
import com.facultad.sistemaavisos.carrera.CarreraRepository;
import com.facultad.sistemaavisos.postulante.Postulante;
import com.facultad.sistemaavisos.postulante.PostulantePerfilService;
import com.facultad.sistemaavisos.postulante.PostulanteRepository;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraCreateRequest;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraResponse;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraUpdateRequest;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostulanteCarreraService {

    private final PostulantePerfilService postulantePerfilService;
    private final PostulanteRepository postulanteRepository;
    private final CarreraRepository carreraRepository;

    @Transactional
    public PostulanteCarreraResponse agregar(Authentication authentication, PostulanteCarreraCreateRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        final Carrera carrera = carreraRepository.findByIdAndFechaBajaCarreraIsNull(request.carreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la carrera solicitada"));

        final boolean yaAsignada = postulante.getPostulanteCarreras().stream()
                .filter(pc -> !pc.estaDadoDeBaja())
                .anyMatch(pc -> pc.getCarrera().getId().equals(carrera.getId()));

        if (yaAsignada) {
            throw new OperacionInvalidaException("El postulante ya tiene esta carrera asignada");
        }

        final PostulanteCarrera nueva = PostulanteCarrera.builder()
                .postulante(postulante)
                .carrera(carrera)
                .fechaDesdePostulanteCarrera(request.fechaDesdePostulanteCarrera())
                .fechaHastaPostulanteCarrera(request.fechaHastaPostulanteCarrera())
                .build();

        postulante.getPostulanteCarreras().add(nueva);
        postulanteRepository.flush();

        return mapear(nueva);
    }

    @Transactional
    public PostulanteCarreraResponse actualizar(Authentication authentication, Long id, PostulanteCarreraUpdateRequest request) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final PostulanteCarrera postulanteCarrera = buscarActiva(postulante, id);

        postulanteCarrera.setFechaDesdePostulanteCarrera(request.fechaDesdePostulanteCarrera());
        postulanteCarrera.setFechaHastaPostulanteCarrera(request.fechaHastaPostulanteCarrera());

        postulanteRepository.save(postulante);

        return mapear(postulanteCarrera);
    }

    @Transactional
    public void eliminar(Authentication authentication, Long id) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);
        final PostulanteCarrera postulanteCarrera = buscarActiva(postulante, id);

        postulanteCarrera.darDeBaja();
        postulanteRepository.save(postulante);
    }

    private PostulanteCarrera buscarActiva(Postulante postulante, Long id) {
        return postulante.getPostulanteCarreras().stream()
                .filter(pc -> !pc.estaDadoDeBaja())
                .filter(pc -> pc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro la carrera del postulante"));
    }

    private PostulanteCarreraResponse mapear(PostulanteCarrera postulanteCarrera) {
        return new PostulanteCarreraResponse(
                postulanteCarrera.getId(),
                postulanteCarrera.getCarrera().getId(),
                postulanteCarrera.getCarrera().getNombreCarrera(),
                postulanteCarrera.getFechaDesdePostulanteCarrera(),
                postulanteCarrera.getFechaHastaPostulanteCarrera()
        );
    }
}
