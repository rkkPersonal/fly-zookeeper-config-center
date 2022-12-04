FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} zookeeper-center.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Xmx512m","-Xms512m","/zookeeper-center.jar"]
