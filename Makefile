.DEFAULT_GOAL := build-run

run:
	java -jar ./target/hexlet-correction-0.0.1-SNAPSHOT.jar

clean:
	rm -rf ./target

build-run: build run

build:
	./mvnw clean package

update:
	./mvnw versions:update-properties versions:display-plugin-updates