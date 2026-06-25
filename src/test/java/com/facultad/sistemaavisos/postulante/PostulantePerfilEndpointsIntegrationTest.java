package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.administrador.Administrador;
import com.facultad.sistemaavisos.administrador.AdministradorRepository;
import com.facultad.sistemaavisos.carrera.Carrera;
import com.facultad.sistemaavisos.carrera.CarreraRepository;
import com.facultad.sistemaavisos.tipoestudiante.TipoEstudiante;
import com.facultad.sistemaavisos.tipoestudiante.TipoEstudianteRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "security.subsystem.jwt.secret=test-secret-pala-2026-test-secret-pala-2026"
})
class PostulantePerfilEndpointsIntegrationTest {

    private static final String TEST_SECRET = "test-secret-pala-2026-test-secret-pala-2026";
    private static final long USUARIO_SEGURIDAD_ID_POSTULANTE = 9001L;
    private static final long USUARIO_SEGURIDAD_ID_ADMINISTRADOR = 9002L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostulanteRepository postulanteRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private TipoEstudianteRepository tipoEstudianteRepository;

    @Test
    void ejecutaFlujoCompletoDePerfilDePostulanteConCarrerasYCv() throws Exception {
        final TipoEstudiante tipoEstudiante = tipoEstudianteRepository.save(
                TipoEstudiante.builder().nombreTipoEstudiante("Estudiante").build()
        );

        final Carrera ingenieria = carreraRepository.save(
                Carrera.builder().nombreCarrera("Ingenieria en Sistemas").build()
        );
        carreraRepository.save(Carrera.builder().nombreCarrera("Tecnicatura en Programacion").build());

        postulanteRepository.save(Postulante.builder()
                .usuarioSeguridadId(USUARIO_SEGURIDAD_ID_POSTULANTE)
                .nombrePostulante("Mica")
                .apellidoPostulante("Test")
                .fechaNacimientoPostulante(LocalDate.of(2000, 1, 1))
                .legajoAcademicoPostulante(12345L)
                .mailPersonalPostulante("postulante.test@pala.com")
                .tipoEstudiante(tipoEstudiante)
                .build());

        final String bearer = bearerPostulante();

        mockMvc.perform(get("/api/postulante/me").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePostulante").value("Mica"))
                .andExpect(jsonPath("$.tipoEstudiante.nombre").value("Estudiante"))
                .andExpect(jsonPath("$.carreras.length()").value(0));

        mockMvc.perform(patch("/api/postulante/me")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombrePostulante": "Micaela",
                                  "apellidoPostulante": "Testing",
                                  "legajoAcademicoPostulante": 12345,
                                  "mailAcademicoPostulante": "micaela.academico@pala.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePostulante").value("Micaela"))
                .andExpect(jsonPath("$.mailAcademicoPostulante").value("micaela.academico@pala.com"));

        mockMvc.perform(get("/api/carreras").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.nombreCarrera == 'Ingenieria en Sistemas')]").exists())
                .andExpect(jsonPath("$[?(@.nombreCarrera == 'Tecnicatura en Programacion')]").exists());

