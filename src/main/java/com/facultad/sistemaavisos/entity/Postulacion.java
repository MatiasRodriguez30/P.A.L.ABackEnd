package com.facultad.sistemaavisos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "postulaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_postulacion")
    private Integer nroPostulacion;

    @Column(name = "descripcion_postulacion")
    private String descripcionPostulacion;

    @Column(name = "fecha_postulacion")
    private LocalDateTime fechaPostulacion;

    @ManyToOne
    @JoinColumn(name = "legajo_academico_postulante", nullable = false)
    private Postulante postulante;

    @ManyToOne
    @JoinColumn(name = "nro_aviso", nullable = false)
    private Aviso aviso;

    @ManyToOne
    @JoinColumn(name = "cod_estado_postulacion", nullable = false)
    private EstadoPostulacion estadoActual;
}