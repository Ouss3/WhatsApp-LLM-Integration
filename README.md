# Project Setup and Installation

## Prerequisites

- Docker
- Docker Compose

## Project Description

This project interacts with the WhatsApp API to receive user messages. Based on the patterns selected by the user, it generates a response using a Large Language Model (LLM) with GitHub Copilot or fetches other user data from other servers by calling the gateway. Apache Camel is used for integration and managing the process.

## Services

This project consists of the following services:

- Discovery Service
- Config Service
- Integration Service
- Client Service
- Product Service
- Gateway Service

## Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/Ouss3/WhatsApp-LLM-Integration
    cd <repository-directory>
    ```

2. **Build and start the services:**

    ```sh
    docker-compose up --build
    ```

    This command will build and start all the services defined in the `docker-compose.yml` file.

## Services Details

### Discovery Service

- **Port:** 8761
- **Health Check:** `http://localhost:8761/actuator/health`

### Config Service

- **Port:** 9999
- **Health Check:** `http://localhost:9999/actuator/health`

### Integration Service

- **Port:** 8085
- **Health Check:** `http://localhost:8085/actuator/health`

### Client Service

- **Port:** 8090
- **Health Check:** `http://localhost:8090/actuator/health`

### Product Service

- **Port:** 8091
- **Health Check:** `http://localhost:8091/actuator/health`

### Gateway Service

- **Port:** 8888
- **Health Check:** `http://localhost:8888/actuator/health`

## Generating Tokens

### GitHub Token

1. Go to [GitHub Settings](https://github.com/settings/tokens).
2. Click on **Generate new token**.
3. Select the scopes or permissions you need.
4. Click **Generate token**.
5. Copy the generated token and add it to the `application.properties` file:

    ```ini
    GITHUB_TOKEN=your_generated_github_token
    ```

### WhatsApp Token

1. Go to the [Facebook Developers](https://developers.facebook.com/) page.
2. Create a new app or select an existing app.
3. Navigate to **WhatsApp** and set up your WhatsApp Business API.
4. Generate an access token.
5. Copy the generated token and add it to the `application.properties` file:

    ```ini
    whatsapp.access_token=your_generated_whatsapp_token
    ```

## Stopping the Services

To stop the services, run:

```sh
docker-compose down