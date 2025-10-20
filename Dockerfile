# ===========================
# 🧱 Stage 1: Build
# ===========================
FROM maven:3.9.8-eclipse-temurin-17 AS builder

# Workdir
WORKDIR /build

# Copy root POM and the module POMs first to leverage Docker layer caching.
COPY pom.xml ./
COPY domain/pom.xml ./domain/
COPY shopapp/pom.xml ./shopapp/

# Copy sources for all modules
COPY domain ./domain/
COPY shopapp ./shopapp/

# Build the whole multi-module project (skip tests compile/run for faster build)
# Use -Dmaven.test.skip=true to skip test compilation as well (fixes missing test deps)
RUN mvn -B -f ./pom.xml clean package -Dmaven.test.skip=true


# ===========================
# 🚀 Stage 2: Run
# ===========================
# Use a Debian-based Temurin JRE to avoid potential Alpine/glibc compatibility issues
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the Spring Boot runnable jar from the built shopapp module
COPY --from=builder /build/shopapp/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
