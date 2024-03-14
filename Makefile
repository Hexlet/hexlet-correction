.PHONY: build

build:
	./gradlew clean check

package:
	./gradlew clean bootJar -x test

clear:
	./gradlew clean
	docker compose -f docker/docker-compose.yml down -v

setup: build

test:
	./gradlew unitTest integrationTest

test-unit-only:
	./gradlew unitTest

test-integration-only:
	./gradlew integrationTest

run-dev:
	./gradlew bootRun --args='--spring.profiles.active=dev'

run-dev-docker-db: docker-infra-start run-dev

start: run-dev-docker-db

docker-infra-start:
	docker compose -f docker/docker-compose.yml up -d -V --remove-orphans

run-dev-debug:
	./gradlew bootRun --args='--spring.profiles.active=dev' -Dorg.gradle.jvmargs="-Xdebug"

update-versions:
	./gradlew dependencyUpdates

vagrant-build:
	vagrant up
	vagrant ssh -c "cd /vagrant && make build"

vagrant-run:
	vagrant ssh -c "cd /vagrant && make run-dev-docker-db"
