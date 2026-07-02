package com.facultad.sistemaavisos.conexioncarrera;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import lombok.RequiredArgsConstructor; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.*;
@RestController @RequestMapping("/api/conexiones-carrera") @RequiredArgsConstructor @PreAuthorize("hasAuthority('GESTIONAR_CONEXION')")
public class ConexionCarreraController {
 private final ConexionCarreraService service;
 @GetMapping public List<Response> listar(){return service.listar().stream().map(c->Response.from(c,service.cantidad(c.getId()))).toList();}
 @PostMapping public Response crear(@RequestBody @Valid Request r){var c=service.crear(r.nombre(),r.url());return Response.from(c,0);}
 @PatchMapping("/{id}") public Response editar(@PathVariable Long id,@RequestBody @Valid Request r){var c=service.actualizar(id,r.nombre(),r.url());return Response.from(c,service.cantidad(id));}
 @DeleteMapping("/{id}") public Response baja(@PathVariable Long id){var c=service.baja(id);return Response.from(c,service.cantidad(id));}
 @PatchMapping("/{id}/reactivar") public Response reactivar(@PathVariable Long id){var c=service.reactivar(id);return Response.from(c,service.cantidad(id));}
 @PostMapping("/{id}/previsualizar") public ConexionCarreraService.Resultado preview(@PathVariable Long id){return service.sincronizar(id,false);}
 @PostMapping("/{id}/sincronizar") public ConexionCarreraService.Resultado sync(@PathVariable Long id){return service.sincronizar(id,true);}
 public record Request(@NotBlank String nombre,@NotBlank String url){}
 public record Response(Long id,String nombre,String url,Instant fechaCreacion,Instant ultimaSincronizacion,Instant fechaBaja,long cantidadCarreras){static Response from(ConexionCarrera c,long cantidad){return new Response(c.getId(),c.getNombreConexionCarrera(),c.getUrlConexionCarrera(),c.getFechaCreacionConexionCarrera(),c.getFechaUltimaSincronizacion(),c.getFechaBajaConexionCarrera(),cantidad);}}
}
