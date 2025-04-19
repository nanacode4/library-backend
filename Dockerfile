# Using lightweight OpenJDK images
FROM openjdk:17-jdk-slim

# Copy the JAR file into the image
COPY target/spring-boot-library-0.0.1-SNAPSHOT.jar app.jar

# Set the container startup command
ENTRYPOINT ["java", "-jar", "/app.jar"]
