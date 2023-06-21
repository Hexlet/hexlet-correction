FROM maven:3.9.2-eclipse-temurin-20

COPY . .

RUN ./mvnw clean package -Dmaven.test.skip=true

CMD java -Xmx256m -jar target/typoreporter-*.jar --spring.profiles.active=default,prod --server.port=$PORT
