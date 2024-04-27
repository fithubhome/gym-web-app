# GymApp API Documentation

## Introduction
GymApp is a web application that provides authentication, user management, and role-based access control features for a gym or fitness center.

## Technologies Used
- Java
- Spring Boot
- Thymeleaf
- HTML/CSS
- HttpSession

## HttpSession Implementation
HttpSession has been implemented for the entire functionalities and APIs provided.

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
  - **URL:** `/user/dashboard`
  - **Method:** GET
  - **Description:** Retrieves the user's dashboard based on the provided user ID.
  - **Response:** Renders the dashboard page with user-specific data.

### Roles
- **Get All Roles**
  - **URL:** `/roles`
  - **Method:** GET
  - **Description:** Retrieves all roles data.
  - **Response:** Renders a page displaying all roles. Clicking on a role redirects to `/roles/{userId}`.

- **Modify User Roles**
  - **URL:** `/roles/{userId}`
  - **Method:** GET
  - **Description:** Displays the roles assigned to a user and allows modification of roles.
  - **Response:** Renders a page showing the user's roles and options to add or remove roles.

- **Add or Remove User Roles**
  - **URL:** `/roles/{userId}`
  - **Method:** POST
  - **Description:** Adds or removes roles for the specified user.
  - **Request Body:** (Example)
    ```json
    {
      "userId": 14542,
      "roleType": "Admin"
    }
    ```

## Setup Instructions
1. Clone the repository: `git clone https://github.com/fithubhome/gym-web-app`
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

## HttpSession Implementation
HttpSession has been implemented for the entire functionalities and APIs provided so far.
