package com.facultad.sistemaavisos.postulacion;

import java.util.List;

public interface PostulacionService {

    List<Postulacion> listarTodos();

    Postulacion buscarPorId(Integer nroPostulacion);

    Postulacion guardar(Postulacion postulacion);

    Postulacion actualizar(Integer nroPostulacion, Postulacion postulacion);

    void eliminar(Integer nroPostulacion);
}
