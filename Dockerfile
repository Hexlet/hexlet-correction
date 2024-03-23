FROM eclipse-temurin:21-jdk

# FROM maven:3.9.2-eclipse-temurin-20 AS build

ARG GRADLE_VERSION=8.6

RUN apt-get update && apt-get install -yq unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR .

# CMD mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod,--server.port=$PORT"

#
# Build stage
#

COPY . .
# RUN mvn clean package -DskipTests
RUN gradle build -x test

#
# Package stage
#
# FROM openjdk:20-jdk-slim
# COPY --from=build ./target/typoreporter-*.jar typoreporter.jar

# CMD java -Xmx256m -jar target/typoreporter-*.jar --spring.profiles.active=default,prod --server.port=$PORT

CMD make run-dev

# CMD java -Xmx256m -jar build/libs/typoreporter-*.jar --spring.profiles.active=default,prod --server.port=$PORT
