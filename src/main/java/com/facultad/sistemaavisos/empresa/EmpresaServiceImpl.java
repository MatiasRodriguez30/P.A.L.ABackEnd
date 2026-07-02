package com.facultad.sistemaavisos.empresa;

import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import com.facultad.sistemaavisos.empresa.dto.EmpresaCreateRequest;
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
        return empresaRepository.findAllByOrderByRazonSocialEmpresaAsc();
    }

    @Override
    public List<Empresa> listarActivas() {
        return empresaRepository.findByFechaBajaEmpresaIsNullOrderByRazonSocialEmpresaAsc();
    }

    @Override
    public Empresa buscarPorCuit(String cuitEmpresa) {
        return empresaRepository.findByCuitEmpresa(cuitEmpresa)
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
    public Empresa crearDesdeRequest(EmpresaCreateRequest request) {
        final String cuit = normalizarCuit(request.cuitEmpresa());
        if (cuit == null || cuit.length() != 11) {
            throw new OperacionInvalidaException("El CUIT debe contener 11 digitos numericos");
        }
        if (empresaRepository.findByCuitEmpresaNormalizado(cuit).isPresent()) {
            throw new OperacionInvalidaException("Ya existe una empresa registrada con ese CUIT");
        }
        if (empresaRepository.existsByMailEmpresaIgnoreCase(request.mailEmpresa().trim())) {
            throw new OperacionInvalidaException("Ya existe una empresa registrada con ese mail");
        }

        return empresaRepository.save(Empresa.builder()
                .cuitEmpresa(cuit)
                .razonSocialEmpresa(request.razonSocialEmpresa().trim())
                .mailEmpresa(request.mailEmpresa().trim().toLowerCase())
                .telefonoEmpresa(limpiar(request.telefonoEmpresa()))
                .descripcionEmpresa(limpiar(request.descripcionEmpresa()))
                .direccionEmpresa(limpiar(request.direccionEmpresa()))
                .fechaAltaEmpresa(Instant.now())
                .build());
    }

    @Override
    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada"));
    }

    @Override
    public Empresa actualizarDesdeRequest(Long id, EmpresaCreateRequest request) {
        final Empresa empresa = buscarPorId(id);
        final String cuit = normalizarCuit(request.cuitEmpresa());
        if (cuit == null || cuit.length() != 11) {
            throw new OperacionInvalidaException("El CUIT debe contener 11 digitos numericos");
        }
        empresaRepository.findByCuitEmpresaNormalizado(cuit)
                .filter(otra -> !otra.getId().equals(id))
                .ifPresent(otra -> { throw new OperacionInvalidaException("Ya existe una empresa con ese CUIT"); });
        if (empresaRepository.existsByMailEmpresaIgnoreCaseAndIdNot(request.mailEmpresa().trim(), id)) {
            throw new OperacionInvalidaException("Ya existe una empresa registrada con ese mail");
        }

        empresa.setCuitEmpresa(cuit);
        empresa.setRazonSocialEmpresa(request.razonSocialEmpresa().trim());
        empresa.setMailEmpresa(request.mailEmpresa().trim().toLowerCase());
        empresa.setTelefonoEmpresa(limpiar(request.telefonoEmpresa()));
        empresa.setDescripcionEmpresa(limpiar(request.descripcionEmpresa()));
        empresa.setDireccionEmpresa(limpiar(request.direccionEmpresa()));
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa darDeBaja(Long id) {
        final Empresa empresa = buscarPorId(id);
        if (empresa.getFechaBajaEmpresa() == null) empresa.setFechaBajaEmpresa(Instant.now());
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa reactivar(Long id) {
        final Empresa empresa = buscarPorId(id);
        empresa.setFechaBajaEmpresa(null);
        return empresaRepository.save(empresa);
    }

    private String normalizarCuit(String cuit) {
        if (cuit == null) return null;
        final String limpio = cuit.trim().replace("-", "").replace(" ", "");
        return limpio.chars().allMatch(Character::isDigit) ? limpio : null;
    }

    private String limpiar(String valor) {
        if (valor == null || valor.isBlank()) return null;
        return valor.trim();
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
