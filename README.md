# Petstore API Automation Framework

REST API test automation suite for the [Swagger Petstore](https://petstore.swagger.io) API,
built from scratch in Java.

## Tech stack

| Tool | Purpose |
|---|---|
| Java 21 | Language |
| Maven | Build and dependency management |
| REST Assured 5.4 | HTTP calls and assertions |
| TestNG 7.10 | Test runner, groups, data providers |
| Jackson | JSON ↔ Java object mapping |
| Allure | HTML reports with request/response attachments |
| GitHub Actions | CI on every push |

## What's covered

- **Pet, Store, User endpoints**: full CRUD lifecycle for each resource
- **Negative tests**: 404 on missing resources, double-delete behaviour
- **Data-driven tests**: pet status variants (available/pending/sold) and order status variants (placed/approved/delivered) via `@DataProvider`
- **TestNG groups**: smoke (3 tests) and regression (full suite)
- **Config-driven base URL**: override per environment with `-Dbase.url=...`

## Project structure

src/
├── main/java/com/automation/petstore/
│ ├── base/ BaseTest.java
│ ├── config/ ConfigManager.java
│ ├── endpoints/ Endpoints.java
│ └── models/ Pet, Order, User, Category, Tag, ApiResponse
└── test/java/com/automation/petstore/tests/
├── PetTests.java
├── StoreTests.java
├── UserTests.java
└── NegativeTests.java


## Run locally

**Prerequisites:** Java 21, Maven 3.9+, Allure CLI (`brew install allure`)

```bash
# Full suite
mvn clean test

# Smoke suite only
mvn clean test -DsuiteFile=testng-smoke.xml

# Target a different environment
mvn clean test -Dbase.url=https://your-other-host/v2

# Open the Allure report
allure serve target/allure-results
```

## CI

GitHub Actions runs `mvn clean test` on every push to `main`.
Allure results are uploaded as a downloadable artifact for each run.