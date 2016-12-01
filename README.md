# zonky-api-test
Smoke API test of [Zonky Marketplace](https://app.zonky.cz/#/marketplace/). 


## Zonky Marketplace
 - [Zonky Marketplace](https://app.zonky.cz/#/marketplace/) displays available loans. A Zonky investor can pick a loan and finance it.
 - Zonky REST API documentation at [apiary](http://docs.zonky.apiary.io/#)

## API Smoke test

The test hits the Marketplace endpoint _/loans/marketplace_ and asserts:
- HTTP status is 200 OK
- json response confirms to json schema `marketplace.json`

API uses _oauth2_ authentication. In order to make a request client must obtain API token first.

## Requirements

- Credentials and Environments

To authenticate a valid registered Zonky user credentials must be provided. Put them into `config.yaml` file. Provide environment the test should run against e.g. for production pass `-Denv=production`.

File `marketplace.json` with json schema must be in classpath. You may want to pass `-cp C:\path\to\directory\with\marketplace.json`.

- Java
```
java -version
java version "1.8.0_112"
Java(TM) SE Runtime Environment (build 1.8.0_112-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.112-b15, mixed mode)
```
- Maven

```
mvn --version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T17:41:47+01:00)
Maven home: C:\tools\apache-maven-3.3.9
Java version: 1.8.0_112, vendor: Oracle Corporation
Java home: C:\Program Files\Java\jdk1.8.0_112\jre
Default locale: cs_CZ, platform encoding: Cp1250
OS name: "windows 10", version: "10.0", arch: "amd64", family: "dos"
```
