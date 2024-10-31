
FROM openjdk:17
EXPOSE 8082

RUN curl -o gestion-station-ski-0.0.1.jar -L "http://192.168.33.10:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/0.0.1/kaddem-0.0.1.jar"

ENTRYPOINT ["java", "-jar", "kaddem-0.0.1.jar"]
