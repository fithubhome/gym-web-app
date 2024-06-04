# GYM App Documentation

## Introduction
GymApp is a web application that provides authentication, userEntity management, and role-based access control features for a gym or fitness center.

## Technologies Used
- Java
- Spring Boot
- Thymeleaf
- HTML/CSS
- HttpSession/Security
- JPA (Hibernate)
- MySQL 8

## HttpSession Implementation
HttpSession has been implemented for the entire functionalities and APIs provided.

## JPA and MySQL Integration
JPA (using Hibernate) is used for data persistence, and MySQL 8 is used as the database. This allows seamless interaction with the database through ORM (Object-Relational Mapping), enabling CRUD operations and complex queries.

## Endpoints - entry point: http://localhost:8080/

### User Authentication
- **Login**
  - **URL:** `/userEntity/login`
  - **Method:** POST
  - **Description:** Authenticates a userEntity with provided email and password.
  - **Request Body:**
    ```json
    {
      "email": "userEntity@example.com",
      "password": "password"
    }
    ```
  - **Response:** Redirects to the dashboard upon successful authentication, otherwise returns to the login page with an error message.

- **Register**
  - **URL:** `/userEntity/register`
  - **Method:** POST
  - **Description:** Registers a new userEntity with the provided details.
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
  - **URL:** `/userEntity/dashboard`
  - **Method:** GET
  - **Description:** Retrieves the userEntity's dashboard based on the provided userEntity ID.
  - **Response:** Renders the dashboard page with userEntity-specific data.

### Roles
- **Get All Roles**
  - **URL:** `/role`
  - **Method:** GET
  - **Description:** Retrieves all role data.
  - **Response:** Renders a page displaying all roles. Clicking on a role redirects to `/role/{userId}`.

- **Modify User Roles**
  - **URL:** `/role/{userId}`
  - **Method:** GET
  - **Description:** Displays the role assigned to a userEntity and allows modification of role.
  - **Response:** Renders a page showing the userEntity's role and options to add or remove roles.

- **Add or Remove User Roles**
  - **URL:** `/role/{userId}`
  - **Method:** POST
  - **Description:** Adds or removes role for the specified userEntity.
  - **Request Body:** (Example)
    ```json
    {
      "userId": UUID,
      "roleType": "Admin"
    }
    ```

### Profile Module
- **Get Profile**
  - **URL:** `/profile`
  - **Method:** GET
  - **Description:** Retrieves the profile of the currently logged-in userEntity.
  - **Response:** Renders the profile view page with userEntity-specific data.

- **Edit Profile**
  - **URL:** `/profile/edit`
  - **Method:** GET
  - **Description:** Displays the profile edit form for the current userEntity.
  - **Response:** Renders the profile update form with existing userEntity data.

- **Update Profile**
  - **URL:** `/profile/update`
  - **Method:** POST
  - **Description:** Updates the profile details for the current userEntity based on the form submission.
  - **Request Body:** (Example)
    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "gender": "M",
      "dob": "1985-05-15",
      "address": "123 Main St",
      "phone": "0776543210",
      "imageData": "base64Image"
    }
    ```
  - **Response:** Redirects to the profile view page after successful update or returns error messages if the update fails.

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
