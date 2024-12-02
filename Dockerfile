# Base image with Java 21 support
FROM eclipse-temurin:21-jdk AS build

# Set working directory in the container
WORKDIR /app

# Copy project files into the container
COPY . .

# Ensure gradlew has execute permissions
RUN chmod +x gradlew

# Run Gradle build to generate JAR file
RUN ./gradlew build -x test --no-daemon

# Use a lightweight image for running the application
FROM eclipse-temurin:21-jre

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Command to run the application
CMD ["java", "-jar", "app.jar"]
