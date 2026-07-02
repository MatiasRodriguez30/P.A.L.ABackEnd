package com.facultad.sistemaavisos.solicitudasociacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface SolicitudAsociacionRepository extends JpaRepository<SolicitudAsociacion, Long> {

    Optional<SolicitudAsociacion> findTopByReclutador_IdAndFechaBajaSolicitudAsociacionIsNullOrderByFechaEnvioSolicitudDesc(
            Long reclutadorId
    );

    boolean existsByReclutador_IdAndCuitEmpresaSolicitudAndFechaBajaSolicitudAsociacionIsNullAndEstadoActual_CodigoInternoIn(
            Long reclutadorId,
            String cuitEmpresaSolicitud,
            Collection<String> estados
    );
}
