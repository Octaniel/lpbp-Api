FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

FROM openjdk:11-jdk-slim
EXPOSE 8080
COPY --from=build /target/igf-api.jar igf-api.jar

ENTRYPOINT ["java", "-jar", "lpbp-api.jar"]
