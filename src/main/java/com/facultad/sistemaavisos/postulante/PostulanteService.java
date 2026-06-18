package com.facultad.sistemaavisos.postulante;

import java.util.List;

public interface PostulanteService {

    List<Postulante> listarTodos();

    Postulante buscarPorId(Integer legajoAcademicoPostulante);

    Postulante guardar(Postulante postulante);

    Postulante actualizar(Integer legajoAcademicoPostulante, Postulante postulante);

    void eliminar(Integer legajoAcademicoPostulante);
}
