package com.facultad.sistemaavisos.carrera;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    Optional<Carrera> findByIdAndFechaBajaCarreraIsNull(Long id);

    List<Carrera> findByFechaBajaCarreraIsNullOrderByNombreCarreraAsc();

    Optional<Carrera> findByNombreCarrera(String nombreCarrera);
}
