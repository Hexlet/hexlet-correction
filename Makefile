.PHONY: build

build:
	./mvnw -B -ntp -fae clean verify

package:
	./mvnw -B -ntp -fae clean package -Dmaven.test.skip=true

clear:
	./mvnw -B -ntp -fae clean
	docker compose -f docker/docker-compose.yml down -v

setup: build

test:
	./mvnw -B -ntp -fae test verify

test-unit-only:
	./mvnw -B -ntp -fae test

test-integration-only:
	./mvnw -B -ntp -Dtest=noTest -Dsurefire.failIfNoSpecifiedTests=false verify

run-dev:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

run-dev-docker-db: docker-infra-start run-dev

start: run-dev-docker-db

docker-infra-start:
	docker compose -f docker/docker-compose.yml up -d -V --remove-orphans

run-dev-debug:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.jvmArguments="-Xdebug"

update-versions:
	./mvnw versions:update-properties versions:display-plugin-updates

vagrant-build:
	vagrant up
	vagrant ssh -c "cd /vagrant && make build"

vagrant-run:
	vagrant ssh -c "cd /vagrant && make run-dev-docker-db"
