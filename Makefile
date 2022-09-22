.PHONY: build

build:
	./mvnw -B -ntp -fae clean verify

package:
	./mvnw -B -ntp -fae clean package -Dmaven.test.skip=true

clear:
	./mvnw -B -ntp -fae clean
	docker-compose -f src/main/docker/postgresql.yml down -v

setup: build

test:
	./mvnw -B -ntp -fae test verify

test-unit-only:
	./mvnw -B -ntp -fae test

test-integration-only:
	./mvnw -B -ntp -Dtest=noTest -DfailIfNoTests=false verify

run-dev:
	java -jar -Dspring.profiles.active=dev ./target/hexlet-typo-reporter-*.jar

run-dev-docker-db: docker-db run-dev

start-frontend:
	make -C frontend start

start: run-dev-docker-db & make start-frontend

docker-db:
	docker-compose -f ./src/main/docker/postgresql.yml up -d -V --remove-orphans

update-versions:
	./mvnw versions:update-properties versions:display-plugin-updates

vagrant-build:
	vagrant up
	vagrant ssh -c "cd /vagrant && make build"

vagrant-run:
	vagrant ssh -c "cd /vagrant && make run-dev-docker-db"
