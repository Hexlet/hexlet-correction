FROM maven:3.9.2-eclipse-temurin-20 AS build


# CMD mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod,--server.port=$PORT"


#
# Build stage
#

COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
# FROM openjdk:20-jdk-slim
# COPY --from=build ./target/typoreporter-*.jar typoreporter.jar

CMD java -Xmx256m -jar target/typoreporter-*.jar --spring.profiles.active=default,prod --server.port=$PORT
