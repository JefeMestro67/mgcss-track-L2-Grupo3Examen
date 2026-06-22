# Informe de limitaciones del entorno de pruebas

Este proyecto se desarrolla en un fork individual para la evaluación de la asignatura MGCSS. Debido a la naturaleza del entorno (repositorio con un único miembro), existen las siguientes limitaciones técnicas y operativas:

1. **Revisión por pares (Peer Review):** La política de protección de ramas requiere que las Pull Requests sean aprobadas por una persona con acceso de escritura. Al ser el único integrante del repositorio, no es posible contar con una segunda persona para realizar una revisión de código manual. Se ha suplido esta limitación validando los cambios mediante la ejecución exitosa de los checks automáticos (CI/CD y análisis estático).

2. **Protección de ramas:** Se han aplicado las reglas de protección estándar para impedir push directos a la rama `main` y obligar al uso de Pull Requests, asegurando que el pipeline se ejecute siempre antes de la integración.

3. **Gestión de secretos:** La integración con SonarCloud utiliza un secreto de repositorio (SONAR_TOKEN) gestionado individualmente, asegurando que el análisis de calidad se realice correctamente.
