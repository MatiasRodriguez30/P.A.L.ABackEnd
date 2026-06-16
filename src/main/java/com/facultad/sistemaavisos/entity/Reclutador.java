package com.facultad.sistemaavisos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reclutadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclutador {

    @Id
    @Column(name = "cuil_reclutador", nullable = false, length = 20)
    private String cuilReclutador;

    @Column(name = "descripcion_reclutador")
    private String descripcionReclutador;

    @Column(name = "fecha_baja_reclutador")
    private LocalDateTime fechaBajaReclutador;

    @Column(name = "mail_reclutador", nullable = false, unique = true)
    private String mailReclutador;

    @Column(name = "nombre_reclutador", nullable = false)
    private String nombreReclutador;
}