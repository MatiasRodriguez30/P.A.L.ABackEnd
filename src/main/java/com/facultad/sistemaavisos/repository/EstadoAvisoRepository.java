package com.facultad.sistemaavisos.repository;

import com.facultad.sistemaavisos.entity.EstadoAviso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoAvisoRepository extends JpaRepository<EstadoAviso, Integer> {

    Optional<EstadoAviso> findByNombreEstadoAviso(String nombreEstadoAviso);
}