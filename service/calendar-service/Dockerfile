FROM openjdk:21-slim
VOLUME /tmp
RUN apt-get update && apt-get install -y curl
COPY ./build/libs/*.jar calendar-service.jar
ENTRYPOINT ["java", "-jar", "/calendar-service.jar"]