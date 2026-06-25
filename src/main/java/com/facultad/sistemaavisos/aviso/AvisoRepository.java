package com.facultad.sistemaavisos.aviso;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {

    // Nota: avisosCarrera y avisosTipoAvisos son ambas colecciones List ("bag") de Aviso.
    // Hibernate no permite hacer JOIN FETCH de dos bags a la vez en la misma consulta
    // (MultipleBagFetchException), asi que solo una de las dos va en el EntityGraph;
    // la otra se carga lazy dentro de la misma transaccion (@Transactional readOnly del service).
    @EntityGraph(attributePaths = {
            "empresa",
            "reclutador",
            "estadoActual",
            "avisosCarrera",
            "avisosCarrera.carrera"
    })
    List<Aviso> findDistinctByEstadoActual_CodigoInternoAndFechaBajaAvisoIsNullAndEstadoActual_FechaBajaEstadoAvisoIsNull(
            String codigoInterno
    );

    @EntityGraph(attributePaths = {
            "empresa",
            "reclutador",
            "estadoActual",
            "avisosCarrera",
            "avisosCarrera.carrera"
    })
    Optional<Aviso> findByIdAndEstadoActual_CodigoInternoAndFechaBajaAvisoIsNullAndEstadoActual_FechaBajaEstadoAvisoIsNull(
            Long id,
            String codigoInterno
    );

    Optional<Aviso> findByIdAndFechaBajaAvisoIsNull(Long id);

    Optional<Aviso> findByNombreAvisoAndFechaBajaAvisoIsNull(String nombreAviso);

    List<Aviso> findByEmpresa_CuitEmpresaAndFechaBajaAvisoIsNull(String cuitEmpresa);

    List<Aviso> findByReclutador_CuilReclutadorAndFechaBajaAvisoIsNull(String cuilReclutador);
}
