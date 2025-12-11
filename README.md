# DataWarehouseReader

The micro service provides information for different data domains (for example agreement, customer, installed base, invoices).

## Getting Started

### Prerequisites

- **Java 25 or higher**
- **Maven**
- **Git**
- **[Dependent Microservices](#dependencies)**

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Sundsvallskommun/api-service-datawarehousereader.git
   cd api-service-datawarehousereader
   ```
2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   If this microservice depends on other services, make sure they are up and accessible. See [Dependencies](#dependencies) for more details.

4. **Build and run the application:**

   - Using Maven:

     ```bash
     mvn spring-boot:run
     ```
   - Using Gradle:

     ```bash
     gradle bootRun
     ```

## Dependencies

The microservice depends on the following services:

- **Party**
  - **Purpose:** Service is used for translating between party id and legal id
  - **Repository:** [https://github.com/Sundsvallskommun/api-service-party](https://github.com/Sundsvallskommun/api-service-party)
  - **Setup Instructions:** See documentation in repository above for installation and configuration steps.

The microservice depends on the following database integrations:

- ** MS SQL database**
  - **Purpose:** Database where domain data is stored
  - **Repository:** External database managed by third party

Ensure that microservices and databases that this microservice uses are properly configured and running before starting this microservice.

## API Documentation

Access the API documentation via Swagger UI:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

Alternatively, see the `openapi.yml` file located in directory `/src/test/resources/api` for the OpenAPI specification.

## Usage

### API Endpoints

See [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X 'GET'
  'http://localhost:8080/2281/invoices?page=1&invoiceNumber=230501397' -H 'accept: application/json'
```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in `application.yml`.

### Key Configuration Parameters

- **Server Port:**

  ```yaml
  server:
    port: 8080
  ```
- **External dependencies configurations:**

  ```yaml
  integration:
      party:
          url: <dependency service url>
  spring:
      datasource:
          url: jdbc:sqlserver://<server address>:<port>;databaseName=<database name>;trustServerCertificate=true
          username:<database user name>
          password:<database user password>
      security:
          oauth2:
              client:
                  provider:
                      party:
                          token-uri: <token url>
                  registration:
                      party:
                          client-id: <client id>
                          client-secret: <client secret>
                          authorization-grant-type: client_credentials
                          provider: party
      transaction:
          default-timeout: <default timeout in seconds>
  ```

### Additional Notes

- **Application Profiles:**

  Use Spring profiles (`dev`, `prod`, etc.) to manage different configurations for different environments.

- **Logging Configuration:**

  Adjust logging levels if necessary.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](https://github.com/Sundsvallskommun/.github/blob/main/.github/CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

## Status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-datawarehousereader&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-datawarehousereader)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-datawarehousereader&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-datawarehousereader)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-datawarehousereader&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-datawarehousereader)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-datawarehousereader&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-datawarehousereader)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-datawarehousereader&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-datawarehousereader)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-datawarehousereader&metric=bugs)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-datawarehousereader)

## 

&copy; 2023 Sundsvalls kommun
