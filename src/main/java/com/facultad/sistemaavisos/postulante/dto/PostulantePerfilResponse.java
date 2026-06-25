package com.facultad.sistemaavisos.postulante.dto;

import com.facultad.sistemaavisos.experiencia.dto.ExperienciaResponse;
import com.facultad.sistemaavisos.experienciaacademica.dto.ExperienciaAcademicaResponse;
import com.facultad.sistemaavisos.habilidad.dto.HabilidadResponse;
import com.facultad.sistemaavisos.postulantecarrera.dto.PostulanteCarreraResponse;
import com.facultad.sistemaavisos.tipoestudiante.dto.TipoEstudianteOptionResponse;

import java.time.LocalDate;
import java.util.List;

public record PostulantePerfilResponse(
        Long id,
        String nombrePostulante,
        String apellidoPostulante,
        LocalDate fechaNacimientoPostulante,
        Long legajoAcademicoPostulante,
        String mailAcademicoPostulante,
        String mailPersonalPostulante,
        String urlCVGuardado,
        String cvNombreArchivo,
        TipoEstudianteOptionResponse tipoEstudiante,
        List<PostulanteCarreraResponse> carreras,
        List<ExperienciaResponse> experiencias,
        List<ExperienciaAcademicaResponse> experienciasAcademicas,
        List<HabilidadResponse> habilidades
) {
}
