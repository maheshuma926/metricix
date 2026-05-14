# --- Stage 1: Build the JAR ---
FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom first to maximize Docker layer caching for dependencies
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy source and package (skip tests for image build speed)
COPY src src
RUN mvn -B -q -DskipTests package

# --- Stage 2: The Minimal Production Image ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy ONLY the built JAR from the builder stage
COPY --from=builder /app/target/*.jar metricix.jar

# Expose the port Netty will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "metricix.jar"]