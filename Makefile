.DEFAULT_GOAL := build-run

run-dev: docker-db
	java -jar -Dspring.profiles.active=dev ./build/libs/hexlet-typo-reporter-*.jar

build-run: build run-dev

test:
	./gradlew test

build:
	./gradlew clean build

docker-db:
	docker-compose -f ./src/main/docker/postgresql.yml up -d --force-recreate

update:
	./gradlew dependencyUpdates
