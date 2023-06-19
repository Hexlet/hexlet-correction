FROM gradle:7.4.0-jdk17

WORKDIR /src

COPY / .

RUN docker-infra-start run-dev

CMD ./target/typoreporter-*.jar
