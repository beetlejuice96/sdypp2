FROM openjdk:latest
COPY target/server.jar /usr/src/server.jar
WORKDIR /usr/src/
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/usr/src/server.jar"]
