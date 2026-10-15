# ms-vidasalud-catalog

Microservicio de dominio responsable del **catálogo de prestaciones, boxes, cupos y precios** de VidaSalud.

## Stack requerido

- Spring Boot.
- Spring Security.
- PostgreSQL.

## Responsabilidades

- CRUD de prestaciones.
- CRUD/administración de boxes.
- Administración de cupos disponibles.
- Administración de precios.
- Proveer la información necesaria para que el flujo de atenciones coordine la reserva de cupos.

## Regla funcional clave

El cupo del box disminuye al confirmar una atención.

La pantalla `/catalog` está disponible para Admin y Recepcionista, mientras que el alcance funcional asigna el CRUD administrativo al Admin. Una separación razonable es:

- Admin: lectura y escritura.
- Recepcionista: consulta de prestaciones, boxes y cupos.

## API pública de dominio

Exposición definida:

```text
/api/catalog/*
```

Endpoints mínimos:

### Listar prestaciones

```http
GET /api/catalog/services
```

### Crear prestación

```http
POST /api/catalog/services
```

### Actualizar prestación

```http
PUT /api/catalog/services/{id}
```

El enunciado menciona que esta actualización contempla precio/cupo.

## Seguridad

El flujo de acceso es:

```text
JWT → API Gateway → ms-vidasalud-bff → ms-vidasalud-catalog
```

Spring Security debe validar el JWT de Azure AD y aplicar autorización por rol.

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://login.microsoftonline.com/<TENANT_ID>/v2.0
```

## Persistencia

Base de datos definida para la implementación: **PostgreSQL**.

El modelo debe cubrir, al menos:

- prestaciones;
- boxes;
- cupos disponibles;
- precios.

> El PDF no define el esquema físico ni nombres de tablas.

## Configuración sugerida

> Nombres sugeridos, no definidos en el PDF.

```text
POSTGRES_URL=jdbc:postgresql://<host>:5432/<database>
POSTGRES_USERNAME=<user>
POSTGRES_PASSWORD=<password>
AZURE_TENANT_ID=<tenant>
```
