# Spring Todo Application

## Tech Stack
| Component          | Technology                                                                                                                    |
|--------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Backend (REST)     | [Spring 6](https://spring.io/projects/spring-framework) & [SpringBoot 3.12](https://projects.spring.io/spring-boot) (Java 17) |
| Security           | Token Based (Spring Security and JWT )                                                                                        |
| REST Documentation | [Swagger & Spring Doc](https://springdoc.org/)                                                                                |
| REST Spec          | [Open API Standard](https://www.openapis.org/)                                                                                |
| Persistence        | JPA (Using Spring Data)                                                                                                       |
| Build Tool         | Gradle                                                                                                                        |

## Features

### Security Features

- Registering new users
- Login with existing user
- Logout with one or all devices
- Role System
- Authentication and authorization with JWT
- Refresh Token functionality
- Forgot Password

***

### Application Features

- Create, Read, Update, Delete Todos
- Filter and Sort Todos
- Upload profile image for User

***

# How to use this code?

1. Make sure you have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Gradle](https://gradle.org/install/) installed

2. Clone this repository
```
$ git clone https://github.com/robotTX1/SpringTodoApp.git
```

3. Navigate into the folder

```
$ cd SpringTodoApp
```

4. Create `secrets.yaml` in your `resources` folder and copy everything from `example-secrets.yaml` to it.
   Change example values to real ones.

5. Build project

```
$ gradle build
```

6. Run the project

```
$ gradle bootRun
```

7. Navigate to `http://localhost:8080/swagger-ui.html` in your browser to check everything is working correctly. You can change the default port in the `application.yml` file

```yml
server:
  port: 8080
```

8. Make a GET request to `/api/v1/todos` to check you're not authenticated. You should receive a response with a `401` with no message since you haven't set your valid JWT token yet

```
$ curl -X GET 'http://localhost:8080/api/v1/todos'
```

9. Make a POST request to `/api/v1/auth/login` with the default admin user we programatically created to get a valid JWT token

```
$ curl -d '{"email": "admin@example.com", "password": "password"}' -H 'Content-Type: application/json' -X POST 'http://localhost:8080/api/v1/auth/login'
```

10. Add the JWT token as a Header parameter and make the initial GET request to `/api/v1/todos` again

```
$ curl -H 'Authorization: Bearer <JWT_TOKEN>' -X GET http://localhost:8080/api/v1/todos
```

11. If you got 200 OK and your todos back thank you did everything correctly!

***

## Contribution

- Report issues
- Open pull request with improvements
- Reach out to me directly at <csikos.csaba.hu@gmail.com>