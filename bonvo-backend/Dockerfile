# ── Build stage ────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# ── Runtime stage ───────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S bonvo && adduser -S bonvo -G bonvo
COPY --from=build /app/target/*.jar app.jar
RUN chown bonvo:bonvo app.jar

USER bonvo
EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Xms256m", "-Xmx512m", \
  "-jar", "app.jar"]
