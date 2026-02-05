# Jolt Playground

A standalone, web-based application for testing and developing Jolt JSON transformation specifications. This is a modernized, executable JAR version of the Jolt Transform Demo.

## Quick Start

**Requirements:** Java 11 or higher

### Run the Application

```bash
# Build the JAR
mvn clean package

# Run with default settings (port 8080, errors + warnings only)
java -jar target/jolt-playground.jar

# Run on custom port
java -jar target/jolt-playground.jar --port 9090

# Run with different logging levels
java -jar target/jolt-playground.jar --port 8080 --info    # Show info messages
java -jar target/jolt-playground.jar --port 8081 --debug   # Full debug output
java -jar target/jolt-playground.jar --port 8082 --error   # Errors only

# View help
java -jar target/jolt-playground.jar --help
```

Then open your browser to **http://localhost:8080** (or your chosen port)

## Features

### ✅ Standalone Executable JAR
- **No dependencies needed** - single JAR file with all dependencies bundled (4.9MB)
- **No Maven/build system required** - run directly with `java -jar`
- **Easy distribution** - share single file with team members

### ✅ Web-Based UI
- **Interactive interface** - paste JSON input and Jolt specs, get real-time transformation results
- **20+ pre-built examples** - learn from working Jolt transformations
- **Syntax formatting** - built-in JSON beautification for readable output
- **Rebranded as "Jolt Playground"** - modernized interface

### ✅ CLI Port Configuration
- **Default port: 8080** - runs immediately without arguments
- **Custom ports** - configure via `-p` or `--port` flag
- **Multi-developer friendly** - run multiple instances on different ports

### ✅ Sophisticated Logging System
- **4 distinct logging modes** - granular control over console output
- **Package-level filtering** - suppresses server infrastructure noise
- **Error tracking with source locations** - shows exact file:line of Jolt errors
- **Flexible for different workflows** - production vs. development modes

## Logging Modes

| Mode | Flag | Output | Best For |
|------|------|--------|----------|
| **Default** | (none) | Jolt errors + warnings only | Production, team demos (clean output) |
| **Error** | `--error` | Jolt errors only | Strict error tracking, CI/CD pipelines |
| **Info** | `--info` | Jolt errors + warnings + info messages | Development, understanding transformations |
| **Debug** | `--debug` | All messages including Jetty server logs | Troubleshooting, infrastructure issues |

### Behavior
- **Default/Error/Info modes**: Only show logs from `com.bazaarvoice.jolt` package
- **Debug mode**: Shows all logs including complete Jetty server output

## Usage Examples

### Basic Usage (Recommended for Team)
```bash
java -jar jolt-playground.jar
# Starts on http://localhost:8080
# Clean output: only Jolt errors and warnings
```

### Multi-Developer Setup
```bash
# Developer 1 - default quiet mode
java -jar jolt-playground.jar --port 8080 &

# Developer 2 - debug mode for troubleshooting  
java -jar jolt-playground.jar --port 8081 --debug &

# Developer 3 - info mode for learning
java -jar jolt-playground.jar --port 8082 --info &
```

### Error Logging Example
When a transformation fails, you see detailed error information:
```
ERROR JoltException [chainElement 1]: JOLT Chainr could not find transform class:invalid
  at com.bazaarvoice.jolt.Chainr (Chainr.java:95)
  at PathEvaluatingTraversal.java:51
```

Line numbers point directly to the Jolt source code where the error occurred.

## Distribution

Share the JAR file (`target/jolt-playground.jar`) with your team:
1. No installation needed - just Java 11
2. No configuration required - runs out of the box
3. Optional: Customize port and logging level as needed

---

## Development & Testing

### Local Testing with Maven (Traditional WAR Development)

For traditional development using the WAR file with Maven Jetty plugin:

```bash
# Build the WAR file
mvn clean package -Pwar

# Run locally with Maven Jetty plugin (auto-reloads on changes)
mvn jetty:run

# Access the application
http://localhost:8080
```

The Jetty Maven plugin watches your source files and automatically reloads changes without rebuilding.

**Configuration:**
- Port: 8080
- Context path: /
- Stop key: STOP (type `mvn jetty:stop` in another terminal)

## Building Different Artifacts

This project supports two Maven profiles for building either a standalone JAR or traditional WAR:

### JAR Profile (Default - Recommended for Distribution)
```bash
# Build executable Jolt Playground JAR
mvn clean package -Pjar

# Or use default (jar is active by default)
mvn clean package

# Result: target/jolt-playground.jar (4.8MB)
java -jar target/jolt-playground.jar
```

### WAR Profile (For Traditional Development with Hot Reload)
```bash
# Build traditional WAR with Maven Jetty plugin
mvn clean package -Pwar

# Result: target/server-0.0.1-SNAPSHOT-web.war (4.6MB)

# Use Maven Jetty plugin for development with auto-reload
mvn jetty:run
# Edit source files → Changes auto-reload (no restart needed!)
# Press Ctrl+C to stop
```

### Build Both
```bash
mvn clean package -Pjar -Pwar
```

## Development Workflow

**For Making Code Changes (Fast Feedback):**
```bash
mvn clean package -Pwar
mvn jetty:run
# Edit JoltServlet.java or other source files
# Jetty automatically reloads - just refresh browser!
# Ctrl+C to stop
```

**For Testing Distribution (What team gets):**
```bash
mvn clean package -Pjar
java -jar target/jolt-playground.jar --port 8080
java -jar target/jolt-playground.jar --info
java -jar target/jolt-playground.jar --debug
```

---

## Legacy: App Engine Java Application

Copyright (C) 2010-2012 Google Inc.

### Historical App Engine Setup

This project was originally configured for Google App Engine deployment.

#### Update 2021-02

Google Free Tier still exists, but they really really really want a credit card to bill.
So need to push an updated version of the app that says "only one instance ever".

Updated to Java11 appEngine that does not want to use a war file, and wants a DropWizard/SpringBoot
style main method class that starts jetty.

Followed https://cloud.google.com/appengine/docs/standard/java11/java-differences
See also https://happycoding.io/tutorials/google-cloud/migrating-to-java-11

#### Original Build & Deploy Instructions

```bash
# Build everything
mvn clean install

# Deploy to App Engine
cd server
mvn clean package appengine:deploy
```

Configure version in `server/pom.xml`:
```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>appengine-maven-plugin</artifactId>
    <version>2.2.0</version>
    <configuration>
        <projectId>jolt-demo</projectId>
        <version>15</version>
    </configuration>
</plugin>
```

#### Reference

https://developers.google.com/appengine/docs/java/tools/maven
http://jersey.java.net/



http://stackoverflow.com/questions/2072295/how-to-deploy-a-jax-rs-application

http://stackoverflow.com/questions/18268827/how-do-i-get-jersey-2-2-jax-rs-to-generate-log-output-including-json-request

Jboss guy article
http://architects.dzone.com/articles/putting-java-rest

