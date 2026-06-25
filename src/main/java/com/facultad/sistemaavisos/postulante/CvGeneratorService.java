package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.experiencia.Experiencia;
import com.facultad.sistemaavisos.experienciaacademica.ExperienciaAcademica;
import com.facultad.sistemaavisos.habilidad.Habilidad;
import com.facultad.sistemaavisos.postulantecarrera.PostulanteCarrera;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Genera un PDF simple con los datos del perfil del Postulante usando PDFBox.
// No usa plantillas ni layout fijo en archivo: el texto se escribe linea por linea
// con paginacion manual (ver PdfCursor) porque PDFBox no calcula esto solo.
@Service
public class CvGeneratorService {

    private static final float MARGIN = 50f;
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float LINE_HEIGHT = 16f;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("MM/yyyy");

    public byte[] generar(Postulante postulante) {
        try (PDDocument document = new PDDocument()) {
            final PdfCursor cursor = new PdfCursor(document);

            cursor.titulo(postulante.getNombrePostulante() + " " + postulante.getApellidoPostulante());
            cursor.subtitulo(datosContacto(postulante));

            cursor.seccion("Carreras");
            final List<PostulanteCarrera> carreras = postulante.getPostulanteCarreras().stream()
                    .filter(pc -> !pc.estaDadoDeBaja())
                    .toList();
            if (carreras.isEmpty()) {
                cursor.texto("Sin carreras registradas.");
            } else {
                for (PostulanteCarrera carrera : carreras) {
                    cursor.texto("- " + carrera.getCarrera().getNombreCarrera() + " ("
                            + formatearRango(carrera.getFechaDesdePostulanteCarrera(), carrera.getFechaHastaPostulanteCarrera()) + ")");
                }
            }

            cursor.seccion("Experiencia laboral");
            final List<Experiencia> experiencias = postulante.getExperiencias().stream()
                    .filter(e -> !e.estaDadoDeBaja())
                    .toList();
            if (experiencias.isEmpty()) {
                cursor.texto("Sin experiencia laboral registrada.");
            } else {
                for (Experiencia experiencia : experiencias) {
                    cursor.texto(experiencia.getNombreCargoExperiencia() + " - " + experiencia.getNombreEmpresaExperiencia()
                            + " (" + formatearRango(experiencia.getFechaDesdeExp(), experiencia.getFechaHastaExp()) + ")");
                    if (experiencia.getDescripcionExperiencia() != null && !experiencia.getDescripcionExperiencia().isBlank()) {
                        cursor.textoSangrado(experiencia.getDescripcionExperiencia());
                    }
                }
            }

            cursor.seccion("Experiencia academica");
            final List<ExperienciaAcademica> experienciasAcademicas = postulante.getExperienciasAcademicas().stream()
                    .filter(e -> !e.estaDadoDeBaja())
                    .toList();
            if (experienciasAcademicas.isEmpty()) {
                cursor.texto("Sin experiencia academica registrada.");
            } else {
                for (ExperienciaAcademica experiencia : experienciasAcademicas) {
                    cursor.texto(experiencia.getTituloExpAcademica() + " - " + experiencia.getNombreInstitucionExpAcademica()
                            + " (" + formatearRango(experiencia.getFechaDesdeExpAcademica(), experiencia.getFechaHastaExpAcademica()) + ")");
                }
            }

            cursor.seccion("Habilidades");
            final List<Habilidad> habilidades = postulante.getHabilidades().stream()
                    .filter(h -> !h.estaDadoDeBaja())
                    .toList();
            if (habilidades.isEmpty()) {
                cursor.texto("Sin habilidades registradas.");
            } else {
                cursor.texto(habilidades.stream().map(Habilidad::getNombreHabilidad).reduce((a, b) -> a + ", " + b).orElse(""));
            }

            cursor.cerrar();

            final ByteArrayOutputStream salida = new ByteArrayOutputStream();
            document.save(salida);
            return salida.toByteArray();
        } catch (IOException e) {
            throw new OperacionInvalidaException("No se pudo generar el PDF del CV");
        }
    }

    private String datosContacto(Postulante postulante) {
        final StringBuilder builder = new StringBuilder();
        builder.append(postulante.getMailPersonalPostulante());
        if (postulante.getMailAcademicoPostulante() != null && !postulante.getMailAcademicoPostulante().isBlank()) {
            builder.append(" | ").append(postulante.getMailAcademicoPostulante());
        }
        builder.append(" | Legajo: ").append(postulante.getLegajoAcademicoPostulante());
        if (postulante.getTipoEstudiante() != null) {
            builder.append(" | ").append(postulante.getTipoEstudiante().getNombreTipoEstudiante());
        }
        return builder.toString();
    }

    private String formatearRango(LocalDate desde, LocalDate hasta) {
        final String desdeTexto = desde == null ? "?" : desde.format(FORMATO_FECHA);
        final String hastaTexto = hasta == null ? "Actualidad" : hasta.format(FORMATO_FECHA);
        return desdeTexto + " - " + hastaTexto;
    }

    private static final class PdfCursor {
        private final PDDocument document;
        private final PDFont fontTitulo = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        private final PDFont fontSeccion = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        private final PDFont fontTexto = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        private PDPageContentStream content;
        private float y;

        PdfCursor(PDDocument document) throws IOException {
            this.document = document;
            nuevaPagina();
        }

        void titulo(String texto) throws IOException {
            escribir(texto, fontTitulo, 18);
            y -= 6;
        }

        void subtitulo(String texto) throws IOException {
            escribir(texto, fontTexto, 10);
            y -= 14;
        }

        void seccion(String texto) throws IOException {
            y -= 8;
            escribir(texto.toUpperCase(), fontSeccion, 12);
            y -= 2;
        }

        void texto(String texto) throws IOException {
            escribir(texto, fontTexto, 11);
        }

        void textoSangrado(String texto) throws IOException {
            escribir("   " + texto, fontTexto, 10);
        }

        private void escribir(String texto, PDFont font, float size) throws IOException {
            for (String linea : partirEnLineas(texto, 95)) {
                if (y < MARGIN + LINE_HEIGHT) {
                    nuevaPagina();
                }
                content.beginText();
                content.setFont(font, size);
                content.newLineAtOffset(MARGIN, y);
                content.showText(linea);
                content.endText();
                y -= LINE_HEIGHT;
            }
        }

        private List<String> partirEnLineas(String texto, int maxCaracteres) {
            if (texto.length() <= maxCaracteres) {
                return List.of(texto);
            }

            final List<String> lineas = new ArrayList<>();
            final StringBuilder actual = new StringBuilder();
            for (String palabra : texto.split(" ")) {
                if (actual.length() + palabra.length() + 1 > maxCaracteres) {
                    lineas.add(actual.toString());
                    actual.setLength(0);
                }
                if (!actual.isEmpty()) {
                    actual.append(" ");
                }
                actual.append(palabra);
            }
            if (!actual.isEmpty()) {
                lineas.add(actual.toString());
            }
            return lineas;
        }

        private void nuevaPagina() throws IOException {
            if (content != null) {
                content.close();
            }

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = PAGE_HEIGHT - MARGIN;
        }

        void cerrar() throws IOException {
            if (content != null) {
                content.close();
            }
        }
    }
}
