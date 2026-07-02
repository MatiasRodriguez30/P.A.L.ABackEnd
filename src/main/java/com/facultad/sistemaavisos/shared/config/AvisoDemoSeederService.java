package com.facultad.sistemaavisos.shared.config;

import com.facultad.sistemaavisos.aviso.Aviso;
import com.facultad.sistemaavisos.aviso.AvisoRepository;
import com.facultad.sistemaavisos.avisocarrera.AvisoCarrera;
import com.facultad.sistemaavisos.avisoestado.AvisoEstado;
import com.facultad.sistemaavisos.avisotipoaviso.AvisoTipoAviso;
import com.facultad.sistemaavisos.avisotipoavisosubtipoaviso.AvisoTipoAvisoSubTipoAviso;
import com.facultad.sistemaavisos.carrera.Carrera;
import com.facultad.sistemaavisos.carrera.CarreraRepository;
import com.facultad.sistemaavisos.empresa.Empresa;
import com.facultad.sistemaavisos.empresa.EmpresaRepository;
import com.facultad.sistemaavisos.estadoaviso.EstadoAviso;
import com.facultad.sistemaavisos.reclutador.Reclutador;
import com.facultad.sistemaavisos.reclutador.ReclutadorRepository;
import com.facultad.sistemaavisos.subtipoaviso.SubTipoAviso;
import com.facultad.sistemaavisos.subtipoaviso.SubTipoAvisoRepository;
import com.facultad.sistemaavisos.tipoaviso.TipoAviso;
import com.facultad.sistemaavisos.tipoaviso.TipoAvisoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

// Logica transaccional del seed de demo, separada de AvisosDemoSeedInitializer
// para que @Transactional realmente aplique (un ApplicationRunner llamando a su
// propio metodo @Transactional hace self-invocation y Spring AOP lo ignora).
@Service
@RequiredArgsConstructor
class AvisoDemoSeederService {

    private final AvisoRepository avisoRepository;
    private final EmpresaRepository empresaRepository;
    private final ReclutadorRepository reclutadorRepository;
    private final CarreraRepository carreraRepository;
    private final TipoAvisoRepository tipoAvisoRepository;
    private final SubTipoAvisoRepository subTipoAvisoRepository;
    private final EntityManager entityManager;

    record EmpresaSeed(String cuit, String mail, String razonSocial, String descripcion, String direccion, String telefono) {
    }

    record ReclutadorSeed(String cuil, String mail, String nombre) {
    }

    record TipoAvisoSeed(String nombreTipoAviso, List<String> subTipos) {
    }

    @Transactional
    void asegurarEmpresaDemo(EmpresaSeed empresaSeed) {
        buscarOCrearEmpresa(empresaSeed);
    }

    @Transactional
    void crearAvisoSiNoExiste(
            String nombreAviso,
            String descripcionAviso,
            Duration vigencia,
            String imagenUrlAviso,
            EmpresaSeed empresaSeed,
            ReclutadorSeed reclutadorSeed,
            List<String> carreras,
            List<TipoAvisoSeed> tiposAviso,
            EstadoAviso estadoAbierto
    ) {
        if (avisoRepository.findByNombreAvisoAndFechaBajaAvisoIsNull(nombreAviso).isPresent()) {
            return;
        }

        final Instant ahora = Instant.now();
        final Empresa empresa = buscarOCrearEmpresa(empresaSeed);
        final Reclutador reclutador = buscarOCrearReclutador(reclutadorSeed);

        final Aviso aviso = Aviso.builder()
                .nombreAviso(nombreAviso)
                .descripcionAviso(descripcionAviso)
                .fechaCreacionAviso(ahora)
                .fechaPublicacionAviso(ahora)
                .fechaCierreAviso(ahora.plus(vigencia))
                .imagenUrlAviso(imagenUrlAviso)
                .empresa(empresa)
                .reclutador(reclutador)
                .estadoActual(estadoAbierto)
                .build();

        aviso.getAvisosEstado().add(AvisoEstado.builder()
                .aviso(aviso)
                .estadoAviso(estadoAbierto)
                .fechaInicioVigenciaEstado(ahora)
                .build());

        for (String nombreCarrera : carreras) {
            aviso.getAvisosCarrera().add(AvisoCarrera.builder()
                    .aviso(aviso)
                    .carrera(buscarOCrearCarrera(nombreCarrera))
                    .fechaAsignacionCarrera(ahora)
                    .build());
        }

        for (TipoAvisoSeed tipoSeed : tiposAviso) {
            final TipoAviso tipoAviso = buscarOCrearTipoAviso(tipoSeed.nombreTipoAviso());

            final AvisoTipoAviso avisoTipoAviso = AvisoTipoAviso.builder()
                    .aviso(aviso)
                    .tipoAviso(tipoAviso)
                    .fechaHoraAsignacionTipoAviso(ahora)
                    .build();

            for (String nombreSubTipo : tipoSeed.subTipos()) {
                avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().add(AvisoTipoAvisoSubTipoAviso.builder()
                        .avisoTipoAviso(avisoTipoAviso)
                        .subTipoAviso(buscarOCrearSubTipoAviso(tipoAviso, nombreSubTipo))
                        .fechaHoraAsignacionAvisoTipoAvisoSubTipoAviso(ahora)
                        .build());
            }

            aviso.getAvisosTipoAvisos().add(avisoTipoAviso);
        }

        avisoRepository.save(aviso);
    }

