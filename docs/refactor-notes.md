# Notas de Refactorización (Sesión 8)

## 1. Problema identificado
SonarCloud ha detectado 2 Code Smells. El más grave (Critical) se encuentra en `TecnicoService.java` por duplicación de un literal ("El técnico no existe" repetido 3 veces). También existe un archivo de paquete sin uso (`package-info.java`).

## 2. Métrica asociada
2 Code Smells (1 Crítico, 1 Menor) y un total de 10 minutos de Deuda Técnica.

## 3. Riesgo potencial si no se corrige
La duplicación de literales (Magic Strings) dificulta la mantenibilidad: si se requiere cambiar el mensaje de error, habría que buscar y modificar el código en múltiples sitios, aumentando el riesgo de inconsistencias. Los archivos sin uso ensucian el proyecto.