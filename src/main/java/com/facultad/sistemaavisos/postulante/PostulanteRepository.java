package com.facultad.sistemaavisos.postulante;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostulanteRepository extends JpaRepository<Postulante, Long> {

    Optional<Postulante> findByIdAndFechaBajaPostulanteIsNull(Long id);
}
