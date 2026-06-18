# Arquitectura pendiente

Estado actual:
- Cada entidad ya tiene su paquete propio.
- `Empresa` tiene controller, service, repository y entidad separadas.
- Las excepciones quedaron centralizadas en `com.facultad.sistemaavisos.shared.exception`.
- Se agrego `ErrorResponse` como respuesta de error comun.
- Se agregaron contratos de servicio para los modulos restantes.

Lo que sigue faltando antes de codificar la logica de dominio:
- DTOs de request y response por modulo.
- Mappers entre entidad y DTO.
- Validaciones de entrada con `@Valid` y Bean Validation.
- Controllers para los modulos restantes.
- Implementaciones reales de service para los modulos restantes.
- Carga inicial de estados si se van a usar valores fijos.

Puntos a revisar en el modelo de entidades antes de avanzar:
- `Aviso` depende de `Empresa`, `Reclutador` y `EstadoAviso`; hay que confirmar si todos son obligatorios desde el inicio o si alguno puede ser opcional.
- `Postulacion` depende de `Postulante`, `Aviso` y `EstadoPostulacion`; hay que validar si la combinacion postulante-aviso debe ser unica a nivel de base.
- `Carrera` y `Postulante` probablemente necesitan una relacion si la carrera academica forma parte del perfil del postulante.
- `Empresa`, `Reclutador`, `Postulante` y los estados tienen campos de baja/alta, pero todavia falta definir las reglas de transicion.
- `Empresa` usa `cuit` como clave primaria; hay que confirmar si ese identificador realmente es estable para el modelo final.

Nota:
- No se toco la semantica de los atributos de las entidades todavia. La reorganizacion fue solo estructural.
