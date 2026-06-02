# MGCSS - Sistema de Gestión de Solicitudes y Mantenimiento Técnico

[![CI Pipeline](https://github.com/franciscorrego/mgcss-track-L2-Grupo3/actions/workflows/ci.yml/badge.svg)](https://github.com/franciscorrego/mgcss-track-L2-Grupo3/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=franciscorrego_mgcss-track-L2-Grupo3&metric=alert_status)](https://sonarcloud.io/dashboard?id=franciscorrego_mgcss-track-L2-Grupo3)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=franciscorrego_mgcss-track-L2-Grupo3&metric=coverage)](https://sonarcloud.io/dashboard?id=franciscorrego_mgcss-track-L2-Grupo3)

## Descripción del Proyecto
El proyecto **MGCSS** (Desarrollado por el equipo **mgcss-track-L2-Grupo3**) es un sistema backend empresarial diseñado para automatizar, controlar y trazar el ciclo de vida de incidencias y solicitudes de mantenimiento de sistemas software. El core del sistema reside en un **Modelo de Dominio Rico** que actúa como un búnker de reglas de negocio, protegiendo al sistema de estados inconsistentes mediante invariantes estrictas.

---

## Panel de Control y Enlaces del Proyecto
Haz clic en los siguientes accesos directos para abrir las plataformas de automatización externas o navegar por la documentación funcional detallada:

### Documentación Funcional Avanzada (Casos de Uso Correlativos)
* **Especificación Extendida:** [Acceder al documento completo de Casos de Uso (use-cases.md)](docs/use-cases.md)
* **Caso de Uso 1:** [Caso 1 – Crear cliente](docs/use-cases.md#caso-de-uso-1-crear-cliente)
* **Caso de Uso 2:** [Caso 2 – Crear solicitud correctamente](docs/use-cases.md#caso-de-uso-2-crear-solicitud-correctamente)
* **Caso de Uso 3:** [Caso 3 – Crear solicitud con cliente inexistente](docs/use-cases.md#caso-de-uso-3-crear-solicitud-con-cliente-inexistente)
* **Caso de Uso 4:** [Caso 4 – Asignar técnico](docs/use-cases.md#caso-de-uso-4-asignar-tecnico)
* **Caso de Uso 5:** [Caso 5 – Intentar cerrar sin estar en proceso](docs/use-cases.md#caso-de-uso-5-intentar-cerrar-sin-estar-en-proceso)
* **Caso de Uso 6:** [Caso 6 – Cerrar solicitud correctamente](docs/use-cases.md#caso-de-uso-6-cerrar-solicitud-correctamente)
* **Caso de Uso 7:** [Caso 7 – Reabrir solicitud](docs/use-cases.md#caso-de-uso-7-reabrir-solicitud)

### Pipelines e Infraestructura (Enlaces Externos)
* **Integración Continua:** [Ver ejecuciones del Pipeline en GitHub Actions](https://github.com/franciscorrego/mgcss-track-L2-Grupo3/actions)
* **Calidad de Código:** [Explorar el Dashboard de Métricas y Deuda Técnica en SonarCloud](https://sonarcloud.io/)
* **Contrato de la API:** [Consultar la Especificación OpenAPI / Swagger Local](http://localhost:8080/swagger-ui/index.html)

---

## Restricciones Arquitectónicas y Diseño Limpio

El proyecto implementa una arquitectura desacoplada y limpia dividida en cuatro capas conceptuales de responsabilidad única, garantizando que el dominio permanezca agnóstico a las bases de datos y frameworks web:

* **`com.mgcss.domain` (Capa de Dominio):** Contiene las entidades (`Cliente`, `Tecnico`, `Solicitud`), enums (`Estado`, `TipoCliente`) e interfaces de repositorio (`SolicitudRepository`, etc.). Es código puramente orientado a objetos, auto-contenido y libre de lógica de persistencia o dependencias web.
* **`com.mgcss.services` (Capa de Aplicación):** Orquesta los flujos de negocio (`SolicitudService`, `ClienteService`, `TecnicoService`). Recupera entidades de los repositorios, invoca sus métodos de intención semántica y coordina las transacciones.
* **`com.mgcss.infrastructure` (Capa de Persistencia / Datos):** Implementa el almacenamiento físico mediante repositorios de Spring Data JPA sobre una base de datos relacional y gestiona las transacciones de base de datos.
* **`com.mgcss.api` (Capa de Exposición REST):** Actúa como el adaptador de entrada del sistema. Contiene los controladores REST (`SolicitudController`, etc.), los DTOs y el **`GlobalExceptionHandler`**, encargado de interceptar excepciones de negocio y transformarlas en respuestas HTTP estructuadas y consistentes para el cliente.

### Aislamiento y Contrato Externo (DTOs vs Entidades)
Para cumplir los requerimientos de la asignatura, las entidades de dominio quedan estrictamente confinadas intramuros. Toda comunicación hacia o desde el exterior se realiza mediante objetos planos de transferencia de datos (**DTOs**). 
* **Validación Temprana:** Los `RequestDTO` utilizan anotaciones de *Jakarta Validation* (`@NotBlank`, `@NotNull`, `@Email`) para rechazar peticiones malformadas en la frontera de la API, impidiendo que datos corruptos pisen la lógica de aplicación.
* **Documentación Semántica:** Toda la capa de transferencia está auto-documentada mediante anotaciones OpenAPI v3 (`@Schema`), enriqueciendo la metadata expuesta en la interfaz de usuario interactiva de Swagger.

---

## Catálogo de Endpoints de la API REST

Todos los endpoints raíz operan bajo el prefijo universal `/api`. A continuación, se detallan los contratos reales del sistema y su comportamiento frente a excepciones (controladas de forma centralizada por el `GlobalExceptionHandler`):

### Gestión de Solicitudes Core (`/api/solicitudes`)
| Método | Endpoint | Cuerpo Petición / Parámetros | HTTP Success | HTTP Errores Controlados (Mapeados) | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/solicitudes` | `SolicitudRequestDTO` (JSON) | `201 Created` | `400 Bad Request` (Datos inválidos o cuota superada)<br>`404 Not Found` (Cliente inexistente) | **[MÍNIMO OBLIGATORIO]** Registra una nueva incidencia en estado ABIERTA vinculada a un cliente. |
| **GET** | `/api/solicitudes/{id}` | Ninguno (ID en Path) | `200 OK` | `404 Not Found` (ID de solicitud no registrado) | **[MÍNIMO OBLIGATORIO]** Recupera los detalles completos y el estado actual de una solicitud específica. |
| **GET** | `/api/solicitudes` | Ninguno | `200 OK` | `500 Internal Server Error` | **[MÍNIMO OBLIGATORIO]** Retorna la colección completa con el histórico de todas las solicitudes del sistema. |
| **PUT** | `/api/solicitudes/{id}/tecnico` | `tecnicoId` (Query Param) | `204 No Content` | `400 Bad Request` (Técnico inactivo o solicitud cerrada)<br>`404 Not Found` (Solicitud o Técnico inexistente) | **[MÍNIMO OBLIGATORIO]** Asigna un operario técnico a la incidencia. Cambia el estado automáticamente a EN_PROCESO. |
| **PUT** | `/api/solicitudes/{id}/cerrar` | Ninguno (ID en Path) | `204 No Content` | `400 Bad Request` (Intento ilegal si no está EN_PROCESO)<br>`404 Not Found` (Solicitud inexistente) | **[MÍNIMO OBLIGATORIO - CAMBIO ESTADO]** Finaliza el ciclo de trabajo de una incidencia, liberando la carga del operario. |
| **PATCH**| `/api/solicitudes/{id}/reabrir` | Ninguno (ID en Path) | `204 No Content` | `400 Bad Request` (No estaba CERRADA o viola reglas de cliente)<br>`404 Not Found` (Solicitud inexistente) | **[MÍNIMO OBLIGATORIO]** Resuelve la reapertura de una incidencia cerrada, devolviéndola al estado EN_PROCESO. |

### Gestión de Clientes (`/api/clientes`)
| Método | Endpoint | Cuerpo Petición | HTTP Success | HTTP Errores Controlados | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/clientes` | `ClienteRequestDTO` (JSON) | `201 Created` | `400 Bad Request` (Formato de email inválido o campos vacíos) | Registra un nuevo cliente con tipo inicial STANDARD. |
| **PUT** | `/api/clientes/{id}/desactivar` | Ninguno (ID en Path) | `204 No Content` | `400 Bad Request` (Cliente con solicitudes abiertas activas)<br>`404 Not Found` (Cliente inexistente) | Desactiva un cliente del sistema aplicando las invariantes del dominio. |

### Gestión de Técnicos (`/api/tecnicos`)
| Método | Endpoint | Cuerpo Petición | HTTP Success | HTTP Errores Controlados | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/tecnicos` | `TecnicoRequestDTO` (JSON) | `201 Created` | `400 Bad Request` (Datos de entrada incorrectos o inválidos) | Introduce un nuevo técnico al pool de operarios de soporte. |
| **PUT** | `/api/tecnicos/{id}/desactivar` | Ninguno (ID en Path) | `204 No Content` | `400 Bad Request` (Técnico con tareas activas asignadas)<br>`404 Not Found` (Técnico inexistente) | Cambia el estado del operario a inactivo para restringir nuevas asignaciones. |

---

## Tecnologías y Requisitos Previos
* **Java Development Kit (JDK):** Versión 17+.
* **Apache Maven:** Versión 3.8+ (o uso del wrapper `.\mvnw`).
* **Docker / Docker Desktop:** Construcción y ejecución del contenedor runtime.
* **Base de Datos:** H2 Database (entorno en memoria para desarrollo/testing).

---

## Instalación y Ejecución
### 1. Clonar y Acceder
```bash
git clone [https://github.com/franciscorrego/mgcss-track-L2-Grupo3.git](https://github.com/franciscorrego/mgcss-track-L2-Grupo3.git)
cd mgcss-track-L2-Grupo3/Proyecto_Mantenimiento
