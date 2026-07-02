package com.facultad.sistemaavisos.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCuitEmpresa(String cuitEmpresa);

    @Query("""
            select e
            from Empresa e
            where replace(replace(e.cuitEmpresa, '-', ''), ' ', '') = :cuit
            """)
    Optional<Empresa> findByCuitEmpresaNormalizado(@Param("cuit") String cuit);

    List<Empresa> findByFechaBajaEmpresaIsNullOrderByRazonSocialEmpresaAsc();
}
