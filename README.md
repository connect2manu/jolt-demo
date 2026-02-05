# Jolt Playground Demo

Demo and documentation site for [Jolt](https://github.com/bazaarvoice/jolt) JSON transformations.

**Live Demo:** http://jolt-demo.appspot.com

## Quick Start (Choose Your Path)

### 🚀 Option 1: Standalone JAR (Recommended for Team Distribution)

Build and run as a single, portable executable JAR:

```bash
# Prerequisites: Java 11 and Maven 3.5+
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
cd server

# Build standalone JAR
mvn clean package

# Run
java -jar target/jolt-playground.jar
```

Then open http://localhost:8080

**Advantages:**
- Single 4.8MB file - easy to share with team
- No Maven/build tools needed to run
- Works out of the box with `java -jar`
- Customizable logging levels and ports

### 🔧 Option 2: WAR with Hot-Reload (Best for Development)

Develop and test with automatic reload on code changes:

```bash
cd server

# Build WAR
mvn clean package -Pwar

# Run with Maven Jetty plugin (auto-reloads changes)
mvn jetty:run
```

Then open http://localhost:8080

**Advantages:**
- Edit code → auto-reloads (no rebuild needed)
- Fast feedback loop during development
- Traditional Maven workflow
- Deploy WAR to any servlet container

## Setup & Documentation

**📖 [Detailed Setup Guide →](SETUP.md)** — Complete prerequisites and setup instructions

**📖 [Server/Jolt Playground Guide →](server/README.md)** — Build profiles, logging modes, CLI options, and deployment guide

## Available Deployment Options

| Option | Command | Output | Best For |
|--------|---------|--------|----------|
| **JAR (Default)** | `mvn clean package` | `jolt-playground.jar` | Distribution, team sharing, production |
| **WAR** | `mvn clean package -Pwar` | `server-*.war` | Development, hot-reload, servlet containers |
| **Both** | `mvn clean package -Pjar -Pwar` | Both artifacts | Complete development & release workflow |

## Project Structure

```
jolt-demo/
├── server/                          # Jolt Playground application
│   ├── README.md                   # Complete server documentation
│   ├── pom.xml                     # Maven build with JAR/WAR profiles
│   ├── src/main/java/             # Java servlet code
│   ├── src/main/webapp/           # Web UI and examples
│   └── target/
│       ├── jolt-playground.jar    # Standalone executable JAR
│       └── server-*.war           # Traditional WAR file
├── node/                           # Node.js utilities
└── SETUP.md                        # Setup and installation guide
```

## Tech Stack

- **Language:** Java 8 (source/target), Java 11+ (runtime)
- **Build:** Maven 3.5+
- **Jolt:** v0.1.8
- **Server:** Embedded Jetty 9.4 (JAR) or Maven Jetty plugin (WAR)
- **Logging:** SLF4J with configurable levels (error, info, debug)

## Features

✅ **Interactive Web UI** — Real-time JSON transformation testing
✅ **20+ Examples** — Learn from pre-built transformations
✅ **CLI Configuration** — Customize port and logging from command line
✅ **Sophisticated Logging** — Granular control with error location tracking
✅ **Easy Distribution** — Single JAR file, no dependencies needed
✅ **Developer-Friendly** — Hot-reload, detailed error messages with line numbers

