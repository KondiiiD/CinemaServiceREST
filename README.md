
# Cinema Service

The program is an simple API that simulates a cinema service. The client side can make HTTP requests to several endpoints .


## Functionalities
API can be run with one of 2 profiles (dev and prod).
- dev - connects to memory H2 database
- prod - connects with MySQL server run localy

**Project uses Liquibase to create database schema and to import test data when the application runs.**

User can perform HTTP requests to endpoints described below:

`GET api/seats/{seatId} `

- requests which returns seat information (row, column, price) with assigned ID.

`GET api/{roomId} `

- request which return information about requested cinema hall (rows, columns, list of seats).

`GET api/{roomId}/seats`

- request which return list of available seats to buy, based on provided parameter 

`POST api/{roomId}/purchase`

- request which sends JSON body with seat to buy and return information of bought ticket
```bash
Request                           
{
    "row": 3,
    "column": 3
}

Response
{
    "token": "ac2aa542-9d9b-4dea-89c5-c0d42d83b74f",
    "ticket": {
        "row": 3,
        "column": 3,
        "price": 8
    },
    "roomId": {roomId}
}
```


`PUT api/return`

- request which sends JSON body with ticket's token to return


`GET api/{roomId}/stats`

- request which returns cinema statistics for requested cinema hall only with parameters

*key* - *password*

*value* - *admin*
```bash
GET /api/{roomId}/stats
```

**All of these requests handle scenarios where the entered/requested data is incorrect.**
## Used technologies

- Java 8+
- Spring Boot
- Hibernate
- MySQL
- Liquibase
- Maven

