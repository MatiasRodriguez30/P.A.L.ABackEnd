package com.facultad.sistemaavisos.empresa;

import java.util.List;
import com.facultad.sistemaavisos.empresa.dto.EmpresaCreateRequest;

public interface EmpresaService {

    List<Empresa> listarTodas();

    List<Empresa> listarActivas();

    Empresa buscarPorCuit(String cuitEmpresa);

    Empresa crear(Empresa empresa);

    Empresa crearDesdeRequest(EmpresaCreateRequest request);

    Empresa buscarPorId(Long id);

    Empresa actualizarDesdeRequest(Long id, EmpresaCreateRequest request);

    Empresa darDeBaja(Long id);

    Empresa reactivar(Long id);

    Empresa actualizar(String cuitEmpresa, Empresa empresa);

    void eliminar(String cuitEmpresa);
}
