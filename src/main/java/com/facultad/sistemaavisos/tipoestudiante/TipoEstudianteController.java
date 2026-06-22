package com.facultad.sistemaavisos.tipoestudiante;

import com.facultad.sistemaavisos.tipoestudiante.dto.TipoEstudianteOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/soporte/tipos-estudiante")
@RequiredArgsConstructor
public class TipoEstudianteController {

    private final TipoEstudianteRepository tipoEstudianteRepository;

    @GetMapping
    public List<TipoEstudianteOptionResponse> listarActivos() {
        return tipoEstudianteRepository.findByFechaBajaTipoEstudianteIsNullOrderByIdAsc().stream()
                .map(tipoEstudiante -> new TipoEstudianteOptionResponse(
                        tipoEstudiante.getId(),
                        tipoEstudiante.getNombreTipoEstudiante()
                ))
                .toList();
    }
}
