# zonky-api-test
Smoke API test of Zonky Marketplace. 


## Zonky Marketplace
 - Zonky Marketplace displays available loans. A Zonky investor can pick a loan and finance it.
 - Zonky REST API documentation at [apiary](http://docs.zonky.apiary.io/#)

## API Smoke test

The test hit the Marketplace endoint _/loans/marketplace_ and asserts:
- HTTP status is 200 OK
- response data is a _json_
- the list of loans has non-zero lenght

API uses _oauth2_ authentication. In order to make a request client must obtain API token first.

## Technicalities

### Credentials

To authenticate a valid registered Zonky user credentials must be provided. Put them into `config.yaml` file.

### Java
```
java -version
java version "1.8.0_112"
Java(TM) SE Runtime Environment (build 1.8.0_112-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.112-b15, mixed mode)
```
### Maven

```
mvn --version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T17:41:47+01:00)
Maven home: C:\tools\apache-maven-3.3.9
Java version: 1.8.0_112, vendor: Oracle Corporation
Java home: C:\Program Files\Java\jdk1.8.0_112\jre
Default locale: cs_CZ, platform encoding: Cp1250
OS name: "windows 10", version: "10.0", arch: "amd64", family: "dos"
```