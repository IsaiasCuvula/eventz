## **RESTful API - Event Management System**

#### **Overview**

This project is an API developed with Java Spring Boot to manage CRUD operations (Create, Read, Update, Delete) in an
event management system. The API includes authentication and authorization features using JWT.

#### **Key Features**

1. **User Registration and Authentication**
    - User registration and login.
    - JWT-based authentication.
    - Email verification.

2. **Event Management**
    - Full CRUD operations for events (creation, viewing, updating, and deletion).
    - Support for listing all events and viewing specific event details.

3. **Access Authorization**
    - Different permission levels (regular users and administrators).
    - Access control for event creation, viewing, and editing.

4. **Search and Filtering**
    - Search by title, date, and location.
    - Filtering by category and upcoming events.

5. **Participant Management**
    - Event registration and cancellation for users.
    - Listing participants.

6. **Reports and Statistics**
    - Generating participation reports.
    - Event and participant statistics.

#### **Technologies Used**

- **Backend:** Java, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, PostgreSQL.
- **Testing:** JUnit, Mockito.
- **Documentation:** Swagger.