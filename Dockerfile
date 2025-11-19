# Use JDK 21 official image
FROM eclipse-temurin:21-jdk

# Set working directory inside the container
WORKDIR /app

# Copy all project files
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/attendance-0.0.1-SNAPSHOT.jar"]
