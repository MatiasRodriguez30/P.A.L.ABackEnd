package com.facultad.sistemaavisos.subtipoaviso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubTipoAvisoRepository extends JpaRepository<SubTipoAviso, Long> {

    Optional<SubTipoAviso> findByIdAndFechaBajaSubTipoAvisoIsNull(Long id);

    Optional<SubTipoAviso> findByNombreSubTipoAviso(String nombreSubTipoAviso);
}
