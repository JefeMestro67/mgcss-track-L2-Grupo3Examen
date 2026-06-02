# MGCSS - Especificación de Casos de Uso y Guía de Pruebas API

Este documento contiene la especificación funcional y el catálogo de escenarios de prueba para validar el ciclo de vida completo del sistema. Todos los casos expuestos corresponden a los endpoints reales de la aplicación y pueden ejecutarse de forma interactiva a través de Swagger UI (/swagger-ui/index.html).

---

## Caso de Uso 1: Crear cliente

Registro de una nueva entidad cliente en el sistema para permitirle la apertura posterior de incidencias.

* **Acción (Request):**
  ```http
  POST /api/clientes
  Content-Type: application/json
  ```
  ```json
  {
    "nombre": "Cliente Standard S.A.",
    "email": "soporte@clientestandard.com"
  }
  ```
* **Resultado esperado (Response):**
  ```http
  HTTP/1.1 201 Created
  Content-Type: application/json
  ```
  ```json
  {
    "id": 1,
    "nombre": "Cliente Standard S.A.",
    "email": "soporte@clientestandard.com",
    "tipoCliente": "STANDARD"
  }
  ```

---

## Caso de Uso 2: Crear solicitud correctamente

Validación del flujo de alta de una incidencia asociada a un cliente activo que dispone de cuota.

* **Precondición:** Existe el cliente con ID 1 en estado activo.
* **Acción (Request):**
  ```http
  POST /api/solicitudes
  Content-Type: application/json
  ```
  ```json
  {
    "clienteId": 1,
    "descripcion": "Error en el módulo de pasarela de pago al procesar transacciones."
  }
  ```
* **Resultado esperado (Response):**
  ```http
  HTTP/1.1 201 Created
  Content-Type: application/json
  ```
  ```json
  {
    "id": 1,
    "descripcion": "Error en el módulo de pasarela de pago al procesar transacciones.",
    "estado": "ABIERTA"
  }
  ```

---

## Caso de Uso 3: Crear solicitud con cliente inexistente

Garantiza que la API rechaza peticiones vinculadas a identidades no registradas, protegiendo las invariantes del dominio.

* **Precondición:** El ID de cliente proporcionado no existe en la base de datos.
* **Acción (Request):**
  ```http
  POST /api/solicitudes
  Content-Type: application/json
  ```
  ```json
  {
    "clienteId": 999,
    "descripcion": "Petición de mantenimiento preventivo."
  }
  ```
* **Resultado esperado (Response / Error):**
  ```http
  HTTP/1.1 404 Not Found
  Content-Type: application/json
  ```
  ```json
  {
    "message": "El cliente proporcionado no existe en el sistema"
  }
  ```

---

## Caso de Uso 4: Asignar técnico

Vinculación de un operario técnico a una incidencia abierta, provocando el cambio automático de estado.

* **Precondición:** Existe una solicitud activa con ID 1 en estado ABIERTA y un técnico registrado con ID 2.
* **Acción (Request):**
  ```http
  PUT /api/solicitudes/1/tecnico?tecnicoId=2
  ```
* **Resultado esperado (Response):**
  ```http
  HTTP/1.1 204 No Content
  ```
> **Efecto en el dominio:** El estado de la solicitud transmuta internamente a `EN_PROCESO` de forma automatizada.

---

## Caso de Uso 5: Intentar cerrar sin estar en proceso

Verificación de la seguridad del modelo frente a transiciones de estado ilegales.

* **Precondición:** La solicitud con ID 1 se encuentra en estado ABIERTA (no ha sido asignada previamente a ningún técnico).
* **Acción (Request):**
  ```http
  PUT /api/solicitudes/1/cerrar
  ```
* **Resultado esperado (Response / Error):**
  ```http
  HTTP/1.1 400 Bad Request
  Content-Type: application/json
  ```
  ```json
  {
    "message": "La solicitud debe estar EN_PROCESO para poder ser cerrada"
  }
  ```

---

## Caso de Uso 6: Cerrar solicitud correctamente

Finalización del ciclo de trabajo de una incidencia de soporte técnico.

* **Precondición:** La solicitud con ID 1 se encuentra en estado EN_PROCESO (tiene un técnico asignado).
* **Acción (Request):**
  ```http
  PUT /api/solicitudes/1/cerrar
  ```
* **Resultado esperado (Response):**
  ```http
  HTTP/1.1 204 No Content
  ```
> **Efecto en el dominio:** El estado de la solicitud pasa a `CERRADA`, liberando la carga del operario técnico.

---

## Caso de Uso 7: Reabrir solicitud

Validación del flujo de reapertura de una incidencia cerrada que requiere revisión adicional.

* **Precondición:** La solicitud con ID 1 se encuentra en estado CERRADA.
* **Acción (Request):**
  ```http
  PATCH /api/solicitudes/1/reabrir
  ```
* **Resultado esperado (Response):**
  ```http
  HTTP/1.1 204 No Content
  ```
> **Efecto en el dominio:** La solicitud vuelve al estado `EN_PROCESO`.## Caso 1 – Crear cliente

### Request
POST /api/clientes

{
  "nombre": "Juan Perez",
  "email": "juan.perez@mgcss.com"
}

### Response esperado
201 CREATED

{
  "id": 1,
  "nombre": "Juan Perez",
  "email": "juan.perez@mgcss.com",
  "tipoCliente": "STANDARD"
}

### Reglas de negocio
- El email debe ser válido
- El cliente se crea activo por defecto

## Caso 2 – Crear solicitud correctamente

### Request
POST /api/solicitudes

{
  "clienteId": 1,
  "descripcion": "Incidencia en servidor"
}

### Response esperado
201 CREATED

{
  "id": 1,
  "estado": "ABIERTA"
}

### Reglas de negocio
- El cliente debe existir
- Estado inicial siempre ABIERTA

### Cómo probarlo en Swagger
1. Crear cliente primero
2. Usar su ID
3. Ejecutar POST /api/solicitudes
4. Ver 201

## Caso 3 – Crear solicitud con cliente inexistente

### Request
POST /api/solicitudes

{
  "clienteId": 999,
  "descripcion": "Error de prueba"
}

### Response esperado
400 BAD REQUEST o 404 NOT FOUND

{
  "message": "El cliente no existe"
}

### Reglas de negocio
- No se puede crear solicitud sin cliente válido

## Caso 5 – Intentar cerrar sin estar en proceso

### Request
PUT /api/solicitudes/1/cerrar

### Response esperado
400 BAD REQUEST

IllegalStateException: No se puede cerrar si no está EN_PROCESO

### Reglas de negocio
- Refuerza integridad del dominio

