package com.facultad.sistemaavisos.estadopostulacion;

import java.util.List;

public interface EstadoPostulacionService {

    List<EstadoPostulacion> listarTodos();

    EstadoPostulacion buscarPorId(Integer codEstadoPostulacion);

    EstadoPostulacion guardar(EstadoPostulacion estadoPostulacion);

    EstadoPostulacion actualizar(Integer codEstadoPostulacion, EstadoPostulacion estadoPostulacion);

    void eliminar(Integer codEstadoPostulacion);
}
