package com.facultad.sistemaavisos.postulacion;

import java.util.List;

public interface PostulacionService {

    List<Postulacion> listarTodos();

    Postulacion buscarPorId(Long postulacionId);

    Postulacion guardar(Postulacion postulacion);

    Postulacion actualizar(Long postulacionId, Postulacion postulacion);

    void eliminar(Long postulacionId);
}
