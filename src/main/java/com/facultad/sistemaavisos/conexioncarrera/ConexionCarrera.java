package com.facultad.sistemaavisos.conexioncarrera;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="conexiones_carrera") @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ConexionCarrera {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="nro_conexion_carrera") private Long id;
 @Column(nullable=false) private String nombreConexionCarrera;
 @Column(nullable=false,length=1000,unique=true) private String urlConexionCarrera;
 private Instant fechaCreacionConexionCarrera; private Instant fechaUltimaSincronizacion; private Instant fechaBajaConexionCarrera;
}
