FROM openjdk:19
WORKDIR /

COPY / .


CMD java -jar -Dspring.profiles.active=dev ./target/typoreporter-*.jar

EXPOSE 5432
