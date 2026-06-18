package com.facultad.sistemaavisos.carrera;

import java.util.List;

public interface CarreraService {

    List<Carrera> listarTodos();

    Carrera buscarPorId(Long carreraId);

    Carrera guardar(Carrera carrera);

    Carrera actualizar(Long carreraId, Carrera carrera);

    void eliminar(Long carreraId);
}
