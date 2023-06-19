FROM alpine:3.17

COPY ./target/*.jar typoreporter-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/typoreporter-0.0.1-SNAPSHOT.jar"]
