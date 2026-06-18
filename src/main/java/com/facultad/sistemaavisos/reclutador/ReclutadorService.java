package com.facultad.sistemaavisos.reclutador;

import java.util.List;

public interface ReclutadorService {

    List<Reclutador> listarTodos();

    Reclutador buscarPorId(String cuilReclutador);

    Reclutador guardar(Reclutador reclutador);

    Reclutador actualizar(String cuilReclutador, Reclutador reclutador);

    void eliminar(String cuilReclutador);
}
