# Create jar
FROM maven:3.8.3-openjdk-11-slim AS builder
WORKDIR /app
COPY . /app
ARG mvn_arg="clean package -DskipTests"

RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml $mvn_arg

# Run jar
FROM openjdk:11-jre-slim
ARG JAR_FILE=/app/book-app/target/book-app-*.jar
COPY --from=builder $JAR_FILE /opt/bookapp/app.jar
WORKDIR /opt/bookapp/

RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=local", "app.jar"]
