FROM openjdk:21-jdk-slim
ARG JAR_FILE=build/libs/helios-sentinel.jar
COPY ${JAR_FILE} helios-sentinel.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "helios-sentinel.jar"]
