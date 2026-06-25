package com.facultad.sistemaavisos.carrera;

import com.facultad.sistemaavisos.carrera.dto.CarreraOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraRepository carreraRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
    public List<CarreraOptionResponse> listarActivas() {
        return carreraRepository.findByFechaBajaCarreraIsNullOrderByNombreCarreraAsc().stream()
                .map(carrera -> new CarreraOptionResponse(carrera.getId(), carrera.getNombreCarrera()))
                .toList();
    }
}
