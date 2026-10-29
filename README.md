# VRIA Backend

VRIA es un sistema de gestión comercial y logística. Este repositorio contiene su backend: una API REST que permite administrar el catálogo de productos, controlar el inventario, registrar los pagos recibidos por transferencia y hacer seguimiento a las entregas a clientes.

Está construido con Java 25 y Spring Boot 4, usa PostgreSQL como base de datos y protege el acceso mediante tokens JWT.

## ¿Qué se puede hacer con VRIA?

- **Gestionar usuarios.** Los administradores crean, editan y dan de baja a las personas que usan el sistema, y les asignan un rol.
- **Mantener el catálogo de productos.** Cada producto tiene nombre, referencia, descripción, precio, existencias, una foto y, opcionalmente, una categoría.
- **Organizar por categorías.** Las categorías agrupan los productos. Si se elimina una categoría, sus productos no se pierden: simplemente quedan sin categoría.
- **Descontar existencias.** Cuando sale mercancía se puede reducir el stock de un producto. El sistema impide descontar más unidades de las que hay disponibles.
- **Registrar transferencias.** Se guardan los comprobantes de pago recibidos por transferencia bancaria: cliente, banco, cuenta de destino, monto y fecha.
- **Hacer seguimiento a las entregas.** Cada entrega registra el cliente, el responsable, la dirección, la forma de pago (transferencia o efectivo) y la fecha. Además, avanza por cuatro estados: en preparación, lista, enviada y entregada. El sistema también guarda quién la creó.

## Roles

El sistema tiene tres tipos de usuario, y cada uno ve solo lo que necesita para su trabajo:

| Rol | Para quién es | Qué puede hacer |
|-----|---------------|-----------------|
| **Administrador** (`ADMIN`) | Quien gestiona el negocio | Todo: usuarios, productos, categorías, transferencias y entregas |
| **Bodeguero** (`WAREHOUSE_KEEPER`) | Quien maneja el inventario | Productos y categorías |
| **Asesor comercial** (`COMMERCIAL_ADVISOR`) | Quien atiende a los clientes | Transferencias y entregas |

Cualquier usuario que haya iniciado sesión puede consultar el listado de productos, porque este alimenta el indicador de stock del panel principal.

## Cómo funciona la sesión

Al registrarse o iniciar sesión, el servidor entrega dos tokens:

- un **token de acceso**, de corta duración, que acompaña cada petición;
- un **token de renovación**, que solo se envía a las rutas de autenticación y sirve para obtener un par nuevo cuando el de acceso expira.


Si el cliente no es un navegador, como Postman o una integración, también puede enviar el token en el encabezado `Authorization: Bearer <token>`.

Las contraseñas se guardan cifradas y las cuentas deshabilitadas no pueden autenticarse.

## Endpoints principales

Todas las rutas parten de `/api/v1`.

**Autenticación** (públicas, salvo `/me`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/auth/register` | Crea un usuario e inicia su sesión |
| POST | `/auth/login` | Inicia sesión con correo y contraseña |
| POST | `/auth/refresh` | Renueva los tokens de la sesión |
| POST | `/auth/logout` | Cierra la sesión y borra las cookies |
| GET | `/auth/me` | Devuelve el usuario de la sesión actual |

**Usuarios:** `/users`, con listado, consulta, creación, edición y eliminación.

**Categorías:** `/categories`, con listado, consulta, creación, edición y eliminación.

**Productos:** `/products`, con listado, consulta, filtro por categoría (`/products/category/{id}`), creación, edición, eliminación y reducción de stock (`PATCH /products/{id}/reduce?quantity=N`).

**Transferencias:** `/transfers`, con listado, consulta, creación y eliminación.

**Entregas:** `/deliveries`, con listado, consulta, filtro por estado (`/deliveries/state/{estado}`), creación, cambio de estado (`PATCH /deliveries/{id}/state?state=...`) y eliminación.

**Notificaciones:** `/notifications`. Cada creación, edición, eliminación, salida de stock o cambio de estado de despacho queda registrado con su autor y fecha. Lo que ve cada usuario depende de su rol:

| Rol | Ve |
|-----|----|
| Administrador | Todos los movimientos |
| Encargado de bodega | Movimientos de productos y categorías, sin importar quién los hizo |
| Asesor comercial | Movimientos de productos y categorías, más sus propias transferencias y domicilios |

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/notifications?module=PRODUCT&page=0&size=20` | Notificaciones del día, paginadas del más reciente al más antiguo (`module` es opcional) |
| GET | `/notifications/unread-count` | Cantidad de notificaciones sin leer (no cuenta las acciones propias) |
| PATCH | `/notifications/read-all` | Marca todo como leído |

Las notificaciones son diarias: solo se muestran las del día en curso y a medianoche se eliminan las de días anteriores. El día se calcula con la zona horaria `NOTIFICATIONS_ZONE` (por defecto `America/Bogota`).

La documentación interactiva completa, con los modelos de cada petición y respuesta, está en Swagger:

```
http://localhost:8080/swagger-ui.html
```

### Ejemplo: iniciar sesión

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "ana.gomez@vria.com",
  "password": "SuperSegura123"
}
```

Respuesta (los tokens `access_token` y `refresh_token`):

```json
{
  "idUser": 1,
  "name": "Ana",
  "email": "ana.gomez@vria.com",
  "role": "ADMIN"
}
```

## Manejo de errores

Los errores se devuelven siempre con la misma forma, para que el frontend los muestre sin complicaciones:

- **400**: datos inválidos o una regla de negocio incumplida, por ejemplo un correo ya registrado o stock insuficiente. Si falla la validación de campos, la respuesta indica qué campo falló y por qué.
- **401**: credenciales incorrectas o sesión inválida.
- **403**: el usuario no tiene permiso para esa operación según su rol.
- **500**: error inesperado del servidor.

```json
{
  "timestamp": "2026-10-12T10:15:30",
  "status": 400,
  "message": "Stock insuficiente para el producto Silla: disponible 3, solicitado 5"
}
```

## Organización del código

El proyecto se organiza por módulos de negocio. Cada módulo agrupa sus propios controladores, servicios, repositorios, modelos y DTOs:

```
com.vria
 ├── auth        → registro, inicio y cierre de sesión
 ├── users       → usuarios y roles
 ├── products    → productos y categorías
 ├── transfer    → comprobantes de transferencia
 ├── delivery    → entregas y su estado
 ├── notifications → historial de movimientos y su visibilidad por rol
 └── config      → seguridad, JWT, CORS, Swagger y manejo de errores
```

## Requisitos

- JDK 25
- Maven 3.9 o superior
- PostgreSQL 15 o superior

## Ejecutar el proyecto

```bash
mvn clean spring-boot:run
```

La API queda disponible en `http://localhost:8080`. Las tablas se crean automáticamente al arrancar.

Para generar el empaquetado:

```bash
mvn clean package
```

Esto deja el ejecutable en `target/vria-backend.jar`.

## Frontend

El backend acepta peticiones con credenciales desde cualquier puerto de `localhost`, pensado para el frontend en Angular durante el desarrollo. Para desplegar en otro ambiente hay que agregar el dominio real del frontend en la configuración de CORS.
