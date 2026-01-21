## Academic Context

This project is based on the course-provided example repository
"kotlin-ktor-starter" from the Initial Capacity GitHub organization.
The original codebase was used as a starting point and adapted to
model the GamePulse system described in this report.

The focus of this project is system architecture, design decisions,
and requirement testability rather than building a production-ready
application from scratch.

# Kotlin ktor starter

An [application continuum](https://www.appcontinuum.io/) style example using Kotlin and Ktor
that includes a single web application with two background workers.

* Basic web application
* Data analyzer
* Data collector

### Technology stack

This codebase is written in a language called [Kotlin](https://kotlinlang.org) that is able to run on the JVM with full
Java compatibility.
It uses the [Ktor](https://ktor.io) web framework, and runs on the [Netty](https://netty.io/) web server.
HTML templates are written using [Freemarker](https://freemarker.apache.org).
The codebase is tested with [JUnit](https://junit.org/) and uses [Gradle](https://gradle.org) to build a jarfile.

## Getting Started

1.  Build a Java Archive (jar) file.
    ```bash
    ./gradlew clean build
    ```

1.  Configure the port that each server runs on.
    ```bash
    export PORT=8881
    ```

1.  Run the servers locally using the below examples.

    ```bash
    java -jar applications/basic-server/build/libs/basic-server-1.0-SNAPSHOT.jar
    ```

    Data collector

    ```bash
    java -jar applications/data-collector-server/build/libs/data-collector-server-1.0-SNAPSHOT.jar
    ```

    Data analyzer
    
    ```bash
    java -jar applications/data-analyzer-server/build/libs/data-analyzer-server-1.0-SNAPSHOT.jar
    ```
    
## Running with Docker

1. Build with Docker.

    ```bash
    docker build -t kotlin-ktor-starter . --platform linux/amd64
    ```

1.  Run with docker.

    ```bash
    docker run -e PORT=8881 -p 8881:8881 kotlin-ktor-starter
    ```

That's a wrap for now.

## Scope & Requirements
See **docs/requirements.md** for the product definition, scope, and acceptance criteria.

### Quick links
- Health: `GET /health` (liveness), `GET /health/ready` (readiness)
- Example API: `GET /api/example/{id}` (stub)
- Docs: `docs/requirements.md`

## Assignment – Tests & CI
- Unit test: `ScoringTest` (GamePulse trending score), under `applications/basic-server/src/test/...`
- Integration test: `RoutesIntegrationTest` (GET `/` returns 200).
- CI: GitHub Actions workflow runs `./gradlew clean build` on each push/PR.

    [CI](https://github.com/Jamespoju/kotlin-ktor-starter/actions/workflows/build.yml/badge.svg)
