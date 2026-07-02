package com.facultad.sistemaavisos.carrera;
import com.facultad.sistemaavisos.shared.exception.*;import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;import lombok.RequiredArgsConstructor;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.time.Instant;import java.util.*;
@RestController @RequestMapping("/api/admin/carreras") @RequiredArgsConstructor @PreAuthorize("hasAnyAuthority('ABM_CARRERA','GESTIONAR_CONEXION')")
public class CarreraAdminController{
 private final CarreraRepository repo;
 @GetMapping public List<Response> listar(){return repo.findAllByOrderByNombreCarreraAsc().stream().map(Response::from).toList();}
 @PostMapping public Response crear(@RequestBody @Valid Request r){if(repo.findByNombreCarrera(r.nombre()).isPresent())throw new OperacionInvalidaException("Ya existe una carrera con ese nombre");return Response.from(repo.save(Carrera.builder().nombreCarrera(r.nombre().trim()).descripcionCarrera(l(r.descripcion())).fechaAltaCarrera(Instant.now()).build()));}
 @PatchMapping("/{id}") public Response editar(@PathVariable Long id,@RequestBody @Valid Request r){var c=b(id);c.setNombreCarrera(r.nombre().trim());c.setDescripcionCarrera(l(r.descripcion()));c.setFechaActualizacionCarrera(Instant.now());return Response.from(repo.save(c));}
 @DeleteMapping("/{id}") public Response baja(@PathVariable Long id){var c=b(id);if(c.getFechaBajaCarrera()==null)c.setFechaBajaCarrera(Instant.now());return Response.from(repo.save(c));}
 @PatchMapping("/{id}/reactivar") public Response reactivar(@PathVariable Long id){var c=b(id);c.setFechaBajaCarrera(null);return Response.from(repo.save(c));}
 private Carrera b(Long id){return repo.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Carrera no encontrada"));}private String l(String s){return s==null||s.isBlank()?null:s.trim();}
 public record Request(@NotBlank String nombre,String descripcion){} public record Response(Long id,String nombre,String descripcion,Instant alta,Instant actualizacion,Instant baja,String origen,String urlOrigen){static Response from(Carrera c){return new Response(c.getId(),c.getNombreCarrera(),c.getDescripcionCarrera(),c.getFechaAltaCarrera(),c.getFechaActualizacionCarrera(),c.getFechaBajaCarrera(),c.getConexionCarrera()==null?"Manual":c.getConexionCarrera().getNombreConexionCarrera(),c.getUrlOrigenCarrera());}}
}
