package com.facultad.sistemaavisos.reclutadorempresa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReclutadorEmpresaRepository extends JpaRepository<ReclutadorEmpresa, Long> {

    Optional<ReclutadorEmpresa> findByReclutador_IdAndEmpresa_IdAndFechaFinReclutadorEmpresaIsNull(
            Long reclutadorId,
            Long empresaId
    );

    @EntityGraph(attributePaths = "empresa")
    List<ReclutadorEmpresa> findByReclutador_IdAndFechaFinReclutadorEmpresaIsNull(Long reclutadorId);

    @EntityGraph(attributePaths = "empresa")
    List<ReclutadorEmpresa> findByReclutador_IdAndFechaFinReclutadorEmpresaIsNullAndEmpresa_FechaBajaEmpresaIsNull(
            Long reclutadorId
    );

    Optional<ReclutadorEmpresa> findByReclutador_IdAndEmpresa_IdAndFechaFinReclutadorEmpresaIsNullAndEmpresa_FechaBajaEmpresaIsNull(
            Long reclutadorId,
            Long empresaId
    );

    boolean existsByReclutador_IdAndFechaFinReclutadorEmpresaIsNull(Long reclutadorId);

    boolean existsByReclutador_IdAndEmpresa_IdAndFechaFinReclutadorEmpresaIsNull(
            Long reclutadorId,
            Long empresaId
    );
}
