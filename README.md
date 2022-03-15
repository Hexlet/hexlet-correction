# Hexlet Typo Reporter

This application created for receive the typo errors from a site.

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java 17

### Packaging as uber-jar

To build the final jar:

    make build

### Development with vagrant

Install Vagrant and VM provider (Virtualbox).

Run commands:

```bash
make vagrant build
make vagrant-run
```

or just `vagrant ssh`, then `cd /vagrant` to run commands inside vm

## Testing

To launch your application's tests, run:

    make test

## Using Docker to simplify development (optional)

For example, to start a postgresql database in a docker container, run:

    make docker-db

## Run application with database in docker

    make run-dev

## Build and run application with database in docker

    make run-dev-docker-db

## typo API calls

For creating new `typo`:

    POST http://localhost:8080/api/workspaces/{workspace-name}/typos
    Content-Type: application/json
    Authorization: Token workspace-token

    {
        "pageUrl": "http://site.com/",
        "reporterName": "reporterName",
        "reporterComment": "reporterComment",
        "textBeforeTypo": "textBeforeTypo",
        "textTypo": "textTypo",
        "textAfterTypo": "textAfterTypo"
    }

---

[![Hexlet Ltd. logo](https://raw.githubusercontent.com/Hexlet/assets/master/images/hexlet_logo128.png)](https://ru.hexlet.io/pages/about?utm_source=github&utm_medium=link&utm_campaign=exercises-javascript)

This repository is created and maintained by the team and the community of Hexlet, an educational project. [Read more about Hexlet (in Russian)](https://ru.hexlet.io/pages/about?utm_source=github&utm_medium=link&utm_campaign=hexlet-comparator).

See most active contributers on [hexlet-friends](https://friends.hexlet.io/).
