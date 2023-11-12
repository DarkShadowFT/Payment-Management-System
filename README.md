# Payment System Gateway

Welcome to the Payment System Gateway repository! This Spring Boot application serves as a seamless gateway between banks, facilitating secure and efficient payment processing.

## Best Industry Practices

This Payment System Gateway project adheres to the best industry practices, ensuring robustness, maintainability, and adherence to coding standards.

### Key Features

- 🌐 **Swagger Specs:** The application includes Swagger specifications for easy API exploration and testing.

- 🔄 **Liquibase Scripts:** Database changes are managed seamlessly with Liquibase scripts, ensuring efficient database version control.

- 🔐 **Authorized Endpoints with Roles:** The application incorporates authorized endpoints with two roles: `user` and `admin` for controlled access.

### Implemented Features

✅ Gained proficiency in Spring Boot and Liquibase.

✅ GitLab became my coding companion.

✅ Worked on Fedora OS.

✅ Leveled up my Swagger skills with practical application.

✅ Implemented clean code and SOLID principles.

✅ Utilized DTOs generated with openapi-swagger-maven-plugin.

✅ Implemented Spring Security with basic auth and cookies.

✅ Navigated through Spring Security 6, even without a guide!

✅ Achieved 100% test coverage with JPA, unit, and integration tests using JUnit and Mockito.

✅ Embraced code management with separate branches for each subtask.

### Clean Code

The codebase is developed with a strong emphasis on clean code principles. Here are some highlights:

- ✨ **Modularity:** Code is organized into modular components, promoting readability and ease of maintenance.
  
- 🔄 **Consistent Naming Conventions:** Meaningful and consistent naming conventions enhance code comprehension.

- 🌐 **SOLID Principles:** The code follows SOLID principles to ensure scalability, maintainability, and flexibility.

- 🛠️ **Code Quality:** Regular code reviews and automated testing contribute to maintaining high code quality.

## REST APIs

Explore the user-friendly REST APIs provided by the Payment System Gateway for seamless payment management.

### Banks

- ➕ **Create a new Bank**
  - Endpoint: `/api/banks`
  - Method: `POST`

- 📋 **Get list of Banks**
  - Endpoint: `/api/banks`
  - Method: `GET`

### Accounts

- ➕ **Create an account (Sign up)**
  - Endpoint: `/api/accounts`
  - Method: `POST`

- 📋 **Get list of accounts**
  - Endpoint: `/api/accounts`
  - Method: `GET`

- 🔍 **Get details of an account**
  - Endpoint: `/api/accounts/{accountId}`
  - Method: `GET`

- ✏️ **Update account information**
  - Endpoint: `/api/accounts/{accountId}`
  - Method: `PUT`

- 🗑️ **Delete account**
  - Endpoint: `/api/accounts/{accountId}`
  - Method: `DELETE`

- 📜 **Get payment history of an account**
  - Endpoint: `/api/accounts/{accountId}/payments`
  - Method: `GET`

### Balance

- 💰 **Get account balance**
  - Endpoint: `/api/accounts/{accountId}/balance`
  - Method: `GET`

### Payments

- ➕ **Create a payment between two registered accounts**
  - Endpoint: `/api/payments`
  - Method: `POST`

- 📋 **Get all payments**
  - Endpoint: `/api/payments`
  - Method: `GET`

- 🔍 **Get payment details of provided payment id**
  - Endpoint: `/api/payments/{paymentId}`
  - Method: `GET`

- 🔄 **Get payment status of provided payment id**
  - Endpoint: `/api/payments/{paymentId}/status`
  - Method: `GET`

## Getting Started

### Prerequisites

Make sure you have the following installed on your machine:

- ☕ [Java](https://www.oracle.com/java/technologies/javase-downloads.html)
- 🛠️ [Maven](https://maven.apache.org/download.cgi)
- 📂 [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/payment-system-gateway.git
    ```

2. Navigate to the project folder:

    ```bash
    cd payment-system-gateway
    ```

3. Build the project:

    ```bash
    mvn clean install
    ```

4. Run the application:

    ```bash
    java -jar target/payment-system-gateway.jar
    ```

5. Access the application at [http://localhost:8080](http://localhost:8080)
