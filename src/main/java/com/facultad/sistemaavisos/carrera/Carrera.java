package com.facultad.sistemaavisos.carrera;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrera {

    private static final Set<String> CONECTORES = Set.of("de", "del", "en", "y", "e", "la", "las", "los");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_carrera")
    private Long id;

    @Column(name = "descripcion_carrera", length = 4000)
    private String descripcionCarrera;

    @Column(name = "fecha_actualizacion_carrera")
    private Instant fechaActualizacionCarrera;

    @Column(name = "fecha_alta_carrera")
    private Instant fechaAltaCarrera;

    @Column(name = "fecha_baja_carrera")
    private Instant fechaBajaCarrera;

    @Column(name = "nombre_carrera", nullable = false)
    private String nombreCarrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_conexion_carrera")
    private com.facultad.sistemaavisos.conexioncarrera.ConexionCarrera conexionCarrera;

    @Column(name = "clave_externa_carrera")
    private String claveExternaCarrera;

    @Column(name = "url_origen_carrera", length = 1000)
    private String urlOrigenCarrera;

    public void setNombreCarrera(String nombreCarrera) {
        if (nombreCarrera == null) {
            this.nombreCarrera = null;
            return;
        }
        final String[] palabras = nombreCarrera.trim().toLowerCase(new Locale("es", "AR")).split("\\s+");
        for (int i = 0; i < palabras.length; i++) {
            if (i > 0 && CONECTORES.contains(palabras[i])) continue;
            palabras[i] = palabras[i].substring(0, 1).toUpperCase(new Locale("es", "AR")) + palabras[i].substring(1);
        }
        this.nombreCarrera = String.join(" ", palabras);
    }
}
