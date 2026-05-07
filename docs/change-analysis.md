# Análisis de Impacto - CR: Reopen and History

1.¿Qué métodos del dominio se ven afectados?
Principalmente la entidad `Solicitud`. Se requiere un nuevo método `reabrir()` y modificar la lógica de transición de estados para actualizar el histórico.

2.¿Qué reglas actuales cambian?
Cambia la regla de que el estado `CERRADA` es final e inmutable. Ahora se permite la transición de `CERRADA` a `EN_PROCESO`.

3.¿Qué tests deberían romparse?
Los tests unitarios de `Solicitud` que verifiquen que una solicitud cerrada no puede ser modificada. Estos deberán ajustarse a la nueva realidad del negocio.

4.¿Qué parte del modelo debe extenderse?
La clase `Solicitud` necesita una estructura interna (Lista) para almacenar objetos de tipo `EstadoChange` (histórico).

5.¿Qué impacto tiene en persistencia?
Es necesario mapear la nueva relación del histórico en la base de datos (JPA), probablemente mediante una tabla de unión o una colección de elementos.