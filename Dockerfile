FROM openjdk:11
LABEL authors="Josue Nsumba"

COPY target/*.jar api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/api.jar"]