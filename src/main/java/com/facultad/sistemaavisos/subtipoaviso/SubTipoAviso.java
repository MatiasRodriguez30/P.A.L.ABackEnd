package com.facultad.sistemaavisos.subtipoaviso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "subtipos_aviso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubTipoAviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_sub_tipo_aviso")
    private Long id;

    @Column(name = "fecha_alta_sub_tipo_aviso")
    private Instant fechaAltaSubTipoAviso;

    @Column(name = "fecha_baja_sub_tipo_aviso")
    private Instant fechaBajaSubTipoAviso;

    @Column(name = "nombre_sub_tipo_aviso", nullable = false)
    private String nombreSubTipoAviso;
}
