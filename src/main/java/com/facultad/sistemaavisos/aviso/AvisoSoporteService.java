package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoFormularioSoporteResponse;

import java.util.List;

public interface AvisoSoporteService {

    AvisoFormularioSoporteResponse obtenerSoporteFormulario(Long reclutadorId);

    List<AvisoFormularioSoporteResponse.EmpresaActivaResponse> listarEmpresasActivasDelReclutador(Long reclutadorId);

    List<AvisoFormularioSoporteResponse.CarreraActivaResponse> listarCarrerasActivas();

    List<AvisoFormularioSoporteResponse.TipoAvisoActivoResponse> listarTiposAvisoActivos();
}
