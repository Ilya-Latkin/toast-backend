FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/toast-src/toast-backend/src
COPY pom.xml /home/toast-src/toast-backend
RUN mvn -f /home/toast-src/toast-backend/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/toast-src/toast-backend/target/Toast-1.0-SNAPSHOT.jar /home/toast-app/Toast-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/toast-app/Toast-1.0-SNAPSHOT.jar"]