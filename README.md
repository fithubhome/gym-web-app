# GYM App Documentation

## Introduction
GymApp is a web application that provides authentication, user/profile management and role-based access control features for a GYM or Fitness Center.

## Technologies Used
- Java
- Spring Boot
- Thymeleaf
- HTML/CSS
- HttpSession/Security
- JPA (Hibernate)
- MySQL 8
- Maven
- Docker

## Setup Instructions
1. Clone the repositories: 
   - `git clone https://github.com/fithubhome/gym-web-app`
   - `git clone https://github.com/fithubhome/membership-api.git`
   - `git clone https://github.com/fithubhome/payment-api.git`
   - `git clone https://github.com/fithubhome/body_stats.git`
   - `git clone https://github.com/fithubhome/GymActivities.git`
2. SQL database must be created based on the application.properties on port 3306 with the database name: gym_app (as per link mysql://localhost:3306/gym_app); same for
      other APIs the database (including name) must be created as per application.properties file and on localhsot port


For docker compose:
1. Navigate to the project directory: `cd gym-web-app`
2. Build the project: `mvn clean package`
3. Run the application: `mvn spring-boot:run`
4. To use Docker, build and start the services: `docker-compose up --build`

## Entry point: http://localhost:8080/

### User Authentication
- **Login**
  - **URL:** `/auth/login`
  - **Method:** GET/POST
  - **Description:** Authenticates a userEntity with provided email and password.
  - **Response:** Redirects to the dashboard upon successful authentication, otherwise returns to the login page with an error message.

- **Register**
  - **URL:** `/auth/register`
  - **Method:** POST
  - **Description:** Registers a new userEntity with the provided details.
  - **Response:** Redirects to the login page upon successful registration, otherwise returns to the registration page with an error message.

### Dashboard
- **Get Dashboard**
  - **URL:** `/dashboard`
  - **Method:** GET
  - **Description:** Retrieves the user's dashboard.
  - **Response:** Renders the dashboard page with user-specific data.

### Roles
- **Get All Roles**
  - **URL:** `/role`
  - **Method:** GET
  - **Description:** Retrieves all role data.
  - **Response:** Renders a page displaying all roles. Clicking on a role redirects to `/role/{userId}`.

- **Modify User Roles**
  - **URL:** `/role/{userId}`
  - **Method:** GET
  - **Description:** Displays the role assigned to a user and allows modification of role.
  - **Response:** Renders a page showing the user's role and options to add or remove roles.

- **Add or Remove User Roles**
  - **URL:** `/role/{userId}`
  - **Method:** POST
  - **Description:** Adds or removes role for the specified user.

### Profile Module
- **Get Profile**
  - **URL:** `/profile`
  - **Method:** GET
  - **Description:** Retrieves the profile of the currently logged-in user.
  - **Response:** Renders the profile view page with user-specific data.

- **Edit Profile**
  - **URL:** `/profile/edit`
  - **Method:** GET
  - **Description:** Displays the profile edit form for the current user.
  - **Response:** Renders the profile update form with existing user data.

- **Update Profile**
  - **URL:** `/profile/update`
  - **Method:** POST
  - **Description:** Updates the profile details for the current user based on the form submission.
  - **Response:** Redirects to the profile view page after successful update or returns error messages if the update fails.

## Contribution Guidelines
Contributions to improve and enhance GymApp are welcome! Here's how you can contribute:
- Fork the repository
- Create a new branch: `git checkout -b feature/new-feature`
- Make your changes and commit them: `git commit -am 'Add new feature'`
- Push to the branch: `git push origin feature/new-feature`
- Submit a pull request

## License
This project is licensed under the [MIT License](LICENSE).
