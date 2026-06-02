# MGCSS - Especificación de Casos de Uso y Guía de Pruebas API

Este documento contiene la especificación funcional y el catálogo de escenarios de prueba para validar el ciclo de vida completo del sistema. Todos los casos expuestos corresponden a los endpoints reales de la aplicación y están alineados con la lógica del `GlobalExceptionHandler` (404 para recursos no encontrados con formato JSON, 400 para violaciones de reglas de negocio e incorporando respuestas 204 No Content cuando corresponde).

---

## Caso 1 - Crear cliente

### Request
```http
POST /api/clientes
```
```json
{
  "nombre": "Juan Perez",
  "email": "juan.perez@mgcss.com"
}
```

### Response esperado
`201 CREATED`
```json
{
  "id": 1,
  "nombre": "Juan Perez",
  "email": "juan.perez@mgcss.com",
  "tipoCliente": "STANDARD"
}
```

---

## Caso 2 - Crear solicitud correctamente

### Request
```http
POST /api/solicitudes
```
```json
{
  "clienteId": 1,
  "descripcion": "Incidencia en servidor"
}
```

### Response esperado
`201 CREATED`
```json
{
  "id": 1,
  "estado": "ABIERTA"
}
```

---

## Caso 3 - Crear solicitud con cliente inexistente

### Request
```http
POST /api/solicitudes
```
```json
{
  "clienteId": 999,
  "descripcion": "Error de prueba"
}
```

### Response esperado
`404 NOT FOUND`
```json
{
  "message": "El cliente proporcionado no existe en el sistema"
}
```

---

## Caso 4 - Asignar tecnico a la solicitud

### Request
```http
PUT /api/solicitudes/1/tecnico?tecnicoId=1
```

### Response esperado
`204 NO CONTENT`

---

## Caso 5 - Intentar cerrar sin estar en proceso

### Request
```http
PUT /api/solicitudes/1/cerrar
```

### Response esperado
`400 BAD REQUEST`
```json
{
  "message": "Intento de cierre ilegal desde un estado no permitido (ej. ABIERTA)"
}
```

---

## Caso 6 - Cerrar solicitud correctamente

### Request
```http
PUT /api/solicitudes/1/cerrar
```

### Response esperado
`204 NO CONTENT`

---

## Caso 7 - Reabrir solicitud

### Request
```http
PATCH /api/solicitudes/1/reabrir
```

### Response esperado
`204 NO CONTENT`
