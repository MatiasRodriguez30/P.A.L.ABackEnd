package com.facultad.sistemaavisos.estadoaviso;

import java.util.List;

public interface EstadoAvisoService {

    List<EstadoAviso> listarTodos();

    EstadoAviso buscarPorId(Long estadoAvisoId);

    EstadoAviso guardar(EstadoAviso estadoAviso);

    EstadoAviso actualizar(Long estadoAvisoId, EstadoAviso estadoAviso);

    void eliminar(Long estadoAvisoId);
}
