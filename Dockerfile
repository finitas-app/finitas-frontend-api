FROM amazoncorretto:17-alpine-jdk

VOLUME /tmp
COPY ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Dprofile=docker", "-jar", "/app.jar"]
