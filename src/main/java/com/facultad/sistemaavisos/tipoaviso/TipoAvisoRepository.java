package com.facultad.sistemaavisos.tipoaviso;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoAvisoRepository extends JpaRepository<TipoAviso, Long> {

    @EntityGraph(attributePaths = "subTipoAvisos")
    Optional<TipoAviso> findByIdAndFechaBajaTipoAvisoIsNull(Long id);

    @EntityGraph(attributePaths = "subTipoAvisos")
    List<TipoAviso> findByFechaBajaTipoAvisoIsNullOrderByNombreTipoAvisoAsc();
}
