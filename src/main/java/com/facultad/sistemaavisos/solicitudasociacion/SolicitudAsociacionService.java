package com.facultad.sistemaavisos.solicitudasociacion;

import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionCreateRequest;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionResponse;

public interface SolicitudAsociacionService {

    SolicitudAsociacionResponse crearSolicitud(String bearerToken, String mailUsuario, SolicitudAsociacionCreateRequest request);
}
