package com.facultad.sistemaavisos.service;

import com.facultad.sistemaavisos.entity.Empresa;

import java.util.List;

public interface EmpresaService {

    List<Empresa> listarTodas();

    Empresa buscarPorCuit(String cuitEmpresa);

    Empresa crear(Empresa empresa);

    Empresa actualizar(String cuitEmpresa, Empresa empresa);

    void eliminar(String cuitEmpresa);
}