    private Empresa buscarOCrearEmpresa(EmpresaSeed seed) {
        return empresaRepository.findByCuitEmpresaNormalizado(normalizarCuit(seed.cuit()))
                .or(() -> empresaRepository.findByCuitEmpresa(seed.cuit()))
                .map(empresa -> {
            boolean modificada = false;

            if (estaVacio(empresa.getMailEmpresa()) && !estaVacio(seed.mail())) {
                empresa.setMailEmpresa(seed.mail());
                modificada = true;
            }
            if (estaVacio(empresa.getRazonSocialEmpresa()) && !estaVacio(seed.razonSocial())) {
                empresa.setRazonSocialEmpresa(seed.razonSocial());
                modificada = true;
            }
            if (estaVacio(empresa.getDescripcionEmpresa()) && !estaVacio(seed.descripcion())) {
                empresa.setDescripcionEmpresa(seed.descripcion());
                modificada = true;
            }
            if (estaVacio(empresa.getDireccionEmpresa()) && !estaVacio(seed.direccion())) {
                empresa.setDireccionEmpresa(seed.direccion());
                modificada = true;
            }
            if (estaVacio(empresa.getTelefonoEmpresa()) && !estaVacio(seed.telefono())) {
                empresa.setTelefonoEmpresa(seed.telefono());
                modificada = true;
            }
            if (empresa.getFechaAltaEmpresa() == null) {
                empresa.setFechaAltaEmpresa(Instant.now());
                modificada = true;
            }

            return modificada ? empresaRepository.save(empresa) : empresa;
                }).orElseGet(() -> empresaRepository.save(
                Empresa.builder()
                        .cuitEmpresa(seed.cuit())
                        .mailEmpresa(seed.mail())
                        .razonSocialEmpresa(seed.razonSocial())
                        .descripcionEmpresa(seed.descripcion())
                        .direccionEmpresa(seed.direccion())
                        .telefonoEmpresa(seed.telefono())
                        .fechaAltaEmpresa(Instant.now())
                        .build()
        ));
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private String normalizarCuit(String cuit) {
        return cuit == null ? null : cuit.trim().replace("-", "").replace(" ", "");
    }

    private Reclutador buscarOCrearReclutador(ReclutadorSeed seed) {
        return reclutadorRepository.findByCuilReclutador(seed.cuil()).orElseGet(() -> reclutadorRepository.save(
                Reclutador.builder()
                        .cuilReclutador(seed.cuil())
                        .mailReclutador(seed.mail())
                        .nombreReclutador(seed.nombre())
                        .build()
        ));
    }

    private Carrera buscarOCrearCarrera(String nombreCarrera) {
        return carreraRepository.findByNombreCarrera(nombreCarrera).orElseGet(() -> carreraRepository.save(
                Carrera.builder()
                        .nombreCarrera(nombreCarrera)
                        .fechaAltaCarrera(Instant.now())
                        .build()
        ));
    }

    private TipoAviso buscarOCrearTipoAviso(String nombreTipoAviso) {
        return tipoAvisoRepository.findByNombreTipoAviso(nombreTipoAviso).orElseGet(() -> tipoAvisoRepository.save(
                TipoAviso.builder()
                        .nombreTipoAviso(nombreTipoAviso)
                        .fechaHoraAltaTipoAviso(Instant.now())
                        .build()
        ));
    }

    private SubTipoAviso buscarOCrearSubTipoAviso(TipoAviso tipoAviso, String nombreSubTipoAviso) {
        return subTipoAvisoRepository.findByNombreSubTipoAviso(nombreSubTipoAviso).orElseGet(() -> {
            final SubTipoAviso nuevoSubTipo = SubTipoAviso.builder()
                    .nombreSubTipoAviso(nombreSubTipoAviso)
                    .fechaAltaSubTipoAviso(Instant.now())
                    .build();
            // SubTipoAviso no tiene el FK como campo propio: cod_tipo_aviso lo administra
            // el lado dueño (TipoAviso.subTipoAvisos), cascade=PERSIST se encarga del insert.
            // Forzamos un flush ahora (no save()/merge(), que rompe el cascade sobre una
            // entidad ya managed) para que nuevoSubTipo tenga ID antes de que el aviso lo
            // referencie via AvisoTipoAvisoSubTipoAviso.
            tipoAviso.getSubTipoAvisos().add(nuevoSubTipo);
            entityManager.flush();
            return nuevoSubTipo;
        });
    }
}
