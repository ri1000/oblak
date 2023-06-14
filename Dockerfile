FROM openjdk:17-jdk
WORKDIR /app
COPY target/sd-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java","-jar","sd-0.0.1-SNAPSHOT.jar"]