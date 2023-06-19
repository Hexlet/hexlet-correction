FROM alpine:3.17

COPY ./target/*.jar
ENTRYPOINT ["java","-jar","/typoreporter-0.0.1-SNAPSHOT.jar"]
