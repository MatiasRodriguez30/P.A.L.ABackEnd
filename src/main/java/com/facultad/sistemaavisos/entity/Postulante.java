package com.facultad.sistemaavisos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "postulantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulante {

    @Id
    @Column(name = "legajo_academico_postulante", nullable = false)
    private Integer legajoAcademicoPostulante;

    @Column(name = "apellido_postulante", nullable = false)
    private String apellidoPostulante;

    @Column(name = "fecha_baja_postulante")
    private LocalDateTime fechaBajaPostulante;

    @Column(name = "fecha_nacimiento_postulante")
    private LocalDateTime fechaNacimientoPostulante;

    @Column(name = "mail_academico_postulante", nullable = false, unique = true)
    private String mailAcademicoPostulante;

    @Column(name = "mail_personal_postulante")
    private String mailPersonalPostulante;

    @Column(name = "nombre_postulante", nullable = false)
    private String nombrePostulante;
}