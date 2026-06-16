package com.facultad.sistemaavisos.repository;

import com.facultad.sistemaavisos.entity.Postulante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulanteRepository extends JpaRepository<Postulante, Integer> {
}