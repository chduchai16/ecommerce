# Multi-stage build for Spring Boot application

# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy parent pom first for better layer caching
COPY pom.xml .

# Copy module poms
COPY domain/pom.xml domain/
COPY shopapp/pom.xml shopapp/

# Download dependencies (this layer will be cached if poms don't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY domain/src domain/src
COPY shopapp/src shopapp/src

# Build the application (skip test compilation and execution)
RUN mvn clean package -Dmaven.test.skip=true -B

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Add metadata
LABEL maintainer="ecommerce-app"
LABEL version="1.0"

# Create app user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /app/shopapp/target/*.jar app.jar

# Change ownership to app user
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose application port
EXPOSE 8080

# Set JVM options for container environment
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
