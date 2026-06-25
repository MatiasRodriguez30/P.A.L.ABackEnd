package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.postulante.dto.CvResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/postulante/me/cv")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VER_PERFIL_POSTULANTE')")
public class PostulanteCvController {

    private final PostulanteCvService postulanteCvService;

    @PostMapping
    public CvResponse subir(Authentication authentication, @RequestParam("archivo") MultipartFile archivo) {
        return postulanteCvService.subir(authentication, archivo);
    }

    @PostMapping("/generar")
    public CvResponse generar(Authentication authentication) {
        return postulanteCvService.generar(authentication);
    }

    @GetMapping
    public ResponseEntity<byte[]> descargar(Authentication authentication) {
        final byte[] archivo = postulanteCvService.obtenerArchivo(authentication);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(archivo);
    }
}