        final String carreraCreada = mockMvc.perform(post("/api/postulante/me/carreras")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "carreraId": %d,
                                  "fechaDesdePostulanteCarrera": "2022-03-01",
                                  "fechaHastaPostulanteCarrera": null
                                }
                                """.formatted(ingenieria.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCarrera").value("Ingenieria en Sistemas"))
                .andReturn().getResponse().getContentAsString();

        final long postulanteCarreraId = objectMapper.readTree(carreraCreada).path("id").asLong();

        mockMvc.perform(post("/api/postulante/me/carreras")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "carreraId": %d, "fechaDesdePostulanteCarrera": "2023-01-01" }
                                """.formatted(ingenieria.getId())))
                .andExpect(status().isConflict());

        mockMvc.perform(delete("/api/postulante/me/carreras/{id}", postulanteCarreraId)
                        .header("Authorization", bearer))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/postulante/me").header("Authorization", bearer))
                .andExpect(jsonPath("$.carreras.length()").value(0));

        mockMvc.perform(post("/api/postulante/me/carreras")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "carreraId": %d, "fechaDesdePostulanteCarrera": "2024-01-01" }
                                """.formatted(ingenieria.getId())))
                .andExpect(status().isOk());

        final String experienciaCreada = mockMvc.perform(post("/api/postulante/me/experiencias")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descripcionExperiencia": "Desarrollo backend",
                                  "fechaDesdeExp": "2021-01-01",
                                  "fechaHastaExp": null,
                                  "nombreCargoExperiencia": "Dev Jr",
                                  "nombreEmpresaExperiencia": "TechDemo"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final long experienciaId = objectMapper.readTree(experienciaCreada).path("id").asLong();

        mockMvc.perform(post("/api/postulante/me/experiencias-academicas")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreInstitucionExpAcademica": "UTN",
                                  "tituloExpAcademica": "Ingenieria en Sistemas",
                                  "fechaDesdeExpAcademica": "2019-03-01",
                                  "fechaHastaExpAcademica": null
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/postulante/me/habilidades")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombreHabilidad\": \"React\" }"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/postulante/me").header("Authorization", bearer))
                .andExpect(jsonPath("$.carreras.length()").value(1))
                .andExpect(jsonPath("$.experiencias.length()").value(1))
                .andExpect(jsonPath("$.experienciasAcademicas.length()").value(1))
                .andExpect(jsonPath("$.habilidades.length()").value(1))
                .andExpect(jsonPath("$.urlCVGuardado").doesNotExist());

        mockMvc.perform(delete("/api/postulante/me/experiencias/{id}", experienciaId)
                        .header("Authorization", bearer))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/postulante/me").header("Authorization", bearer))
                .andExpect(jsonPath("$.experiencias.length()").value(0));

        mockMvc.perform(post("/api/postulante/me/cv/generar").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.urlCVGuardado").value("/api/postulante/me/cv"));

        final byte[] pdfGenerado = mockMvc.perform(get("/api/postulante/me/cv").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentType()).startsWith("application/pdf"))
                .andReturn().getResponse().getContentAsByteArray();
        assertThat(pdfGenerado).isNotEmpty();
        assertThat(new String(pdfGenerado, 0, 5, StandardCharsets.US_ASCII)).isEqualTo("%PDF-");

        final MockMultipartFile archivoSubido = new MockMultipartFile(
                "archivo", "mi-cv.pdf", "application/pdf", "%PDF-1.4 contenido de prueba".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/postulante/me/cv").file(archivoSubido).header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cvNombreArchivo").value("mi-cv.pdf"));

        final byte[] pdfSubido = mockMvc.perform(get("/api/postulante/me/cv").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        assertThat(new String(pdfSubido, StandardCharsets.UTF_8)).contains("contenido de prueba");
    }

    @Test
    void rechazaAccederAlPerfilDePostulanteSinElPermisoCorrespondiente() throws Exception {
        final SecretKey secretKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
        final Date ahora = new Date();
        final String tokenSinPermiso = Jwts.builder()
                .subject(String.valueOf(USUARIO_SEGURIDAD_ID_POSTULANTE))
                .claim("mail", "sin-permiso@pala.com")
                .claim("roles", List.of("Postulante"))
                .claim("permisos", List.of())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + 60_000))
                .signWith(secretKey)
                .compact();

        mockMvc.perform(get("/api/postulante/me").header("Authorization", "Bearer " + tokenSinPermiso))
                .andExpect(status().isForbidden());
    }

    @Test
    void ejecutaFlujoDePerfilDeAdministrador() throws Exception {
        administradorRepository.save(Administrador.builder()
                .usuarioSeguridadId(USUARIO_SEGURIDAD_ID_ADMINISTRADOR)
                .nombreAdministrador("Admin")
                .apellidoAdministrador("Original")
                .legajoAdministrador(555L)
                .mailAdministrador("admin.test@pala.com")
                .build());

        final String bearer = bearerAdministrador();

        mockMvc.perform(get("/api/administrador/me").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreAdministrador").value("Admin"))
                .andExpect(jsonPath("$.legajoAdministrador").value(555))
                .andExpect(jsonPath("$.mailAdministrador").value("admin.test@pala.com"));

        final String respuestaActualizada = mockMvc.perform(patch("/api/administrador/me")
                        .header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombreAdministrador\": \"Administrador\", \"apellidoAdministrador\": \"Actualizado\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreAdministrador").value("Administrador"))
                .andExpect(jsonPath("$.apellidoAdministrador").value("Actualizado"))
                .andReturn().getResponse().getContentAsString();

        final JsonNode actualizado = objectMapper.readTree(respuestaActualizada);
        assertThat(actualizado.path("legajoAdministrador").asLong()).isEqualTo(555L);
        assertThat(actualizado.path("mailAdministrador").asText()).isEqualTo("admin.test@pala.com");
    }

    private String bearerPostulante() {
        return bearerCon(USUARIO_SEGURIDAD_ID_POSTULANTE, "postulante.test@pala.com", "Postulante", "VER_PERFIL_POSTULANTE");
    }

    private String bearerAdministrador() {
        return bearerCon(USUARIO_SEGURIDAD_ID_ADMINISTRADOR, "admin.test@pala.com", "Administrador", "VER_PERFIL_ADMINISTRADOR");
    }

    private String bearerCon(long usuarioSeguridadId, String mail, String rol, String permiso) {
        final SecretKey secretKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
        final Date ahora = new Date();
        final Date expiracion = new Date(ahora.getTime() + 60_000);

        final String token = Jwts.builder()
                .subject(String.valueOf(usuarioSeguridadId))
                .claim("mail", mail)
                .claim("roles", List.of(rol))
                .claim("permisos", List.of(permiso))
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(secretKey)
                .compact();

        return "Bearer " + token;
    }
}
