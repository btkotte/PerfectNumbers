# Perfect Numbers

## Requirements

For building and running the application you need:

- [Spring Boot 2.2.4](https://spring.io/blog/2020/01/20/spring-boot-2-2-4-released)
- [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally
you can run on CLI using the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like:

```shell
mvn spring-boot:run
```
The server is configured to starts on port 9999

## REST API
In this API, each perfect number represents a resource.

## Checkif a number is perfect

Get a given perfect number from the API. 
If it is perfect then HTTP `204 no content`  is returned.
if it not perfect then HTTP `404 not found`  is returned.

```http
GET /perfects/check/{number}
```

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `number` | `long` | **Required**. The number that should be checked if perfect |


## Get all perfect numbers in a given range

Given a closed range [start,end] the API returns a list of all perfect numbers in the range

```http
GET /perfects?start=12&end=5000000
```

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `start` | `long` | **Required**. The starting number of the range |
| `end` | `long` | **Required**. The ending number of the range |

Sample response

```javascript
{
  "perfectNumbers": [
    6,
    28,
    496,
    8128
  ]
}
```
## Error Responses

Many API endpoints return the JSON representation of error
```javascript
{
    "status" : int,
    "reason" : string,
    "message" : string
  
}
```

The `status` attribute contains the http status code

The `reason` attribute describes short information regarding the error

The `message` attribute contains detailed error message

## Status Codes

The following status codes are returned in the API:

| Status Code | Description |
| :--- | :--- |
| 200 | `OK` |
| 204 | `NO CONTENT` |
| 400 | `BAD REQUEST` |
| 404 | `NOT FOUND` |
| 500 | `INTERNAL SERVER ERROR` |

## Logic

![header image](https://raw.github.com/im-mr-kotte/PerfectNumbers/master/doc/logic.jpg)