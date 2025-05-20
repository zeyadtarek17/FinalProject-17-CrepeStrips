FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

COPY target/user-service-0.0.1-SNAPSHOT.jar user-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","user-service-0.0.1-SNAPSHOT.jar"]