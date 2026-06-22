package com.facultad.sistemaavisos.tipoestudiante;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoEstudianteRepository extends JpaRepository<TipoEstudiante, Long> {

    Optional<TipoEstudiante> findByIdAndFechaBajaTipoEstudianteIsNull(Long id);

    List<TipoEstudiante> findByFechaBajaTipoEstudianteIsNullOrderByIdAsc();

    Optional<TipoEstudiante> findByNombreTipoEstudianteIgnoreCaseAndFechaBajaTipoEstudianteIsNull(
            String nombreTipoEstudiante
    );
}
