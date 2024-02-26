# CRUD-Java

The repository contains a simple CRUD application with two entities (Providers and Services) having a many-to-many relationship.

## Getting Started

Follow the steps below to run the CRUD Java application:

### Prerequisites

Make sure you have the following installed:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Gradle](https://gradle.org/install/)
- [PostgreSQL](https://www.postgresql.org/download/)

### Database Configuration

1. Create a new `.env` file based on the provided `.env.example` file.

   ```env
   DB_USERNAME=test
   DB_PASSWORD=test
   ```

2. Run Gradle build to compile the application. Open a terminal and navigate to the project directory, then run:

   ```
   ./gradlew build
   ```

3. Execute database migrations using Gradle and Flyway. Run the following command:

   ```
   ./gradlew flywayMigrate
   ```

4. Finally, run the application using the following Gradle command:

   ```
   ./gradlew bootRun
   ```

5. Application should be available on:
   ```
   http://localhost:8080
   ```
