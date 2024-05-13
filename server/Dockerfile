FROM gradle:jdk17 as build
WORKDIR /app
COPY . .
RUN gradle clean build

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/build/libs/EnglishMasteryHub-1.0.0.jar app.jar
ENTRYPOINT [ "java", "-Xms1024m", "-Xmx2048m", "-Dspring.profiles.active=docker", "-jar", "app.jar" ]