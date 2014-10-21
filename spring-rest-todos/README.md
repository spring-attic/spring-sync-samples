# Spring REST Todos

[![Build Status](https://drone.io/github.com/royclarkson/spring-rest-todos/status.png)](https://drone.io/github.com/royclarkson/spring-rest-todos/latest)

A simple todo list example built with Spring

## Build and Run

> **NOTE:**
> This project now depends on org.springframework:spring-sync:0.5.0.RELEASE. This project is not (yet) in any known Maven repository. Therefore, you'll need to clone it from https://github.com/habuma/spring-sync and do 'gradle build install' to get it into your local repository.

```sh
./gradlew clean build bootRun
```

## Test

The following curl commands can be used to test the API.

Request the list of todos:

```sh
curl -X "GET" -v localhost:8080/todos
```

Add a new todo:

```sh
curl -X "POST" -v localhost:8080/todos -H "Content-Type: application/json" -d '{"description":"A Todo","complete":false}'
```

Modify an existing todo:

```sh
curl -X "PUT" -v localhost:8080/todos/0 -H "Content-Type: application/json" -d '{"description":"Modified Todo","complete":false}'
```

Delete a todo:

```sh
curl -X "DELETE" -v localhost:8080/todos/0
```

Apply a JSON PATCH to the todo list:

```sh
curl -X "PATCH" -v localhost:8080/todos -H "Content-Type: application/json" -d '[{"op":"replace","path":"/0/description","value":"go go go!"}]'
```

Generate a JSON PATCH from a modified todo list:

```sh
curl -X "POST" -v localhost:8080/todos/diff -H "Content-Type: application/json" -d '[{"description":"go go go!","complete":false},{"description":"b","complete":false}]'
```

## Run the Web Client

### Setup

```sh
npm install -g bower
cd public
bower install
```

### Open in Browser

Go to http://localhost:8080/index.html