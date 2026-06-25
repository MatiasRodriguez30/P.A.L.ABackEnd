package com.facultad.sistemaavisos.shared.config;

import com.facultad.sistemaavisos.estadoaviso.EstadoAviso;
import com.facultad.sistemaavisos.estadoaviso.EstadoAvisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.List;

// Datos de demo para que /avisos-libre tenga algo real para mostrar sin
// depender del flujo completo de alta de avisos (reclutador + subsistema externo).
@Configuration
@RequiredArgsConstructor
public class AvisosDemoSeedInitializer {

    private static final String CODIGO_ESTADO_ABIERTO = "ABIERTO";

    private final AvisoDemoSeederService avisoDemoSeederService;
    private final EstadoAvisoRepository estadoAvisoRepository;

    @Bean
    @Order(2)
    ApplicationRunner seedAvisosDemo() {
        return args -> {
            final EstadoAviso abierto = estadoAvisoRepository.findByCodigoInterno(CODIGO_ESTADO_ABIERTO)
                    .orElseThrow(() -> new IllegalStateException(
                            "No se encontro el estado de aviso ABIERTO; CatalogoEstadosInitializer deberia correr antes."
                    ));

            avisoDemoSeederService.crearAvisoSiNoExiste(
                    "Desarrollador Full Stack Jr",
                    "Buscamos un desarrollador Full Stack junior con ganas de aprender React y Node.js. Trabajo en equipo con mentoria de devs senior.",
                    Duration.ofDays(45),
                    "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800&h=400&fit=crop",
                    new AvisoDemoSeederService.EmpresaSeed("20-11111111-1", "contacto@techdemo.com", "TechDemo S.A."),
                    new AvisoDemoSeederService.ReclutadorSeed("27-22222222-2", "lucia.fernandez@techdemo.com", "Lucia Fernandez"),
                    List.of("Ingenieria en Sistemas de Informacion", "Tecnicatura Universitaria en Programacion"),
                    List.of(
                            new AvisoDemoSeederService.TipoAvisoSeed("Modalidad de trabajo", List.of("Remoto", "Hibrido")),
                            new AvisoDemoSeederService.TipoAvisoSeed("Tipo de contrato", List.of("Pasantia"))
                    ),
                    abierto
            );

            avisoDemoSeederService.crearAvisoSiNoExiste(
                    "Analista de Datos",
                    "Oportunidad para analizar datos de uso de la plataforma PALA. Se valora conocimiento de SQL y Python.",
                    Duration.ofDays(60),
                    "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800&h=400&fit=crop",
                    new AvisoDemoSeederService.EmpresaSeed("20-33333333-3", "rrhh@datapala.com", "DataPala Argentina"),
                    new AvisoDemoSeederService.ReclutadorSeed("20-44444444-4", "martin.gomez@datapala.com", "Martin Gomez"),
                    List.of("Ingenieria en Sistemas de Informacion"),
                    List.of(
                            new AvisoDemoSeederService.TipoAvisoSeed("Modalidad de trabajo", List.of("Remoto")),
                            new AvisoDemoSeederService.TipoAvisoSeed("Experiencia requerida", List.of("Sin experiencia"))
                    ),
                    abierto
            );

            avisoDemoSeederService.crearAvisoSiNoExiste(
                    "Soporte Tecnico IT",
                    "Atencion a usuarios internos, mantenimiento de equipos e instalacion de software. Ideal para dar los primeros pasos en IT.",
                    Duration.ofDays(30),
                    "https://images.unsplash.com/photo-1581092160562-40aa08e78837?w=800&h=400&fit=crop",
                    new AvisoDemoSeederService.EmpresaSeed("20-55555555-5", "contacto@soportemendoza.com", "Soporte Mendoza"),
                    new AvisoDemoSeederService.ReclutadorSeed("27-66666666-6", "carla.diaz@soportemendoza.com", "Carla Diaz"),
                    List.of("Tecnicatura Universitaria en Programacion"),
                    List.of(
                            new AvisoDemoSeederService.TipoAvisoSeed("Modalidad de trabajo", List.of("Presencial")),
                            new AvisoDemoSeederService.TipoAvisoSeed("Tipo de contrato", List.of("Relacion de dependencia"))
                    ),
                    abierto
            );
        };
    }
}
