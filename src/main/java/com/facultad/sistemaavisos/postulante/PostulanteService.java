package com.facultad.sistemaavisos.postulante;

import java.util.List;

public interface PostulanteService {

    List<Postulante> listarTodos();

    Postulante buscarPorId(Long postulanteId);

    Postulante guardar(Postulante postulante);

    Postulante actualizar(Long postulanteId, Postulante postulante);

    void eliminar(Long postulanteId);
}
