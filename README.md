# mgcss-track-L2-Grupo3

## Ejecución del proyecto
Para compilar y verificar el proyecto, navega a la carpeta `Proyecto_Mantenimiento` y ejecuta:
`.\mvnw clean verify`

## Estrategia de Ramas
* **main**: Versión estable y protegida.
* **feature/*** : Ramas de desarrollo para nuevas funcionalidades.  

## Despliegue con Docker
Para construir y ejecutar la aplicación en un contenedor, usa los siguientes comandos:
1. Construir la imagen: `docker build -t mgcss-track .`
2. Ejecutar el contenedor: `docker run -d -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod mgcss-track`
