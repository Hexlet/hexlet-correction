.DEFAULT_GOAL := build-run

run: docker-db
	java -jar ./target/hexlet-correction-*.jar

build-run: build run

validate:
	./mvnw -ntp -fae validate

unit-test:
	./mvnw -ntp -fae clean test

build:
	./mvnw -ntp -fae clean verify

build-docker:
	./mvnw -ntp -fae clean verify jib:dockerBuild

docker-db:
	docker-compose -f ./src/main/docker/postgresql.yml up -d

update:
	./mvnw versions:update-properties versions:display-plugin-updates

generate-migration:
	./mvnw clean compile liquibase:update liquibase:diff -DskipTests=true && rm /tmp/liquibase_migration*
