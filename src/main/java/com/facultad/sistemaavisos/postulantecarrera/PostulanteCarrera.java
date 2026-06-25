package com.facultad.sistemaavisos.postulantecarrera;

import com.facultad.sistemaavisos.carrera.Carrera;
import com.facultad.sistemaavisos.postulante.Postulante;
import com.facultad.sistemaavisos.shared.exception.EntidadDadaDeBajaException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "postulantes_carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulanteCarrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_postulante_carrera")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_postulante", nullable = false)
    private Postulante postulante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_carrera", nullable = false)
    private Carrera carrera;

    @Column(name = "fecha_desde_postulante_carrera")
    private LocalDate fechaDesdePostulanteCarrera;

    @Column(name = "fecha_hasta_postulante_carrera")
    private LocalDate fechaHastaPostulanteCarrera;

    @Column(name = "fecha_baja_postulante_carrera")
    private Instant fechaBajaPostulanteCarrera;

    public boolean estaDadoDeBaja() {
        return fechaBajaPostulanteCarrera != null;
    }

    public void darDeBaja() {
        if (estaDadoDeBaja()) {
            throw new EntidadDadaDeBajaException("PostulanteCarrera", id);
        }

        this.fechaBajaPostulanteCarrera = Instant.now();
    }
}
