# Use the official Maven image to create a build stage with Java 17
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the working directory
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use the official OpenJDK image as the base image with Java 17
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/gym_app.api-1.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
