# SpringBoot RestAPI Task

This project is a simple RESTful API. Built using Spring Boot that exposes a set of endpoints for managing a user database.

## Run the application using Docker

- Run `mvn clean package` in the root folder
- Run `docker-compose up -d` in the root folder
- The application should run at `locahost:8080`
- You can use technologies like curl or postman to call the endpoints
 
## You can also run the application without Docker

- Run `mvn clean package` in the root folder
- Run `mvn springBoot:run` in the root folder

## Improvement Items

- Add Security (Authentication and Authorization). Maybe Oauth2 with JWT token
- Add pagination
- Add a simple UI