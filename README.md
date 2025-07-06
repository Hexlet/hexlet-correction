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

## Registration/Authorization with GitHub

For registration or/and authorization account with GitHub:
- Create OAuth app https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/creating-an-oauth-app.
- Authorization callback URL = {baseUrl}/login/oauth2/code/github, for example: http://localhost:8080/login/oauth2/code/github
- Get values the `Client ID` and `Client Secret` and add to environment variables in any known way.

For example, you can create an `.env` file in the root of the project, where you can enter the names of variables
and their values as shown below:
```bash
GITHUB_CLIENT_ID=your_github_client_id_values
GITHUB_CLIENT_SECRET=your_github_client_secret_values
```
A `.env.example` file has been created in the root of the project, which specifies the variables as they should be specified. For these variables, you need to specify the values you received.
You can copy this file and rename to `.env`, change and use.
---

[![Hexlet Ltd. logo](https://raw.githubusercontent.com/Hexlet/assets/master/images/hexlet_logo128.png)](https://hexlet.io/?utm_source=github&utm_medium=link&utm_campaign=hexlet-correction)

This repository is created and maintained by the team and the community of Hexlet, an educational project. [Read more about Hexlet](https://hexlet.io/?utm_source=github&utm_medium=link&utm_campaign=hexlet-correction).

See most active contributors on [hexlet-friends](https://friends.hexlet.io/).
