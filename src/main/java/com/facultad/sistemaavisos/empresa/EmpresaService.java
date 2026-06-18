package com.facultad.sistemaavisos.empresa;

import java.util.List;

public interface EmpresaService {

    List<Empresa> listarTodas();

    Empresa buscarPorCuit(String cuitEmpresa);

    Empresa crear(Empresa empresa);

    Empresa actualizar(String cuitEmpresa, Empresa empresa);

    void eliminar(String cuitEmpresa);
}
