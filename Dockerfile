FROM maven:3.9.2-eclipse-temurin-20

COPY . .

CMD mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod,--server.port=$PORT"
