# Sistema de Avisos y Postulaciones

Sistema desarrollado en Java con Spring Boot para gestionar avisos laborales/académicos, empresas, reclutadores, postulantes y postulaciones.

El proyecto está basado en un modelo de dominio donde las empresas publican avisos, los reclutadores los administran y los postulantes pueden postularse a dichos avisos según su perfil académico.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.7
- Spring Web
- Spring Data JPA
- Hibernate
- Maven
- Lombok
- Validation
- H2 Database / MySQL
- Spring Boot DevTools

---

## Estructura del proyecto

```text
src/main/java/com/facultad/sistemaavisos
├── aviso
├── carrera
├── empresa
├── estadoaviso
├── estadopostulacion
├── postulante
├── postulacion
├── reclutador
├── shared
└── SistemaAvisosApplication.java
```

### Paquetes principales

- `empresa`, `reclutador`, `postulante`, `carrera`, `aviso`, `postulacion`, `estadoaviso`, `estadopostulacion`: cada modulo contiene su entidad, repository y service.
- `shared.exception`: excepciones globales y handler comun.
- `shared.dto`: objetos de respuesta compartidos.

---

## Entidades principales

Actualmente el sistema cuenta con las siguientes entidades base:

- `Empresa`
- `Reclutador`
- `Postulante`
- `Carrera`
- `Aviso`
- `Postulacion`
- `EstadoAviso`
- `EstadoPostulacion`

---

## Funcionalidades iniciales

El sistema permite, en su primera versión:

- Registrar empresas.
- Listar empresas.
- Buscar empresas por CUIT.
- Actualizar datos de empresas.
- Eliminar empresas.
- Modelar avisos asociados a empresas y reclutadores.
- Modelar postulaciones asociadas a postulantes y avisos.
- Manejar estados de aviso y estados de postulación.

---

## Configuración con H2

Para usar H2 como base de datos en memoria, el archivo:

```text
src/main/resources/application.properties
```

debe tener la siguiente configuración:

```properties
spring.application.name=sistema-avisos

spring.datasource.url=jdbc:h2:mem:sistema_avisos
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Para acceder a la consola de H2:

```text
http://localhost:8080/h2-console
```

Datos de acceso:

```text
JDBC URL: jdbc:h2:mem:sistema_avisos
User Name: sa
Password:
```

El campo `Password` debe quedar vacío.

---

## Configuración con MySQL

Si se desea usar MySQL, se puede reemplazar la configuración anterior por:

```properties
spring.application.name=sistema-avisos

spring.datasource.url=jdbc:mysql://localhost:3306/sistema_avisos?createDatabaseIfNotExist=true&serverTimezone=America/Argentina/Buenos_Aires
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

Se debe reemplazar `TU_PASSWORD` por la contraseña real del usuario de MySQL.

---

## Cómo ejecutar el proyecto

Desde la raíz del proyecto, donde se encuentra el archivo `pom.xml`, ejecutar:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
.\mvnw.cmd spring-boot:run
```

También se puede ejecutar directamente desde el IDE abriendo la clase:

```text
SistemaAvisosApplication.java
```

y presionando `Run`.

---

## Endpoints disponibles

### Empresas

#### Listar empresas

```http
GET /api/empresas
```

Ejemplo:

```text
http://localhost:8080/api/empresas
```

Respuesta esperada inicial:

```json
[]
```

---

#### Buscar empresa por CUIT

```http
GET /api/empresas/{cuitEmpresa}
```

Ejemplo:

```text
http://localhost:8080/api/empresas/30-12345678-9
```

---

#### Crear empresa

```http
POST /api/empresas
```

Body de ejemplo:

```json
{
  "cuitEmpresa": "30-12345678-9",
  "descripcionEmpresa": "Empresa de prueba",
  "direccionEmpresa": "San Martín 123",
  "mailEmpresa": "contacto@empresa.com",
  "nombreEmpresa": "Empresa Demo",
  "nroEmpresa": 1
}
```

---

#### Actualizar empresa

```http
PUT /api/empresas/{cuitEmpresa}
```

Ejemplo:

```text
http://localhost:8080/api/empresas/30-12345678-9
```

Body de ejemplo:

```json
{
  "descripcionEmpresa": "Empresa actualizada",
  "direccionEmpresa": "Belgrano 456",
  "mailEmpresa": "nuevo@empresa.com",
  "nombreEmpresa": "Empresa Demo Actualizada",
  "nroEmpresa": 1
}
```

---

#### Eliminar empresa

```http
DELETE /api/empresas/{cuitEmpresa}
```

Ejemplo:

```text
http://localhost:8080/api/empresas/30-12345678-9
```

---

## Manejo de errores

El sistema cuenta con una excepción personalizada:

```java
RecursoNoEncontradoException
```

Y un manejador global:

```java
GlobalExceptionHandler
```

Cuando no se encuentra un recurso, la API responde con un error controlado en formato JSON.

Ejemplo:

```json
{
  "fechaHora": "2026-06-15T22:30:00",
  "estado": 404,
  "error": "Recurso no encontrado",
  "mensaje": "No se encontró la empresa con CUIT: 30-12345678-9"
}
```

---

## Reglas de negocio previstas

Algunas reglas que se espera incorporar en próximas etapas:

- Un postulante no puede postularse dos veces al mismo aviso.
- Un aviso cerrado no debe aceptar nuevas postulaciones.
- Una empresa dada de baja no debería publicar nuevos avisos.
- Un reclutador solo debería crear avisos para empresas asociadas.
- Una postulación debe iniciar con estado pendiente.
- Un aviso debe iniciar con un estado inicial, por ejemplo borrador o publicado.

---

## Estado actual del desarrollo

El proyecto se encuentra en una etapa inicial y la estructura ya fue separada por modulo.

Ya se configuraron:

- Proyecto Spring Boot base.
- Estructura por modulo.
- Entidades principales.
- Repositories por modulo.
- Manejo global de errores.
- Service inicial de empresa.
- Controller inicial de empresa.
- Contratos de servicio para los modulos restantes.

Pendiente de implementar:

- Controllers y services reales para reclutador.
- Controllers y services reales para postulante.
- Controllers y services reales para carrera.
- Controllers y services reales para aviso.
- Controllers y services reales para postulación.
- DTOs.
- Mappers.
- Validaciones.
- Carga inicial de estados.
- Seguridad/autenticación, si corresponde.

---

## Autora

Proyecto desarrollado para uso académico en la Facultad.
