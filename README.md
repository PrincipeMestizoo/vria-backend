# VRIA Backend

API REST del sistema VRIA (control de inventario, stock critico y pagos), construida a partir
del Informe de Levantamiento VRIA. CRUD de `Producto` protegido con JWT, restringido a los roles
`ADMINISTRADOR` y `BODEGUERO`.

## Stack

- Spring Boot 4.1.0 
- Java 25
- Spring Data JPA + MySQL
- Spring Security 7 (stateless, JWT con jjwt 0.12.x)