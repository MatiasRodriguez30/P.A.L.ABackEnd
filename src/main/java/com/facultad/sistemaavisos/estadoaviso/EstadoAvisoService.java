package com.facultad.sistemaavisos.estadoaviso;

import java.util.List;

public interface EstadoAvisoService {

    List<EstadoAviso> listarTodos();

    EstadoAviso buscarPorId(Integer codEstadoAviso);

    EstadoAviso guardar(EstadoAviso estadoAviso);

    EstadoAviso actualizar(Integer codEstadoAviso, EstadoAviso estadoAviso);

    void eliminar(Integer codEstadoAviso);
}
