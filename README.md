# Hexlet Typo Reporter

A service to notify site owners of errors and typos. After integrating with the site, visitors are able to highlight an error or typo and report it to the administrator. The project works in Java.

Tasks can be discussed in the [Telegram community](https://t.me/hexletcommunity/12).

## Example

Go to: https://hexlet.github.io/hexlet-correction/index.html

Highlight text and press <kbd>Ctrl+Enter</kbd>

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java 19
2. Docker, Docker Compose

### Packaging as uber-jar

To build the final jar:

```bash
make build
```

### How to add a OAuth2 app for local development
Before starting local development, you must create an OAuth application and add clientID and clientSecret to application.yaml: 

```bash
clientId: #add clientId of your OAuth App
clientSecret: #add clientSecret of your OAuth App
```

See more at https://spring.io/guides/tutorials/spring-boot-oauth2#github-register-application

### How to add a OAuth2 app to external server
You need to add environment variables:
```bash
GITHUB_CLIENT_ID
GITHUB_CLIENT_SECRET
```

### Development with vagrant

Install Vagrant and VM provider (Virtualbox).

Run commands:

```bash
make vagrant-build
make vagrant-run
```

or just `vagrant ssh`, then `cd /vagrant` to run commands inside vm

## Testing

To launch your application's tests, run:

```bash
make test
```

## Using Docker to simplify development (optional)

For example, to start a postgresql database in a docker container, run:

```bash
make docker-infra-start
```

## Run application with database in docker

```bash
make run-dev
```

## Build and run application with database in docker

```bash
make run-dev-docker-db # make start
```

## Clear config (database, app build)

```bash
make clear
```

## Typo API calls

For creating new `typo`:

```plaintext
POST http://localhost:8080/api/workspaces/{workspaceId}/typos
Content-Type: application/json
Authorization: Basic base64(workspaceId:api-token)
Referer: (workspaceId:allowed-url)

{
    "pageUrl": "https://mysite.com/page/with/typo",
    "reporterName": "reporterName",
    "reporterComment": "reporterComment",
    "textBeforeTypo": "textBeforeTypo",
    "textTypo": "textTypo",
    "textAfterTypo": "textAfterTypo"
}
```

---

[![Hexlet Ltd. logo](https://raw.githubusercontent.com/Hexlet/assets/master/images/hexlet_logo128.png)](https://hexlet.io/?utm_source=github&utm_medium=link&utm_campaign=hexlet-correction)

This repository is created and maintained by the team and the community of Hexlet, an educational project. [Read more about Hexlet](https://hexlet.io/?utm_source=github&utm_medium=link&utm_campaign=hexlet-correction).

See most active contributors on [hexlet-friends](https://friends.hexlet.io/).
