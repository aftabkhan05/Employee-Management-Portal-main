# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies first (re-used unless pom.xml changes)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Build the jar (tests skipped: they need a live MySQL database)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Run as a non-root user
RUN useradd --create-home --shell /bin/bash appuser
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /home/appuser/employee-images && chown -R appuser:appuser /app /home/appuser
USER appuser

# Render sets PORT at runtime; Spring reads it via server.port=${PORT:8080}
EXPOSE 8080

# Tune the JVM for small containers (Render free tier = 512 MB)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
