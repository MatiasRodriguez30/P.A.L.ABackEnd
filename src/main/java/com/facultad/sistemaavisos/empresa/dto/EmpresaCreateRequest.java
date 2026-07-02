package com.facultad.sistemaavisos.empresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmpresaCreateRequest(
        @NotBlank String cuitEmpresa,
        @NotBlank String razonSocialEmpresa,
        @NotBlank @Email String mailEmpresa,
        String telefonoEmpresa,
        String descripcionEmpresa,
        String direccionEmpresa
) {
}
