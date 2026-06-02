# MGCSS - Especificación de Casos de Uso y Guía de Pruebas API

Este documento contiene la especificación funcional y el catálogo de escenarios de prueba para validar el ciclo de vida completo del sistema. Todos los casos expuestos corresponden a los endpoints reales de la aplicación y pueden ejecutarse de forma interactiva a través de Swagger UI (/swagger-ui/index.html).

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

### Reglas de negocio
- El email debe ser válido
- El cliente se crea activo por defecto

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

### Reglas de negocio
- El cliente debe existir
- Estado inicial siempre ABIERTA

### Cómo probarlo en Swagger
1. Crear cliente primero
2. Usar su ID
3. Ejecutar POST /api/solicitudes
4. Ver 201

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
`400 BAD REQUEST` o `404 NOT FOUND`
```json
{
  "message": "El cliente no existe"
}
```

### Reglas de negocio
- No se puede crear solicitud sin cliente válido

---

## Caso 4 - Asignar tecnico a la solicitud

### Request
```http
PUT /api/solicitudes/1/tecnico
```
```json
1
```

### Response esperado
`200 OK`
```json
{
  "id": 1,
  "estado": "EN_PROCESO",
  "tecnicoAsignado": "1"
}
```

### Reglas de negocio
- El técnico asignado debe estar activo previamente

---

## Caso 5 - Intentar cerrar sin estar en proceso

### Request
```http
PUT /api/solicitudes/1/cerrar
```

### Response esperado
`400 BAD REQUEST`
```text
IllegalStateException: No se puede cerrar si no está EN_PROCESO
```

### Reglas de negocio
- Refuerza integridad del dominio

---

## Caso 6 - Cerrar solicitud correctamente

### Request
```http
PUT /api/solicitudes/1/cerrar
```

### Response esperado
`204 NO CONTENT`

### Reglas de negocio
- La solicitud debe estar en estado EN_PROCESO para poder cerrarse
- Libera de forma automática la carga de trabajo asignada al técnico operario

---

## Caso 7 - Reabrir solicitud

### Request
```http
PATCH /api/solicitudes/1/reabrir
```

### Response esperado
`204 NO CONTENT`

### Reglas de negocio
- La solicitud debe encontrarse en estado CERRADA previamente
- Al reabrirse de forma manual, vuelve de nuevo al estado EN_PROCESO
