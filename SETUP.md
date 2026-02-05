# Jolt Playground - Setup Guide (Local Env)

Step-by-step instructions to set up and run the Jolt Playground application on your local Mac.

## Overview

**Jolt Playground** is a web application for testing and experimenting with [Jolt](https://github.com/bazaarvoice/jolt) JSON transformations. It offers two deployment models:

- **Standalone JAR** - Single executable file for easy distribution (recommended for teams)
- **WAR with Hot-Reload** - Traditional development workflow with automatic code reload

Choose your path below based on your needs.

## Prerequisites

Before you begin, ensure you have the following installed on your Mac:

### 1. Java Development Kit (JDK) 11

The project requires Java 11. You can install it using Homebrew:

```bash
# Install Homebrew if you don't have it
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java 11
brew install openjdk@11

# Link the Java installation (the path depends on your Mac architecture)
# For Apple Silicon Macs (M1, M2, etc.):
sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk

# For Intel Macs:
# sudo ln -sfn /usr/local/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
```

Verify the installation:

```bash
java -version
```

You should see output similar to:
```
openjdk version "11.0.x" ...
```

### 2. Maven 3.5+

Install Maven using Homebrew:

```bash
brew install maven
```

Verify the installation:

```bash
mvn -version
```

You should see output showing Maven 3.5 or higher.

### 3. Set JAVA_HOME (if needed)

If you have multiple Java versions installed, you may need to set `JAVA_HOME` to point to Java 11:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

You can add this to your shell profile (`~/.zshrc` or `~/.bash_profile`) to make it permanent:

```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 11)' >> ~/.zshrc
source ~/.zshrc
```

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/connect2manu/jolt-demo.git
cd jolt-demo
```

### 2. Set Java Home

Ensure you're using Java 11:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

### 3. Choose Your Setup Path

#### Path A: Standalone JAR (Recommended for Distribution)

```bash
# Build standalone executable JAR
cd jolt-demo/server
mvn clean package

# Run the JAR
java -jar target/jolt-playground.jar
```

Access the application at: **http://localhost:8080**

**Options:**
```bash
# Custom port
java -jar target/jolt-playground.jar --port 9090

# Info-level logging
java -jar target/jolt-playground.jar --info

# Error-only logging
java -jar target/jolt-playground.jar --error

# Full debug logging
java -jar target/jolt-playground.jar --debug

# View all options
java -jar target/jolt-playground.jar --help
```

**Advantages:**
- Single 4.8MB executable file
- No Maven/build tools needed to run
- Perfect for sharing with team
- Configurable logging and port via CLI

#### Path B: WAR with Hot-Reload (Best for Development)

```bash
# Build WAR with Maven Jetty plugin
cd jolt-demo/server
mvn clean package -Pwar

# Run with auto-reload
mvn jetty:run
```

Access the application at: **http://localhost:8080**

Edit source files → changes auto-reload (no rebuild needed!)
Press `Ctrl+C` to stop.

**Advantages:**
- Automatic reload on code changes
- Fast feedback loop during development
- Traditional Maven workflow
- Can deploy WAR to servlet containers (Tomcat, etc.)

#### Path C: Build Both (Complete Workflow)

```bash
# Build both JAR and WAR
cd jolt-demo/server
mvn clean package -Pjar -Pwar
```

This creates:
- `target/jolt-playground.jar` (for distribution)
- `target/server-0.0.1-SNAPSHOT-web.war` (for servlet containers)

## Testing the Application

Once the server is running, you can test it using the provided test script:

```bash
cd server
./test.sh
```

This script will:
1. Send a GET request to check if the server is running
2. Send a POST request with a sample Jolt transformation

You should see the transformed JSON output.

## Development Workflow

### Making Changes (Using WAR Profile)

For fast iteration during development:

1. Build WAR profile once:
   ```bash
   mvn clean package -Pwar
   ```

2. Start the server:
   ```bash
   mvn jetty:run
   ```

3. Edit your code in `server/src/main/java` or `server/src/main/webapp`

4. **Just refresh your browser** - Jetty automatically detects changes!

5. Stop with `Ctrl+C`

### Testing Distribution (Using JAR Profile)

To test how the JAR will work for team distribution:

```bash
# Build JAR
mvn clean package

# Run JAR with various logging levels
java -jar target/jolt-playground.jar --info
java -jar target/jolt-playground.jar --port 9090 --debug
```

### Running Tests

Run the test suite:

```bash
mvn test
```

## Project Structure

```
jolt-demo/
├── pom.xml                 # Parent POM
├── server/                 # Server module
│   ├── pom.xml            # Server POM with dependencies
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/      # Java servlet code
│   │   │   └── webapp/    # Web resources (HTML, examples)
│   └── test.sh            # Test script
└── node/                   # Node.js utilities
```

## Troubleshooting

### Java Version Issues

If you encounter Java version errors:

1. Check your current Java version:
   ```bash
   java -version
   ```

2. List available Java versions:
   ```bash
   /usr/libexec/java_home -V
   ```

3. Set JAVA_HOME to Java 11:
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 11)
   ```

### Build Errors

If Maven fails to build:

1. Clean the project and try again:
   ```bash
   mvn clean install
   ```

2. Ensure you're using Maven 3.5 or higher:
   ```bash
   mvn -version
   ```

### Port Already in Use

If port 8080 is already in use:

1. Find the process using the port:
   ```bash
   lsof -i :8080
   ```

2. Kill the process gracefully:
   ```bash
   kill <PID>
   ```

3. If the process doesn't stop, force kill it:
   ```bash
   kill -9 <PID>
   ```

### Missing Dependencies

If you encounter dependency resolution errors, ensure you have a stable internet connection and try:

```bash
mvn clean install -U
```

The `-U` flag forces Maven to update snapshots and releases.

## Deployment Options Comparison

| Feature | JAR (Standalone) | WAR (Development) |
|---------|-----------------|-------------------|
| **Build Command** | `mvn clean package` | `mvn clean package -Pwar` |
| **Run Command** | `java -jar jolt-playground.jar` | `mvn jetty:run` |
| **Distribution** | Single 4.8MB file | Requires Maven/Jetty |
| **Hot Reload** | No (need rebuild) | Yes (automatic) |
| **Logging Control** | CLI flags (--info, --debug) | Via SLF4J config |
| **Port Config** | `--port` flag | Default 8080 |
| **Best For** | Teams, production, sharing | Development, testing |
| **Deploy To** | Any machine with Java 11 | Servlet containers |

## Detailed Server Documentation

For comprehensive information about:
- **Build profiles** (JAR vs WAR)
- **Logging modes** (error, warn, info, debug)
- **CLI options** (port, logging configuration)
- **Error tracking** with source line numbers
- **Distribution** to team members

See: **[server/README.md](server/README.md)**

## Additional Resources

- [Jolt GitHub Repository](https://github.com/bazaarvoice/jolt)
- [Jolt Documentation](https://github.com/bazaarvoice/jolt/blob/master/README.md)
- [Google App Engine Documentation](https://cloud.google.com/appengine/docs/standard/java11)
- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)

## Next Steps

Once you have the application running:

1. Open http://localhost:8080 in your browser
2. Experiment with different Jolt transformations using the web interface
3. Check out the examples in `server/src/main/webapp/examples/`
4. Read the [Jolt documentation](https://github.com/bazaarvoice/jolt) to learn more about transformations
5. Explore logging options: try `java -jar target/jolt-playground.jar --info` to see detailed transform messages

## Contributing

If you'd like to contribute to this project:

1. Fork the repository
2. Create a feature branch
3. Make your changes using the WAR profile for hot-reload development
4. Submit a pull request

## Support

For issues and questions:

- Check the [Jolt documentation](https://github.com/bazaarvoice/jolt)
- Review the examples in the `server/src/main/webapp/examples/` directory
- See [server/README.md](server/README.md) for logging and deployment questions
- Open an issue in the GitHub repository
