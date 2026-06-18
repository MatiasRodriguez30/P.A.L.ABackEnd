package com.facultad.sistemaavisos.aviso;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {

    @EntityGraph(attributePaths = {
            "empresa",
            "reclutador",
            "estadoActual",
            "avisosCarrera",
            "avisosCarrera.carrera",
            "avisosTipoAvisos",
            "avisosTipoAvisos.tipoAviso",
            "avisosTipoAvisos.avisosTipoAvisosSubTiposAvisos",
            "avisosTipoAvisos.avisosTipoAvisosSubTiposAvisos.subTipoAviso"
    })
    List<Aviso> findDistinctByEstadoActual_CodigoInternoAndFechaBajaAvisoIsNullAndEstadoActual_FechaBajaEstadoAvisoIsNull(
            String codigoInterno
    );

    @EntityGraph(attributePaths = {
            "empresa",
            "reclutador",
            "estadoActual",
            "avisosCarrera",
            "avisosCarrera.carrera",
            "avisosTipoAvisos",
            "avisosTipoAvisos.tipoAviso",
            "avisosTipoAvisos.avisosTipoAvisosSubTiposAvisos",
            "avisosTipoAvisos.avisosTipoAvisosSubTiposAvisos.subTipoAviso"
    })
    Optional<Aviso> findByIdAndEstadoActual_CodigoInternoAndFechaBajaAvisoIsNullAndEstadoActual_FechaBajaEstadoAvisoIsNull(
            Long id,
            String codigoInterno
    );

    Optional<Aviso> findByIdAndFechaBajaAvisoIsNull(Long id);

    List<Aviso> findByEmpresa_CuitEmpresaAndFechaBajaAvisoIsNull(String cuitEmpresa);

    List<Aviso> findByReclutador_CuilReclutadorAndFechaBajaAvisoIsNull(String cuilReclutador);
}
