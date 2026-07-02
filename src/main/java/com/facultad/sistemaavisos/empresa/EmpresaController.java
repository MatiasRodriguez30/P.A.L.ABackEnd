package com.facultad.sistemaavisos.empresa;

import com.facultad.sistemaavisos.empresa.dto.EmpresaActivaResponse;
import com.facultad.sistemaavisos.empresa.dto.EmpresaCreateRequest;
import com.facultad.sistemaavisos.empresa.dto.EmpresaResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping
    @PreAuthorize("hasAuthority('ABM_EMPRESA')")
    public ResponseEntity<EmpresaResponse> crear(@RequestBody @Valid EmpresaCreateRequest request) {
        final Empresa empresa = empresaService.crearDesdeRequest(request);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(EmpresaResponse.from(empresa));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ABM_EMPRESA')")
    public ResponseEntity<List<EmpresaResponse>> listarTodas() {
        return ResponseEntity.ok(empresaService.listarTodas().stream().map(EmpresaResponse::from).toList());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ABM_EMPRESA')")
    public ResponseEntity<EmpresaResponse> actualizar(
            @PathVariable Long id, @RequestBody @Valid EmpresaCreateRequest request) {
        return ResponseEntity.ok(EmpresaResponse.from(empresaService.actualizarDesdeRequest(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ABM_EMPRESA')")
    public ResponseEntity<EmpresaResponse> darDeBaja(@PathVariable Long id) {
        return ResponseEntity.ok(EmpresaResponse.from(empresaService.darDeBaja(id)));
    }

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasAuthority('ABM_EMPRESA')")
    public ResponseEntity<EmpresaResponse> reactivar(@PathVariable Long id) {
        return ResponseEntity.ok(EmpresaResponse.from(empresaService.reactivar(id)));
    }
}
