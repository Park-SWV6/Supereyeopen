# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jdk

# Set working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/*.jar app.jar

# Expose the application port (default Spring Boot port)
EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "app.jar"]
