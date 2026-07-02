package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoFormularioSoporteResponse;
import com.facultad.sistemaavisos.carrera.CarreraRepository;
import com.facultad.sistemaavisos.reclutador.ReclutadorRepository;
import com.facultad.sistemaavisos.reclutadorempresa.ReclutadorEmpresaRepository;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import com.facultad.sistemaavisos.tipoaviso.TipoAvisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvisoSoporteServiceImpl implements AvisoSoporteService {

    private final ReclutadorRepository reclutadorRepository;
    private final ReclutadorEmpresaRepository reclutadorEmpresaRepository;
    private final CarreraRepository carreraRepository;
    private final TipoAvisoRepository tipoAvisoRepository;

    @Override
    public AvisoFormularioSoporteResponse obtenerSoporteFormulario(Long reclutadorId) {
        return new AvisoFormularioSoporteResponse(
                listarEmpresasActivasDelReclutador(reclutadorId),
                listarCarrerasActivas(),
                listarTiposAvisoActivos()
        );
    }

    @Override
    public List<AvisoFormularioSoporteResponse.EmpresaActivaResponse> listarEmpresasActivasDelReclutador(Long reclutadorId) {
        validarReclutadorActivo(reclutadorId);

        return List.copyOf(reclutadorEmpresaRepository.findByReclutador_IdAndFechaFinReclutadorEmpresaIsNullAndEmpresa_FechaBajaEmpresaIsNull(reclutadorId).stream()
                .map(reclutadorEmpresa -> reclutadorEmpresa.getEmpresa())
                .filter(empresa -> empresa != null && empresa.getFechaBajaEmpresa() == null)
                .sorted(Comparator.comparing(empresa -> empresa.getRazonSocialEmpresa(), String.CASE_INSENSITIVE_ORDER))
                .collect(LinkedHashMap<Long, AvisoFormularioSoporteResponse.EmpresaActivaResponse>::new,
                        (map, empresa) -> map.putIfAbsent(
                                empresa.getId(),
                                new AvisoFormularioSoporteResponse.EmpresaActivaResponse(
                                        empresa.getId(),
                                        empresa.getCuitEmpresa(),
                                        empresa.getRazonSocialEmpresa()
                                )
                        ),
                        LinkedHashMap::putAll)
                .values());
    }

    @Override
    public List<AvisoFormularioSoporteResponse.CarreraActivaResponse> listarCarrerasActivas() {
        return carreraRepository.findByFechaBajaCarreraIsNullOrderByNombreCarreraAsc().stream()
                .map(carrera -> new AvisoFormularioSoporteResponse.CarreraActivaResponse(
                        carrera.getId(),
                        carrera.getNombreCarrera(),
                        carrera.getDescripcionCarrera()
                ))
                .toList();
    }

    @Override
    public List<AvisoFormularioSoporteResponse.TipoAvisoActivoResponse> listarTiposAvisoActivos() {
        return tipoAvisoRepository.findByFechaBajaTipoAvisoIsNullOrderByNombreTipoAvisoAsc().stream()
                .map(tipoAviso -> new AvisoFormularioSoporteResponse.TipoAvisoActivoResponse(
                        tipoAviso.getId(),
                        tipoAviso.getNombreTipoAviso(),
                        tipoAviso.getDescripcionTipoAviso(),
                        tipoAviso.getSubTipoAvisos().stream()
                                .filter(subTipoAviso -> subTipoAviso.getFechaBajaSubTipoAviso() == null)
                                .sorted(Comparator.comparing(
                                        subTipoAviso -> subTipoAviso.getNombreSubTipoAviso(),
                                        String.CASE_INSENSITIVE_ORDER
                                ))
                                .map(subTipoAviso -> new AvisoFormularioSoporteResponse.SubTipoAvisoActivoResponse(
                                        subTipoAviso.getId(),
                                        subTipoAviso.getNombreSubTipoAviso()
                                ))
                                .toList()
                ))
                .toList();
    }

    private void validarReclutadorActivo(Long reclutadorId) {
        reclutadorRepository.findByIdAndFechaBajaReclutadorIsNull(reclutadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el reclutador activo con id: " + reclutadorId
                ));
    }
}
