FROM openjdk:11
MAINTAINER Jean Amadeu
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar /app.jar