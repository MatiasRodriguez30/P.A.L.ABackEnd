package com.facultad.sistemaavisos.aviso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Integer> {

    List<Aviso> findByEmpresa_CuitEmpresa(String cuitEmpresa);

    List<Aviso> findByReclutador_CuilReclutador(String cuilReclutador);
}
