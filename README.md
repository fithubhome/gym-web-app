# GymApp API Documentation

## Introduction
GymApp is a web application that provides authentication and user management features for a gym or fitness center.

## Technologies Used
- Java
- Spring Boot
- Thymeleaf
- HTML/CSS

## Endpoints - entry point: http://localhost:8080/

### User Authentication
- **Login**
    - **URL:** `/user/login`
    - **Method:** POST
    - **Description:** Authenticates a user with provided email and password.
    - **Request Body:**
      ```json
      {
        "email": "user@example.com",
        "password": "password"
      }
      ```
    - **Response:** Redirects to the dashboard upon successful authentication, otherwise returns to the login page with an error message.

- **Register**
    - **URL:** `/user/register`
    - **Method:** POST
    - **Description:** Registers a new user with the provided details.
    - **Request Body:**
      ```json
      {
        "email": "newuser@example.com",
        "password": "password"
      }
      ```
    - **Response:** Redirects to the login page upon successful registration, otherwise returns to the registration page with an error message.

### Dashboard
- **Get Dashboard**
    - **URL:** `/user/{userId}/dashboard`
    - **Method:** GET
    - **Description:** Retrieves the user's dashboard based on the provided user ID.
    - **Response:** Renders the dashboard page with user-specific data.

## Setup Instructions
1. Clone the repository: `git clone <repository_url>`
2. Navigate to the project directory: `cd gymapp`
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

## Contribution Guidelines
Contributions to improve and enhance GymApp are welcome! Here's how you can contribute:
- Fork the repository
- Create a new branch: `git checkout -b feature/new-feature`
- Make your changes and commit them: `git commit -am 'Add new feature'`
- Push to the branch: `git push origin feature/new-feature`
- Submit a pull request

## License
This project is licensed under the [MIT License](LICENSE).
