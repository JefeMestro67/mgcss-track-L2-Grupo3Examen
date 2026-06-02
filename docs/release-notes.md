# MGCSS - Notas de Lanzamiento (Release Notes) - Versión v1.1.0

Este documento contiene la justificación analítica del número de versión asignado para el actual ciclo de entrega, siguiendo las directrices de la disciplina de Versionado Semántico (SemVer) y el protocolo de la asignatura.

---

## 1. Justificación de la Versión: v1.1.0 (MINOR)

Para determinar el identificador de la nueva versión del sistema, el equipo mgcss-track-L2-Grupo3 ha analizado los commits integrados desde la publicación base (v1.0.0) bajo el estándar de MAJOR.MINOR.PATCH:

### ¿Por qué NO es un incremento PATCH (v1.0.1)?

Un incremento en el dígito de PATCH se reserva exclusivamente para la corrección de errores (hotfixes o bugs) que no aportan nuevo valor funcional al sistema. En esta iteración no nos hemos limitado a corregir fallos técnicos, sino que se ha incorporado infraestructura automatizada de despliegue y una reestructuración de la especificación técnica de la API. Por lo tanto, asignar la versión 1.0.1 infravaloraría el impacto del cambio.

### ¿Por qué SÍ es un incremento MINOR (v1.1.0)?

La norma SemVer indica que el dígito MINOR debe incrementarse cuando se añaden nuevas funcionalidades o mejoras que son 100% compatibles hacia atrás (sin romper el contrato de la API existente).

En este lanzamiento, el sistema incorpora:

* **1. Pipeline de Entrega Continua (Continuous Delivery):** Automatización del proceso de compilación, control de calidad y empaquetado mediante GitHub Actions.
* **2. Alignment Funcional:** Sincronización estricta de los controladores REST con los Casos de Uso del 1 al 7, adaptando las respuestas lógicas de negocio al estándar HTTP y a nuestro GlobalExceptionHandler.
* **3. Containerización Dinámica:** Preparación para el despliegue mediante el etiquetado dinámico de imágenes Docker usando variables del sistema.

Ninguna de estas adiciones rompe el comportamiento que los clientes existentes esperaban del backend. Todos los endpoints (`/api/solicitudes`, `/api/clientes`, `/api/tecnicos`) siguen respondiendo con las mismas firmas lógicas.

### ¿Por qué NO es un incremento MAJOR (v2.0.0)?

Un cambio de tipo MAJOR se realiza únicamente cuando se introducen modificaciones incompatibles con las versiones anteriores (breaking changes) que obligarían a los clientes a reescribir su código de integración. Al haber mantenido intacta la arquitectura de capas, el modelo de dominio y las invariantes lógicas del negocio sin romper ningún contrato de comunicación externa, queda descartada la versión 2.0.0.

---

