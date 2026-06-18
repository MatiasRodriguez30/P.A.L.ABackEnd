package com.facultad.sistemaavisos.carrera;

import java.util.List;

public interface CarreraService {

    List<Carrera> listarTodos();

    Carrera buscarPorId(Integer codCarrera);

    Carrera guardar(Carrera carrera);

    Carrera actualizar(Integer codCarrera, Carrera carrera);

    void eliminar(Integer codCarrera);
}
