FROM openjdk:21-slim
VOLUME /tmp
RUN apt-get update && apt-get install -y curl
COPY ./build/libs/*.jar soccer-service.jar
ENTRYPOINT ["java", "-jar", "/soccer-service.jar"]