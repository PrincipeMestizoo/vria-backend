# VRIA Backend

Backend del sistema de gestion comercial y logistica **VRIA**, construido con
**Java 25**, **Spring Boot 4.1.1**, **Spring Security + JWT** y **PostgreSQL**.

## Estructura del proyecto

El proyecto esta organizado por **modulos de dominio** (feature-based), cada uno
con sus propias capas internas:

```
com.vria
 ├── auth          -> login / registro (emite JWT)
 │    ├── controller
 │    ├── dto
 │    └── service
 ├── config         -> seguridad, CORS, JWT, OpenAPI, manejo global de errores
 │    └── security
 ├── users          -> gestion de usuarios (User, TypeRole)
 │    ├── controller
 │    ├── dto
 │    ├── enums
 │    ├── model
 │    ├── repository
 │    └── service
 ├── products       -> catalogo (Product, Category, TypeCategory)
 │    ├── controller
 │    ├── dto
 │    ├── model
 │    ├── repository
 │    └── service
 └── inventory      -> logistica (Transfer, Delivery, PayMode, StateDelivery)
      ├── controller
      ├── dto
      ├── enums
      ├── model
      ├── repository
      └── service
```

## Requisitos

- JDK 25
- Maven 3.9+
- PostgreSQL 15+ (crear una base de datos `vria_db`)

## Configuracion

Variables de entorno (con valores por defecto en `application.yml`):

| Variable              | Descripcion                              | Default               |
|-----------------------|-------------------------------------------|------------------------|
| `DB_USERNAME`         | Usuario de PostgreSQL                     | `postgres`             |
| `DB_PASSWORD`         | Password de PostgreSQL                    |               |
| `JWT_SECRET`          | Clave Base64 para firmar los JWT (HS256)  | |
| `JWT_EXPIRATION`      | Expiracion del access token (ms)          |         |
| `JWT_REFRESH_EXPIRATION` | Expiracion del refresh token (ms)      |     |

> **Importante:** genera tu propio `JWT_SECRET` en produccion. Puedes crear uno con:
> `openssl rand -base64 64`

## Ejecutar el proyecto

```bash
mvn clean spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Documentacion Swagger: `http://localhost:8080/swagger-ui.html`

## Modulo de autenticacion (auth)

Endpoints publicos (no requieren token):

- `POST /api/v1/auth/register` — crea un usuario y devuelve el `accessToken` / `refreshToken`
- `POST /api/v1/auth/login` — autentica por `email` + `password` y devuelve los tokens

### Ejemplo de registro

```json
POST /api/v1/auth/register
{
  "name": "Ana",
  "lastName": "Gomez",
  "email": "ana.gomez@vria.com",
  "password": "SuperSegura123",
  "role": "ADMIN"
}
```

### Ejemplo de login

```json
POST /api/v1/auth/login
{
  "email": "ana.gomez@vria.com",
  "password": "SuperSegura123"
}
```

Respuesta:

```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "eyJhbGciOi...",
  "tokenType": "Bearer",
  "idUser": 1,
  "name": "Ana",
  "email": "ana.gomez@vria.com",
  "role": "ADMIN"
}
```

Para consumir cualquier otro endpoint (usuarios, productos, categorias, inventario),
envia el header:

```
Authorization: Bearer <accessToken>
```

## Roles del sistema (TypeRole)

- `ADMIN`
- `WAREHOUSE_KEEPER`
- `COMMERCIAL_ADVISOR`

Los controladores usan `@PreAuthorize` para restringir operaciones sensibles
(creacion/edicion/borrado) segun el rol.

## Notas de diseno

- `ddl-auto: update` esta activo para desarrollo; en produccion se recomienda
  usar Flyway/Liquibase con migraciones versionadas.
- Las contrasenas se almacenan con `BCryptPasswordEncoder`.
- El filtro `JwtAuthenticationFilter` valida el token en cada request y
  carga el `User` (que implementa `UserDetails`) directamente en el
  `SecurityContext`.
