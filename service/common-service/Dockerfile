FROM openjdk:21-slim
VOLUME /tmp
RUN apt-get update && apt-get install -y curl
COPY ./build/libs/*.jar common-service.jar
ENTRYPOINT ["java", "-jar", "/common-service.jar"]