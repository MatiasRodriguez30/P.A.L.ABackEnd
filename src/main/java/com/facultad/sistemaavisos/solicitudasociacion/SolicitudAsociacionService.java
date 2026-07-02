package com.facultad.sistemaavisos.solicitudasociacion;

import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionCreateRequest;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionResponse;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionDetalleResponse;
import com.facultad.sistemaavisos.solicitudasociacion.dto.SolicitudAsociacionGestionRequest;

import java.util.List;

public interface SolicitudAsociacionService {

    SolicitudAsociacionResponse crearSolicitud(String bearerToken, String mailUsuario, SolicitudAsociacionCreateRequest request);

    List<SolicitudAsociacionDetalleResponse> listar();

    SolicitudAsociacionDetalleResponse obtenerDetalle(Long id);

    SolicitudAsociacionDetalleResponse tomar(Long id, String bearerToken, SolicitudAsociacionGestionRequest request);

    SolicitudAsociacionDetalleResponse aceptar(Long id, String bearerToken, SolicitudAsociacionGestionRequest request);

    SolicitudAsociacionDetalleResponse rechazar(Long id, String bearerToken, SolicitudAsociacionGestionRequest request);
}
