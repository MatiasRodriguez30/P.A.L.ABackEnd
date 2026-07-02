package com.facultad.sistemaavisos.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select e
            from Empresa e
            where replace(replace(e.cuitEmpresa, '-', ''), ' ', '') = :cuit
            """)
    Optional<Empresa> findByCuitEmpresaNormalizadoForUpdate(@Param("cuit") String cuit);

    List<Empresa> findByFechaBajaEmpresaIsNullOrderByRazonSocialEmpresaAsc();

    List<Empresa> findAllByOrderByRazonSocialEmpresaAsc();

    boolean existsByMailEmpresaIgnoreCaseAndIdNot(String mailEmpresa, Long id);

    boolean existsByMailEmpresaIgnoreCase(String mailEmpresa);
}
