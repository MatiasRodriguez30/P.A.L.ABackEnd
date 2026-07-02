package com.facultad.sistemaavisos.empresa;

import com.facultad.sistemaavisos.empresa.dto.EmpresaActivaResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAuthority('SOLICITAR_ASOCIACION_RECLUTADOR')")
    public ResponseEntity<List<EmpresaActivaResponse>> listarActivas() {
        return ResponseEntity.ok(
                empresaService.listarActivas().stream()
                        .map(empresa -> new EmpresaActivaResponse(
                                empresa.getId(),
                                empresa.getCuitEmpresa(),
                                empresa.getRazonSocialEmpresa()
                        ))
                        .toList()
        );
    }
}
