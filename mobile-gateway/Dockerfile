# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

ENV JAVA_OPTS="-XX:-UsePerfData"
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-XX:-UsePerfData"

COPY build.gradle.kts settings.gradle.kts ./
COPY src src

RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
