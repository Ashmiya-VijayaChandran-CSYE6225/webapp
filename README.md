# Webapp testing

## Description

This web application offers a set of RESTful APIs designed to facilitate the management of user accounts. Through these API endpoints, users can perform tasks such as creating a new account, modifying their existing information, and accessing their account details. Additionally, the application incorporates token-based authentication mechanisms to safeguard the privacy and security of user data during access.

## Prerequisites
- Java (jdk 17)
- Spring Boot (version 3.2.2)
- Database (MySQL)

## Steps to follow

1. Clone the repository: git clone <repository_url>
2. Build the project: ./mvnw install (for Maven)
3. Run the application: ./mvnw spring-boot:run

## API Endpoints

### Create User

- Endpoint: /v1/user
- Method: POST
- Request Payload:
  json
  {
  "email": "user@example.com",
  "password": "your_password",
  "first_name": "John",
  "last_name": "Doe"
  }

- Response: 200 OK.

### Update User

- Endpoint: /v1/user/self
- Method: PUT
- Request Payload: Fields that cab be updated (firstName, lastName, password)
- Response: 204 NO CONTENT.

### Get User

- Endpoint: /v1/user/self
- Method: GET
- Response: 200 OK.

Note: Handle 400 Bad Request and 401 Unauthorized Request wherever required. 

### Authentication Requirements

- The user must provide a basic authentication token when making an API call to the authenticated endpoint.
- The web application must only support Token-Based authentication and not Session Authentication.

### Implement Continuous Integration (CI) with GitHub Actions
- Add a GitHub Actions workflow to run simple check (compile code) for each pull request raised. A pull request can only be merged if the workflow executes successfully.
- Add Status Checks GitHub branch protection to prevent users from merging a pull request when the GitHub Actions workflow run fails.


