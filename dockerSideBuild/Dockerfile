FROM amazoncorretto:17-alpine-jdk

WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test
ENTRYPOINT ["java", "-jar", "./build/libs/finitas-frontend-api-all.jar"]
