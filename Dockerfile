FROM 8.7.0-jdk21

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
