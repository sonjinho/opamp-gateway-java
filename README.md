# OpenTelemetry OpAMP Gateway

A server implementation of the Open Agent Management Protocol (OpAMP) for OpenTelemetry. This project acts as a gateway for agents to connect and be managed remotely.

## ✨ Features

*   **OpAMP Protocol:** Implements the server-side of the OpAMP specification.
*   **WebSocket Communication:** Uses WebSockets for real-time, bidirectional communication with agents.
*   **Agent Management:** Provides endpoints for agent lifecycle management, configuration updates, and status reporting.
*   **Persistence:** Stores agent data and configurations using JPA.

## 🛠️ Technologies Used

*   **Java 17**
*   **Spring Boot 3**
*   **Gradle**
*   **Spring Web & WebSocket**
*   **Spring Data JPA**
*   **Google Protocol Buffers**
*   **MySQL** (can be configured for others)

## 🚀 Getting Started

### Prerequisites

*   JDK 17 or later
*   Gradle 8.x

### Building the Project

1.  Clone the repository:
    ```bash
    git clone https://github.com/gclswhub/opamp.git
    cd opamp
    ```

2.  Build the application using Gradle:
    ```bash
    ./gradlew build
    ```
    This will compile the source code, run tests, and package the application into a JAR file.

### Running the Application

You can run the application in two ways:

1.  **Using the JAR file:**
    ```bash
    java -jar build/libs/opamp-0.0.1-SNAPSHOT.jar
    ```

2.  **Using the Gradle wrapper:**
    ```bash
    ./gradlew bootRun
    ```

The server will start on the port configured in `src/main/resources/application.yml` (default is 8080).

## System Architecture

```aiexclude
           ┌────────────────────────────┐
           │        opamp-ui (Web UI)   │
           │ https://github.com/sonjinho/opamp-ui │
           └───────────────┬────────────┘
                           │ HTTP / WebSocket
                           ▼
             ┌────────────────────────────┐
             │   OpAMP Gateway (Server)   │
             │  - Spring Boot App          │
             │  - WebSocket OpAMP Server   │
             │  - REST API for UI          │
             └───────────────┬────────────┘
                             │
             ┌───────────────┴──────────────┐
             │                              │
┌────────────────────┐          ┌────────────────────┐
│   Agent Instance 1 │          │   Agent Instance N │
│ (OpAMP Client SDK) │  ...     │ (OpAMP Client SDK) │
└────────────────────┘          └────────────────────┘

            ▼
       Database (MySQL / File)
```

## 📁 Project Structure

```
.
├── gradle/         # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/   # Java source code
│   │   │   └── io/opentelemetry/opamp/
│   │   │       ├── agent/      # Agent-specific logic
│   │   │       ├── common/     # Common utilities
│   │   │       ├── config/     # Spring configuration
│   │   │       └── gateway/    # OpAMP gateway implementation
│   │   ├── proto/      # Protocol Buffer definitions (*.proto)
│   │   └── resources/  # Application properties and schema
│   └── test/           # Test sources
├── build.gradle    # Project dependencies and build configuration
└── README.md       # This file
```

### Env

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=opamp
DB_USER=opamp
DB_PASSWORD=opamp
#DB or File
REQUEST_PERSISTENCE=FILE

```


## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
