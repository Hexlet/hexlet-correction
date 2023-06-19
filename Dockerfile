FROM alpine:3.17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} typoreporter-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/typoreporter-0.0.1-SNAPSHOT.jar"]
