.PHONY: build

setup:
	./gradlew build

package:
	./gradlew bootJar

clean:
	./gradlew clean

stop-containers:
	docker stop $(docker ps -aq) && docker rm $(docker ps -aq)

check:
	./gradlew check

test-unit-only:
	./gradlew test

test-integration-only:
	./gradlew integrationTest

run-dev:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start: docker-db run-dev

docker-db:
	docker compose -f ./src/main/docker/postgresql.yml up -d -V --remove-orphans

vagrant-build:
	vagrant up
	vagrant ssh -c "cd /vagrant && make package"

vagrant-run:
	vagrant ssh -c "cd /vagrant && make start"
