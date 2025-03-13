Management App

Este proyecto es una aplicación de gestión de eventos desarrollada con Spring Boot, MySQL y Docker. Permite la creación, actualización y administración de eventos, así como el registro de usuarios.

Tecnologías Utilizadas

Java 17 + Spring Boot

MySQL 8

Hibernate (JPA)

Docker & Docker Compose

Maven

Configuración del Proyecto

Clonar el Repositorio

git clone git remote add origin https://github.com/marlonlopezt/event-management.git
cd management-app

Construcción del Proyecto

mvn clean package

Generará un archivo JAR en target/management-0.0.1-SNAPSHOT.jar.

Levantar la Aplicación con Docker

Para levantar la base de datos y la aplicación, ejecuta:

docker-compose up -d

Esto iniciará dos contenedores:

mysql-management (Base de datos MySQL)

management-app (Aplicación Spring Boot)

Detener los Contenedores

Para detener los contenedores sin eliminarlos:

docker-compose stop

Para eliminarlos completamente:

docker-compose down

Si quieres borrar los volúmenes (datos de la BD):

docker-compose down -v

Endpoints de la API

Método

Endpoint

Descripción

POST

/events

Crear un nuevo evento

GET

/events

Obtener todos los eventos

GET

/events/{id}

Obtener un evento por ID

PUT

/events/{id}

Actualizar un evento

POST

/events/{id}/register

Registrar usuario en un evento

GET

/events/user/{userId}

Obtener eventos de un usuario

Variables de Entorno

En docker-compose.yml, se configuran:

SPRING_APPLICATION_NAME: management
SPRING_DATASOURCE_URL: jdbc:mysql://mysql-db:3306/managementdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: 12345
SPRING_JPA_HIBERNATE_DDL_AUTO: update
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.hibernate.dialect.MySQLDialect
SERVER_PORT: 8787

Si desea cambiar el puerto de la app, modifica SERVER_PORT y actualiza docker-compose.yml.

Comandos Útiles

Ver logs de la aplicación: docker-compose logs -f management-app

Acceder a MySQL dentro del contenedor:

docker exec -it mysql-management mysql -u root -p

Reconstruir la imagen después de cambios:

docker-compose down
docker-compose build
docker-compose up -d

