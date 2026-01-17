# Full-Stack Restaurant Reservation System

Developed as a comprehensive solution for managing restaurant bookings, this project focuses on the integration of modern web frameworks with reliable client-server communication.

## Architecture and Design Patterns
The project follows a multi-tier architecture, emphasizing clean code and scalability through several design patterns:
* Model-View-Controller (MVC): Used to separate the presentation layer, the application logic, and the data models.
* Repository Pattern: Implemented to abstract the data access layer for the MySQL database.
* Data Transfer Object (DTO): Employed for structured data exchange between the web frontend and the backend.

## SOLID Principles
* Single Responsibility Principle (SRP): Each component is dedicated to a specific task, such as request handling or database operations.
* Dependency Inversion Principle (DIP): Implemented by leveraging Spring's Dependency Injection to decouple high-level logic from implementation details.

## Tech Stack
* Backend: Java, Spring Boot, OCSF.
* Frontend: Thymeleaf, JavaScript, HTML5, CSS3.
* Database: MySQL.
* Tools: IntelliJ IDEA, Maven.

## How It Works
1. The user interacts with the Thymeleaf-based frontend to manage reservations.
2. Spring Boot processes the request and communicates with the backend via the OCSF protocol.
3. The server validates the data and performs operations in the SQL database.
4. The system provides real-time feedback through the dynamic web interface.
