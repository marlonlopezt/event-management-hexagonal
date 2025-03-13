FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8787
ENTRYPOINT ["java", "-jar", "app.jar"]
