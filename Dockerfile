# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/Sudoku-0.0.1-SNAPSHOT.jar /app/sudoku-solver.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "sudoku-solver.jar"]

# Expose the port the app runs on
EXPOSE 8080
