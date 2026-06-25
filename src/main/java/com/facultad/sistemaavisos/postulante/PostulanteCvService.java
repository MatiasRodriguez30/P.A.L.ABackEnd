package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.postulante.dto.CvResponse;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostulanteCvService {

    public static final String CV_DOWNLOAD_PATH = "/api/postulante/me/cv";

    private final PostulantePerfilService postulantePerfilService;
    private final PostulanteRepository postulanteRepository;
    private final CvGeneratorService cvGeneratorService;

    @Transactional
    public CvResponse subir(Authentication authentication, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new OperacionInvalidaException("Debe seleccionar un archivo PDF para subir");
        }

        if (!"application/pdf".equalsIgnoreCase(archivo.getContentType())) {
            throw new OperacionInvalidaException("El CV debe ser un archivo PDF");
        }

        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        try {
            postulante.setCvArchivo(archivo.getBytes());
        } catch (IOException e) {
            throw new OperacionInvalidaException("No se pudo leer el archivo PDF subido");
        }

        postulante.setCvNombreArchivo(archivo.getOriginalFilename());
        postulante.setUrlCVGuardado(CV_DOWNLOAD_PATH);
        postulanteRepository.save(postulante);

        return new CvResponse(postulante.getUrlCVGuardado(), postulante.getCvNombreArchivo());
    }

    @Transactional
    public CvResponse generar(Authentication authentication) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        final byte[] pdf = cvGeneratorService.generar(postulante);
        final String nombreArchivo = "cv_" + postulante.getApellidoPostulante() + "_" + postulante.getNombrePostulante() + ".pdf";

        postulante.setCvArchivo(pdf);
        postulante.setCvNombreArchivo(nombreArchivo);
        postulante.setUrlCVGuardado(CV_DOWNLOAD_PATH);
        postulanteRepository.save(postulante);

        return new CvResponse(postulante.getUrlCVGuardado(), postulante.getCvNombreArchivo());
    }

    @Transactional(readOnly = true)
    public byte[] obtenerArchivo(Authentication authentication) {
        final Postulante postulante = postulantePerfilService.resolverPostulante(authentication);

        if (postulante.getCvArchivo() == null) {
            throw new RecursoNoEncontradoException("El postulante no tiene un CV guardado");
        }

        return postulante.getCvArchivo();
    }
}
