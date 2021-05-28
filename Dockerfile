
FROM maven:3.6.1-openjdk-15.0.2 AS build
COPY src /home/toast-backend-src/src
COPY pom.xml /home/toast-backend-scr
RUN mvn -f /home/toast-backend/pom.xml clean package


FROM openjdk:15-jre-slim
COPY --from=build /home/toast-backend-src/target/toast.jar /home/toast/toast.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/toast/toast.jar"]