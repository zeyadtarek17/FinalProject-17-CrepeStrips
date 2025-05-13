FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

COPY target/fooditem-service-0.0.1-SNAPSHOT.jar fooditem-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","fooditem-service-0.0.1-SNAPSHOT.jar"]