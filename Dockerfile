# Start with an OpenJDK 11 base image
FROM openjdk:11-jre-slim

# Define build arguments for Nexus credentials
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD
ARG VERSION

# Set the working directory
WORKDIR /app

# Download the JAR from Nexus using credentials and version
RUN apt-get update && \
    apt-get install -y curl && \
    curl -u $NEXUS_USERNAME:$NEXUS_PASSWORD -o app.jar "http://192.168.33.10:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/${VERSION}/gestion-station-ski-${VERSION}.jar"

# Expose the application's port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
