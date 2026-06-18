package com.facultad.sistemaavisos.empresa;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_empresa")
    private Long id;

    @Column(name = "cuit_empresa", nullable = false, unique = true, length = 20)
    private String cuitEmpresa;

    @Column(name = "descripcion_empresa")
    private String descripcionEmpresa;

    @Column(name = "direccion_empresa")
    private String direccionEmpresa;

    @Column(name = "fecha_alta_empresa")
    private Instant fechaAltaEmpresa;

    @Column(name = "fecha_baja_empresa")
    private Instant fechaBajaEmpresa;

    @Column(name = "mail_empresa", nullable = false, unique = true)
    private String mailEmpresa;

    @Column(name = "razon_social_empresa", nullable = false)
    private String razonSocialEmpresa;

    @Column(name = "telefono_empresa")
    private String telefonoEmpresa;
}
