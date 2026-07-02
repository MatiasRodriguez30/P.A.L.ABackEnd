package com.facultad.sistemaavisos.empresa;

import java.util.List;

public interface EmpresaService {

    List<Empresa> listarTodas();

    List<Empresa> listarActivas();

    Empresa buscarPorCuit(String cuitEmpresa);

    Empresa crear(Empresa empresa);

    Empresa actualizar(String cuitEmpresa, Empresa empresa);

    void eliminar(String cuitEmpresa);
}
