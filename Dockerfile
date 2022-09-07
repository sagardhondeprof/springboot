#define base docker image
FROM openjdk:8-jdk-alpine
ADD target/login-0.0.1-SNAPSHOT.jar springboot-docker-demo.jar
#ADD sampledata h2/sampledata
ENTRYPOINT ["java", "-jar", "springboot-docker-demo.jar"]