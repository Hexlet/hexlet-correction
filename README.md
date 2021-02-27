# Hexlet Typo Reporter

This application created for receive the typo errors from a site.

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. Java 15

## Building for development

### Packaging as uber-jar

To build the final jar and optimize the hexletTypoReporter application for development, run:

    make build

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    make test

## Using Docker to simplify development (optional)

For example, to start a postgresql database in a docker container, run:

    make docker-db

## typo API calls

For creating new `typo`:

    POST http://localhost:8080/api/typos
    Content-Type: application/json
    
    {
        "pageUrl": "http://site.com/",
        "reporterName": "reporterName",
        "reporterComment": "reporterComment",
        "textBeforeTypo": "textBeforeTypo",
        "textTypo": "textTypo",
        "textAfterTypo": "textAfterTypo"
    }

For receiving `typo` by id:

    GET http://localhost:8080/api/typos/{id}

For receiving default page with `typo`:

    GET http://localhost:8080/api/typos

For receiving custom page with `typo`:

    GET http://localhost:8080/api/typos?size=5&page=0&sort=typoStatus,desc

For updating existing `typo`:

    PATCH http://localhost:8080/api/typos/{id}
    Content-Type: application/json
    
    {
        "reporterComment": "new comment",
        "typoEvent": "OPEN"
    }

For deleting `typo`:

    DELETE http://localhost:8080/api/typos/{id}
