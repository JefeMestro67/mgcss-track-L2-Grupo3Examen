## Caso 1 – Crear cliente

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

