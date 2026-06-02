# MGCSS - Sistema de Gestión de Solicitudes y Mantenimiento Técnico

[![CI Pipeline](https://github.com/franciscorrego/mgcss-track-L2-Grupo3/actions/workflows/ci.yml/badge.svg)](https://github.com/franciscorrego/mgcss-track-L2-Grupo3/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=franciscorrego_mgcss-track-L2-Grupo3&metric=alert_status)](https://sonarcloud.io/dashboard?id=franciscorrego_mgcss-track-L2-Grupo3)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=franciscorrego_mgcss-track-L2-Grupo3&metric=coverage)](https://sonarcloud.io/dashboard?id=franciscorrego_mgcss-track-L2-Grupo3)

## Descripción del Proyecto
El proyecto **MGCSS** es un sistema backend empresarial diseñado para automatizar y trazar el ciclo de vida de incidencias. Implementa una arquitectura limpia basada en DDD, protegiendo al sistema de estados inconsistentes mediante un **GlobalExceptionHandler** que centraliza la gestión de errores.

---

## Panel de Control y Documentación
* **Especificación Extendida:** [Casos de Uso detallados](docs/use-cases.md)
* **Contrato API (Swagger):** [Consultar Swagger Local](http://localhost:8080/swagger-ui/index.html)

---

## Restricciones Arquitectónicas
El sistema utiliza cuatro capas:
1. **Domain:** Entidades de negocio puras.
2. **Services:** Orquestación de lógica.
3. **Infrastructure:** Persistencia (Spring Data JPA).
4. **API:** Controladores y `GlobalExceptionHandler`.

### Gestión Centralizada de Errores
El `GlobalExceptionHandler` mapea las excepciones de dominio a respuestas HTTP semánticas:
- `IllegalArgumentException` -> **404 Not Found** (recurso no existe).
- `IllegalStateException` -> **400 Bad Request** (violación de regla de negocio/estado).
- `MethodArgumentNotValidException` -> **400 Bad Request** (error de validación de DTO).

---

## Catálogo de Endpoints de la API REST

### Gestión de Clientes (`/api/clientes`)
| Método | Endpoint | HTTP Success | Errores Controlados |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/clientes` | `201 Created` | `400` (Validación DTO) |
| **PUT** | `/api/clientes/{id}/desactivar` | `204 No Content` | `400` (Regla negocio), `404` (No existe) |

### Gestión de Técnicos (`/api/tecnicos`)
| Método | Endpoint | HTTP Success | Errores Controlados |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/tecnicos` | `201 Created` | `400` (Validación DTO) |
| **PUT** | `/api/tecnicos/{id}/desactivar` | `204 No Content` | `400` (Regla negocio), `404` (No existe) |

### Gestión de Solicitudes (`/api/solicitudes`)
| Método | Endpoint | HTTP Success | Errores Controlados |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/solicitudes` | `201 Created` | `400` (Validación), `404` (Cliente no existe) |
| **PUT** | `/api/solicitudes/{id}/tecnico` | `204 No Content` | `400`, `404` |
| **PUT** | `/api/solicitudes/{id}/cerrar` | `204 No Content` | `400` (IllegalState), `404` |
| **PATCH**| `/api/solicitudes/{id}/reabrir` | `204 No Content` | `400` (IllegalState), `404` |

---

## Tecnologías y Requisitos Previos
* **JDK:** 17+
* **Maven:** 3.8+
* **Database:** H2 (In-memory)

## Instalación y Ejecución
1. **Clonar:** `git clone https://github.com/franciscorrego/mgcss-track-L2-Grupo3.git`
2. **Compilar:** `./mvnw clean install`
3. **Ejecutar:** `./mvnw spring-boot:run`
4. **Pruebas:** `./mvnw test`
