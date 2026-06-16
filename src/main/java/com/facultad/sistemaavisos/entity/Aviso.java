package com.facultad.sistemaavisos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "avisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_aviso")
    private Integer nroAviso;

    @Column(name = "descripcion_aviso")
    private String descripcionAviso;

    @Column(name = "fecha_cierre_aviso")
    private LocalDateTime fechaCierreAviso;

    @Column(name = "fecha_creacion_aviso")
    private LocalDateTime fechaCreacionAviso;

    @Column(name = "fecha_publicacion_aviso")
    private LocalDateTime fechaPublicacionAviso;

    @Column(name = "imagen_url_aviso")
    private String imagenUrlAviso;

    @Column(name = "nombre_aviso", nullable = false)
    private String nombreAviso;

    @ManyToOne
    @JoinColumn(name = "cuit_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cuil_reclutador", nullable = false)
    private Reclutador reclutador;

    @ManyToOne
    @JoinColumn(name = "cod_estado_aviso", nullable = false)
    private EstadoAviso estadoActual;
}