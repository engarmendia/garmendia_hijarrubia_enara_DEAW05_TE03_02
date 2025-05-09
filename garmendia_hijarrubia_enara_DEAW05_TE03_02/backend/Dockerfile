# Fase de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Fase de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application-docker.properties application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=application.properties"]