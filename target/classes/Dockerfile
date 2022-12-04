FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} zookeeper-center.jar

ENTRYPOINT ["java","-jar","/zookeeper-center.jar"]