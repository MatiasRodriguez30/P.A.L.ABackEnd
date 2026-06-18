package com.facultad.sistemaavisos.postulacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostulacionRepository extends JpaRepository<Postulacion, Integer> {

    List<Postulacion> findByPostulante_LegajoAcademicoPostulante(Integer legajoAcademicoPostulante);

    List<Postulacion> findByAviso_NroAviso(Integer nroAviso);

    Optional<Postulacion> findByPostulante_LegajoAcademicoPostulanteAndAviso_NroAviso(
            Integer legajoAcademicoPostulante,
            Integer nroAviso
    );
}
