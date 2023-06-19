FROM openjdk:21-jdk


COPY ./target/*.jar .
#ENTRYPOINT ["java","-jar","/typoreporter-0.0.1-SNAPSHOT.jar"]
