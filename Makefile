.DEFAULT_GOAL := build-run

run-dev: docker-db
	java -jar -Dspring.profiles.active=default,dev ./target/hexlet-typo-reporter-*.jar

build-run: build run-dev

unit-test:
	./mvnw -B -ntp -fae test

integration-test:
	./mvnw -B -ntp -Dtest=noTest -DfailIfNoTests=false verify

test:
	./mvnw -B -ntp -fae test verify

build:
	./mvnw -B -ntp -fae clean verify

docker-db:
	docker-compose -f ./src/main/docker/postgresql.yml up -d --force-recreate

update:
	./mvnw versions:update-properties versions:display-plugin-updates
