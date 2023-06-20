FROM openjdk:19
ADD target/typoreporter-0.0.1-SNAPSHOT.jar typoreporter-0.0.1-SNAPSHOT.jar

RUN ./mvnw -B -ntp -fae clean verify
RUN docker-compose -f docker/docker-compose.yml up -d -V --remove-orphans
RUN java -jar -Dspring.profiles.active=dev ./target/typoreporter-*.jar
EXPOSE 5432
