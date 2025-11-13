FROM openjdk:21-slim
VOLUME /tmp
RUN apt-get update && apt-get install -y curl
COPY ./build/libs/*.jar discovery.jar
ENTRYPOINT ["java", "-jar", "/discovery.jar"]

