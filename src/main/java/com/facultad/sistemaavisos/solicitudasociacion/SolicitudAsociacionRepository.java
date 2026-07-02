package com.facultad.sistemaavisos.solicitudasociacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

public interface SolicitudAsociacionRepository extends JpaRepository<SolicitudAsociacion, Long> {

    @EntityGraph(attributePaths = {"estadoActual", "reclutador", "administrador"})
    List<SolicitudAsociacion> findAllByFechaBajaSolicitudAsociacionIsNullOrderByFechaEnvioSolicitudDesc();

    @EntityGraph(attributePaths = {"estadoActual", "reclutador", "administrador", "solicitudEstados", "solicitudEstados.estadoSolicitud"})
    Optional<SolicitudAsociacion> findWithDetailsById(Long id);

    Optional<SolicitudAsociacion> findTopByReclutador_IdAndFechaBajaSolicitudAsociacionIsNullOrderByFechaEnvioSolicitudDesc(
            Long reclutadorId
    );

    boolean existsByReclutador_IdAndCuitEmpresaSolicitudAndFechaBajaSolicitudAsociacionIsNullAndEstadoActual_CodigoInternoIn(
            Long reclutadorId,
            String cuitEmpresaSolicitud,
            Collection<String> estados
    );
}
