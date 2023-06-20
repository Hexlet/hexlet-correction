FROM openjdk:19
WORKDIR /

COPY / .

RUN ./mvnw -B -ntp -fae clean verify

CMD java -jar -Dspring.profiles.active=dev ./target/typoreporter-*.jar

EXPOSE 5432
