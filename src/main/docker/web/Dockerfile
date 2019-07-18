FROM openjdk:8u212-jdk
WORKDIR /home/user/
COPY target/spring-hotel-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -jar -Dspring.profiles.active=docker app.jar
EXPOSE 8080