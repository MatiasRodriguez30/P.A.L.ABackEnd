Skill: springboot-clean-architecture (actualizada — SistemaAvisos)
yamlname: springboot-clean-architecture
description: >
  Actúa como desarrollador Senior en Java y Spring Boot. Aplica esta skill al generar,
  refactorizar o revisar cualquier código de backend en Spring Boot. Cubre arquitectura limpia,
  Domain-Driven Design, manejo de excepciones, DTOs, validaciones y buenas prácticas de Java moderno.
1. Rich Domain Model (Tell, Don't Ask)

Las entidades NUNCA son anémicas. La lógica de negocio y los cambios de estado viven en la Entidad, no en el Service.
Métodos semánticos en vez de setters sueltos: actualizarDatos(dto), darDeBaja(), activar(), estaDadoDeBaja().

javapublic void actualizarDatos(UsuarioUpdateDTO dto) {
    Optional.ofNullable(dto.mailUsuario()).ifPresent(this::setMailUsuario);
}

public boolean estaDadoDeBaja() {
    return fechaBajaUsuario != null;
}

public void darDeBaja() {
    if (estaDadoDeBaja()) throw new UsuarioDadoDeBajaException(this.id);
    this.fechaBajaUsuario = Instant.now();
}
Cambio aplicado: Instant.now() en vez de LocalDateTime.now(), según la regla del proyecto.
2. Thin Services
Flujo obligatorio: Buscar → Validar → Delegar → Guardar. Prohibido comparar campos crudos de la entidad en el Service.
java@Override
@Transactional
public Usuario updateUsuario(Long id, UsuarioUpdateDTO dto) {
    final Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));

    if (usuario.estaDadoDeBaja()) {
        throw new UsuarioDadoDeBajaException(id);
    }

    usuario.actualizarDatos(dto);
    return usuarioRepository.save(usuario);
}
3. Manejo de Excepciones

Prohibido RuntimeException/Exception genérica para reglas de negocio.
Sin try-catch en controllers/services; las excepciones fluyen al @RestControllerAdvice.

javapublic abstract class DomainException extends RuntimeException {
    protected DomainException(String message) { super(message); }
}

public class UsuarioNotFoundException extends DomainException {
    public UsuarioNotFoundException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }
}

public class UsuarioDadoDeBajaException extends DomainException {
    public UsuarioDadoDeBajaException(Long id) {
        super("El usuario con id " + id + " está dado de baja y no puede ser modificado");
    }
}
java@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UsuarioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UsuarioDadoDeBajaException.class)
    public ResponseEntity<ErrorResponse> handleDadoDeBaja(UsuarioDadoDeBajaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        final String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(mensaje));
    }
}
ErrorResponse (en shared/dto/) debe incluir como mínimo: timestamp (Instant), status, error, mensaje, opcionalmente path.
4. DTOs como Frontera de la API

Entidades nunca expuestas en controllers. Flujo: RequestDTO → Entidad → ResponseDTO.
Mappers manuales por ahora (no MapStruct), pero estructurados como clase (no solo métodos estáticos sueltos) para poder inyectarlos si el proyecto lo requiere más adelante.

usuario/
  dto/
    request/
      UsuarioCreateDTO.java
      UsuarioUpdateDTO.java
    response/
      UsuarioResponseDTO.java
javapublic record UsuarioUpdateDTO(
    @Email String mailUsuario,
    @Size(min = 8) String passwordUsuario
) {}

public record UsuarioResponseDTO(
    Long id,
    String mailUsuario,
    boolean activo
) {}
5. Validación de Entrada

Formato/presencia: anotaciones Bean Validation en el DTO (@NotNull, @Email, @Size, @NotBlank) + @Valid en el controller.
Reglas de negocio (ej. "no se puede dar de baja dos veces"): en la entidad, nunca replicadas en el DTO ni en el Service.

java@PutMapping("/{id}")
public ResponseEntity<UsuarioResponseDTO> update(
        @PathVariable final Long id,
        @RequestBody @Valid final UsuarioUpdateDTO dto) {
    final Usuario actualizado = usuarioService.updateUsuario(id, dto);
    return ResponseEntity.ok(mapper.toResponseDTO(actualizado));
}
6. Repositorios

Nombres expresivos: findByMailAndActivoTrue(), no traer todo y filtrar en el service.
Queries complejas: @Query + JPQL.
Prohibido inyectar EntityManager directamente en un service.

javapublic interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByMailUsuario(String mail);
    List<Usuario> findByFechaBajaUsuarioIsNull(); // activos
    boolean existsByMailUsuario(String mail);
}
7. Java Moderno y Clean Code

Optional funcional (.map(), .orElseThrow(), .ifPresent()), nunca .get() sin verificar.
Early return en vez de if-else anidados.
Variables final siempre que sea posible.
record para DTOs y objetos de valor inmutables.
Streams para colecciones, no bucles imperativos.

8. Estructura de Paquetes — Package by Feature
com.facultad.sistemaavisos
├── usuario/
│   ├── Usuario.java                    ← Entidad rica, extiende EntidadBase si aplica
│   ├── UsuarioRepository.java
│   ├── UsuarioService.java             ← Interface
│   ├── UsuarioServiceImpl.java
│   ├── UsuarioController.java
│   ├── UsuarioMapper.java
│   ├── dto/
│   │   ├── request/
│   │   │   ├── UsuarioCreateDTO.java
│   │   │   └── UsuarioUpdateDTO.java
│   │   └── response/
│   │       └── UsuarioResponseDTO.java
│   └── exception/
│       ├── UsuarioNotFoundException.java
│       └── UsuarioDadoDeBajaException.java
│
├── carrera/ (mismo patrón)
├── aviso/ (mismo patrón)
├── ... (resto de las ~25 entidades, mismo patrón)
│
└── shared/                             ← Solo código genuinamente transversal
    ├── EntidadBase.java                 ← id, fechaCreacion, fechaModificacion (Instant)
    ├── DateUtils.java                   ← conversión Instant → horario regional (uso en frontend)
    ├── exception/
    │   ├── DomainException.java
    │   └── GlobalExceptionHandler.java
    └── dto/
        └── ErrorResponse.java
Reglas:

Cada módulo autocontenido: entidad, service, repo, DTOs, excepciones propias viven juntos.
shared/ no es cajón de sastre — solo lo genuinamente común a todos los módulos.
Excepciones específicas de un módulo van dentro del módulo (usuario/exception/), no en shared/.
Si un módulo necesita datos de otro, pasa por su Service, nunca accede al repositorio ajeno directamente.

Cheatsheet
CapaResponsabilidadProhibidoEntidadLógica de negocio, cambios de estado, validaciones internasSetters públicos indiscriminadosServiceBuscar → validar → delegar → guardarLógica de negocio, leer campos internos directoControllerRecibir request, llamar service, devolver responseLógica de negocio, try-catchDTOTransportar datos, validación de formatoLógica de negocioRepositoryAcceso a datos, queries expresivasLógica de negocioExceptionErrores de dominio expresivosRuntimeException genérica