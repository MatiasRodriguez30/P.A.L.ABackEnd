package com.facultad.sistemaavisos.administrador.dto;

public record AdministradorPerfilResponse(
        Long id,
        String nombreAdministrador,
        String apellidoAdministrador,
        Long legajoAdministrador,
        String mailAdministrador
) {
}
