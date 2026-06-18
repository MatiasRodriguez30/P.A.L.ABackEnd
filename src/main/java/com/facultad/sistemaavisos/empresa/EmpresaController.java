package com.facultad.sistemaavisos.empresa;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public List<Empresa> listarTodas() {
        return empresaService.listarTodas();
    }

    @GetMapping("/{cuitEmpresa}")
    public Empresa buscarPorCuit(@PathVariable String cuitEmpresa) {
        return empresaService.buscarPorCuit(cuitEmpresa);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empresa crear(@RequestBody Empresa empresa) {
        return empresaService.crear(empresa);
    }

    @PutMapping("/{cuitEmpresa}")
    public Empresa actualizar(
            @PathVariable String cuitEmpresa,
            @RequestBody Empresa empresa
    ) {
        return empresaService.actualizar(cuitEmpresa, empresa);
    }

    @DeleteMapping("/{cuitEmpresa}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String cuitEmpresa) {
        empresaService.eliminar(cuitEmpresa);
    }
}
