package com.facultad.sistemaavisos.empresa.dto;

import com.facultad.sistemaavisos.empresa.Empresa;
import java.time.Instant;

public record EmpresaResponse(
        Long id,
        String cuitEmpresa,
        String razonSocialEmpresa,
        String mailEmpresa,
        String telefonoEmpresa,
        String descripcionEmpresa,
        String direccionEmpresa,
        Instant fechaAltaEmpresa,
        Instant fechaBajaEmpresa
) {
    public static EmpresaResponse from(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getId(), empresa.getCuitEmpresa(), empresa.getRazonSocialEmpresa(),
                empresa.getMailEmpresa(), empresa.getTelefonoEmpresa(), empresa.getDescripcionEmpresa(),
                empresa.getDireccionEmpresa(), empresa.getFechaAltaEmpresa(), empresa.getFechaBajaEmpresa());
    }
}
