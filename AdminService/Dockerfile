FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

COPY target/admin-service-0.0.1-SNAPSHOT.jar admin-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","admin-service-0.0.1-SNAPSHOT.jar"]