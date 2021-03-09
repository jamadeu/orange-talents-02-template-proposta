FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV DB_URL=jdbc:mysql://localhost/proposta?createDatabaseIfNotExist=true&useTimezone=true&serverTimezone=UTC
ENV DB_USERNAME=root
ENV DB_PASSWORD=root
ENV DB_PLATFORM=org.hibernate.dialect.MySQL8Dialect
ENV JPA_DATABASE=POSTGRESQL
ENV DB_DRIVER=com.mysql.cj.jdbc.Driver
ENV API_ANALISE=http://analise:9999
ENV API_CARTOES=http://contas:8888
ENTRYPOINT java -jar /app.jar