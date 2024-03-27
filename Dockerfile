FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.6

RUN apt-get update && apt-get install -yq unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR .

#
# Build stage
#

COPY . .

RUN gradle build -x test

#
# Run application
#

CMD java -Xmx256m -jar build/libs/typoreporter-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
