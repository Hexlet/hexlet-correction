.PHONY: build

build:
	./mvnw -B -ntp -fae clean verify

test:
	./mvnw -B -ntp -fae test verify

test-unit-only:
	./mvnw -B -ntp -fae test

test-integration-only:
	./mvnw -B -ntp -Dtest=noTest -DfailIfNoTests=false verify

run-dev:
	java -jar -Dspring.profiles.active=dev ./target/hexlet-typo-reporter-*.jar

run-dev-docker-db: docker-db
	java -jar -Dspring.profiles.active=dev ./target/hexlet-typo-reporter-*.jar

docker-db:
	docker-compose -f ./src/main/docker/postgresql.yml up -d --force-recreate

update-versions:
	./mvnw versions:update-properties versions:display-plugin-updates
