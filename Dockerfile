FROM openjdk:19
WORKDIR /

COPY / .

RUN docker-compose -f docker/docker-compose.yml up -d -V --remove-orphans
CMD java -jar -Dspring.profiles.active=dev ./target/typoreporter-*.jar
EXPOSE 5432
