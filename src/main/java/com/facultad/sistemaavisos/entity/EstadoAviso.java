package com.facultad.sistemaavisos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "estados_aviso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoAviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_estado_aviso")
    private Integer codEstadoAviso;

    @Column(name = "fecha_alta_estado_aviso")
    private LocalDateTime fechaAltaEstadoAviso;

    @Column(name = "fecha_baja_estado_aviso")
    private LocalDateTime fechaBajaEstadoAviso;

    @Column(name = "nombre_estado_aviso", nullable = false, unique = true)
    private String nombreEstadoAviso;
}