package com.facultad.sistemaavisos.estadopostulacion;

import java.util.List;

public interface EstadoPostulacionService {

    List<EstadoPostulacion> listarTodos();

    EstadoPostulacion buscarPorId(Long estadoPostulacionId);

    EstadoPostulacion guardar(EstadoPostulacion estadoPostulacion);

    EstadoPostulacion actualizar(Long estadoPostulacionId, EstadoPostulacion estadoPostulacion);

    void eliminar(Long estadoPostulacionId);
}
