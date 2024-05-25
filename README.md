# Train-Ticket_API

Train Ticket API is a Spring Boot application that allows users to purchase train tickets, manage user information, allocate seats, and retrieve ticket receipts. It includes a RESTful API for handling CRUD operations on passengers and seat management.

## Features

- **Passenger Management**: Create, update, delete, and retrieve passenger details.
- **Seat Allocation**: Automatically allocate seats in sections A and B.
- **Ticket Purchase**: Validate user information, allocate seats, and generate ticket receipts.
- **Section Management**: Fetch passengers based on seat section.
- **Error Handling**: Proper error handling and response status codes.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- H2 Database (for development)
- Jakarta Persistence (JPA)
- RESTful API

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/your-username/train_ticket_api.git
    cd train_ticket_api
    ```

2. **Build the project:**

    ```sh
    mvn clean install
    ```

3. **Run the application:**

    ```sh
    mvn spring-boot:run
    ```

4. **Access the API:**

    The application will be running at `http://localhost:8080/api`.

### API Endpoints

- **Purchase Ticket:**
    - **Endpoint:** `POST /api/purchase`
    - **Description:** Purchase a ticket by providing passenger details.
    - **Request Body:**
        ```json
        {
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.doe@example.com"
        }
        ```

- **Get User Receipt:**
    - **Endpoint:** `GET /api/receipt/{id}`
    - **Description:** Retrieve the receipt of a user by ID.

- **Get Users by Section:**
    - **Endpoint:** `GET /api/section/{section}`
    - **Description:** Get a list of passengers in a specific section.

- **Remove User:**
    - **Endpoint:** `DELETE /api/user/{id}`
    - **Description:** Remove a user by ID.

- **Modify User Seat:**
    - **Endpoint:** `PUT /api/user/{id}/seat`
    - **Description:** Modify a user's seat.
    - **Request Params:** `section` (String), `seatNumber` (int)

### Project Structure

- **Model:** Contains the `Passenger` entity.
- **Controller:** REST controllers for handling HTTP requests.
- **Service:** Business logic for managing passengers and seats.
- **Repository:** JPA repositories for data access.
- **Configuration:** Configuration files for Spring Boot.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for any feature requests or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
