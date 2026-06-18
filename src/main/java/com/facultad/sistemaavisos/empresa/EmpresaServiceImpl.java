package com.facultad.sistemaavisos.empresa;

import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Override
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa buscarPorCuit(String cuitEmpresa) {
        return empresaRepository.findById(cuitEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la empresa con CUIT: " + cuitEmpresa
                ));
    }

    @Override
    public Empresa crear(Empresa empresa) {
        if (empresa.getFechaAltaEmpresa() == null) {
            empresa.setFechaAltaEmpresa(Instant.now());
        }

        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa actualizar(String cuitEmpresa, Empresa empresa) {
        Empresa empresaExistente = buscarPorCuit(cuitEmpresa);

        empresaExistente.setDescripcionEmpresa(empresa.getDescripcionEmpresa());
        empresaExistente.setDireccionEmpresa(empresa.getDireccionEmpresa());
        empresaExistente.setFechaBajaEmpresa(empresa.getFechaBajaEmpresa());
        empresaExistente.setMailEmpresa(empresa.getMailEmpresa());
        empresaExistente.setRazonSocialEmpresa(empresa.getRazonSocialEmpresa());
        empresaExistente.setTelefonoEmpresa(empresa.getTelefonoEmpresa());

        return empresaRepository.save(empresaExistente);
    }

    @Override
    public void eliminar(String cuitEmpresa) {
        Empresa empresa = buscarPorCuit(cuitEmpresa);
        empresaRepository.delete(empresa);
    }
}
