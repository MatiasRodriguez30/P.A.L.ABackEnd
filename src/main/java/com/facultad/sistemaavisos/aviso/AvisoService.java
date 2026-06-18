package com.facultad.sistemaavisos.aviso;

import java.util.List;

public interface AvisoService {

    List<Aviso> listarTodos();

    Aviso buscarPorId(Integer nroAviso);

    Aviso guardar(Aviso aviso);

    Aviso actualizar(Integer nroAviso, Aviso aviso);

    void eliminar(Integer nroAviso);
}
