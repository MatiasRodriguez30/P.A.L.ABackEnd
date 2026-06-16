package com.facultad.sistemaavisos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @Column(name = "cuit_empresa", nullable = false, length = 20)
    private String cuitEmpresa;

    @Column(name = "descripcion_empresa")
    private String descripcionEmpresa;

    @Column(name = "direccion_empresa")
    private String direccionEmpresa;

    @Column(name = "fecha_alta_empresa")
    private LocalDateTime fechaAltaEmpresa;

    @Column(name = "fecha_baja_empresa")
    private LocalDateTime fechaBajaEmpresa;

    @Column(name = "mail_empresa", nullable = false, unique = true)
    private String mailEmpresa;

    @Column(name = "nombre_empresa", nullable = false)
    private String nombreEmpresa;

    @Column(name = "nro_empresa")
    private Integer nroEmpresa;
}