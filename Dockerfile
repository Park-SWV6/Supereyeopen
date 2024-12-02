# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jdk

# Set working directory in the container
WORKDIR /app

# Copy Gradle wrapper and source code
COPY . .

# Run Gradle build to generate JAR file
RUN ./gradlew build -x test --no-daemon

# Copy the generated JAR file to the container
RUN cp build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Start the application
CMD ["java", "-jar", "app.jar"]
