[![Build Status](https://travis-ci.org/milanoid/zonky-api-test.svg)](https://travis-ci.org/milanoid/zonky-api-test)


# zonky-api-test
Smoke API test of [Zonky Marketplace](https://app.zonky.cz/#/marketplace/). 


## Zonky Marketplace
 - [Zonky Marketplace](https://app.zonky.cz/#/marketplace/) displays available loans. A Zonky investor can pick a loan and finance it.
 - Zonky REST API documentation at [apiary](http://docs.zonky.apiary.io/#)

## API Smoke test

The test hits the Marketplace endpoint _/loans/marketplace_ and asserts:
- HTTP status is 200 OK

API uses _oauth2_ authentication. In order to make a request client must obtain API token first.

## Requirements

- Credentials and Environments

To authenticate a valid registered Zonky user credentials must be provided. Put them into `config.yaml` file. Pass environment variable `ENV` with one of this value: _production_, _preprod_, _test_.
You may override `config.yaml` values `USERNAME` and `PASSWORD` by environment variables of the same name.

E.g.

Run tests against _production_ using user credentials in `config.yaml`

``
mvn test -DENV=production
``

Run tests against _production_ using user credentials from environment variables `USERNAME` and `PASSWORD`

``
mvn test -DENV=production -DUSERNAME=$USERNAME -DPASSWORD=$PASSWORD
``

