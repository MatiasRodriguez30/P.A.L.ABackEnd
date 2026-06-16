package com.facultad.sistemaavisos.repository;

import com.facultad.sistemaavisos.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
}