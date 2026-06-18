package com.facultad.sistemaavisos.shared.exception;

public class EntidadDadaDeBajaException extends DomainException {

    public EntidadDadaDeBajaException(String entidad, Long id) {
        super("La entidad " + entidad + " con id " + id + " ya se encuentra dada de baja");
    }
}
