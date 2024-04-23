FROM adoptopenjdk/openjdk11:ubi

EXPOSE 8080

ADD target/lpbp-api.jar lpbp-api.jar

ENTRYPOINT ["java", "-jar", "lpbp-api.jar"]
