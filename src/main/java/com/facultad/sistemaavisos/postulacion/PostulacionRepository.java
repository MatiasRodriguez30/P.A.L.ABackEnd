package com.facultad.sistemaavisos.postulacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    List<Postulacion> findByPostulante_Id(Long postulanteId);

    List<Postulacion> findByAviso_Id(Long avisoId);

    Optional<Postulacion> findByPostulante_IdAndAviso_Id(
            Long postulanteId,
            Long avisoId
    );
}
