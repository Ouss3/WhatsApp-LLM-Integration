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

## Exposing the Server to the Internet

To allow the WhatsApp API to reach your server, you can use **Ngrok** to expose your local services to the internet.

### Installing Ngrok

1. **Download Ngrok**:
   - Visit the [Ngrok website](https://ngrok.com/download).
   - Download the version compatible with your operating system.

2. **Install Ngrok**:
   - Extract the downloaded file.
   - Add the Ngrok executable to your system's PATH (optional but recommended).

3. **Verify Installation**:
   - Run the following command to check if Ngrok is installed:
     ```sh
     ngrok version
     ```

### Exposing Your Server Using Ngrok

1. **Start Ngrok**:
   - Run the following command to expose the Gateway Service (running on port `8888`):
     ```sh
     ngrok http 8888
     ```

2. **Copy the Public URL**:
   - After running the command, Ngrok will display a public URL that forwards traffic to your local server. For example:
     ```
     Forwarding                    https://<random-id>.ngrok.io -> http://localhost:8888
     ```

3. **Update WhatsApp Webhook URL**:
   - Use the Ngrok public URL to configure the WhatsApp webhook in the [Facebook Developers Console](https://developers.facebook.com/). For example:
     ```
     https://<random-id>.ngrok.io/webhook
     ```

4. **Run Your Services**:
   - Start your services as usual using:
     ```sh
     docker-compose up --build
     ```

### Notes

- Ngrok URLs change every time you restart Ngrok. To get a persistent public URL, consider using the **Ngrok Pro** plan.
- Always use `https` endpoints provided by Ngrok to meet WhatsApp API security requirements.


## Stopping the Services

To stop the services, run:

```sh
docker-compose down