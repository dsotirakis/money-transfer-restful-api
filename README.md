# money-tranfer-restful-api

RESTful API for money transactions between accounts.

## Stack Used
--------------
- **Application Development**: [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)(Download Link), [Maven](https://maven.apache.org/download.cgi)(Download Link), [Jetty Server](https://en.wikipedia.org/wiki/Jetty_(web_server)), [Jersey Servlets](https://jersey.github.io/)
- **Testing**: [JUnit 5](https://junit.org/junit5/), [JerseyTest](https://jersey.github.io/documentation/latest/test-framework.html)
- **In-memory Database**: [H2 Database](https://www.h2database.com/html/main.html)

## Build
---------

In order to build and run the application, you must have _Maven_ and _Java_ (version 11) installed.

Once cloned, run:

```
mvn clean install; mvn exec:java
```

This will generate a Jetty Server, able to communicate with the API.

## API documentation

The API documentation can be found [here](api_docs/API.md).

## Testing
-----------

For unit tests and end-to-end testing, Junit Test Jupiter Engine is used.

Command:

```
mvn test
```

[JaCoCo](https://www.eclemma.org/jacoco/) library is also used for test coverage.

Once the tests have ran and the `/target` folder is produced, the results of
JaCoCo coverage can be found here:
```
target/site/jacoco/index.html
```

You can use `open target/site/jacoco/index.html` for the report to open
directly in your default web browser.
