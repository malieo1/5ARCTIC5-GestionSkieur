# Start from a lightweight Java runtime image
FROM openjdk:11-jre-slim

# Define environment variables for Nexus credentials and JAR location
ARG NEXUS_URL="http://192.168.33.10:8081/repository/maven-releases"
ARG GROUP_ID="tn/esprit/spring"
ARG ARTIFACT_ID="gestion-station-ski"
ARG VERSION="1.0"
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD

# Construct JAR download URL
ENV JAR_URL="${NEXUS_URL}/${GROUP_ID}/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${VERSION}.jar"

# Set the directory for the app
WORKDIR /app

# Download the JAR from Nexus and save it to the app directory
RUN apt-get update && \
    apt-get install -y curl && \
    curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -o ${ARTIFACT_ID}.jar ${JAR_URL} && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Expose port (if applicable)
EXPOSE 8082

# Command to run the JAR
CMD ["java", "-jar", "gestion-station-ski.jar"]
