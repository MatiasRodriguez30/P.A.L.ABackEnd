package com.facultad.sistemaavisos.carrera;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    Optional<Carrera> findByIdAndFechaBajaCarreraIsNull(Long id);

    List<Carrera> findByFechaBajaCarreraIsNullOrderByNombreCarreraAsc();

    Optional<Carrera> findByNombreCarrera(String nombreCarrera);
    List<Carrera> findByConexionCarrera_Id(Long conexionId);
    Optional<Carrera> findByConexionCarrera_IdAndClaveExternaCarrera(Long conexionId,String clave);
    long countByConexionCarrera_Id(Long conexionId);
    @EntityGraph(attributePaths = "conexionCarrera")
    List<Carrera> findAllByOrderByNombreCarreraAsc();
}
