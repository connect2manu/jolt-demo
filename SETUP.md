# Local Setup Guide for macOS

This guide provides step-by-step instructions to set up and run the Jolt Demo application on your local Mac.

## Overview

Jolt Demo is a web application that provides an interactive interface for testing and experimenting with [Jolt](https://github.com/bazaarvoice/jolt) JSON transformations. The application is built with Java and Maven, and is designed to run on Google App Engine.

## Prerequisites

Before you begin, ensure you have the following installed on your Mac:

### 1. Java Development Kit (JDK) 11

The project requires Java 11. You can install it using Homebrew:

```bash
# Install Homebrew if you don't have it
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java 11
brew install openjdk@11

# Link the Java installation
sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
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

### 2. Build the Project

Build the WAR file for the server:

```bash
# Ensure you're using Java 11
export JAVA_HOME=$(/usr/libexec/java_home -v 11)

# Clean and build
mvn clean package
```

The build will create a WAR file at `server/target/server-0.0.1-SNAPSHOT.war`.

**Note:** You may see a warning about a missing `simple-jetty-main` dependency. This is expected and doesn't prevent building the WAR file, as this dependency is only needed for runtime on Google App Engine.

### 3. Run the Application Locally

There are two main options for running the application locally:

#### Option A: Using Maven AppEngine Plugin (Legacy)

This option uses the older AppEngine development server:

```bash
cd server
mvn appengine:devserver
```

Then open your browser to:
```
http://localhost:8080
```

#### Option B: Using an External Jetty Server (Recommended for Java 11)

For Java 11 compatibility, you can use the Google Cloud App Engine Java 11 migration approach. This requires downloading and setting up an external Jetty runner.

1. Clone the Google samples repository (do this in a separate directory, not inside jolt-demo):

```bash
cd ~/code  # or wherever you keep your projects
git clone https://github.com/GoogleCloudPlatform/java-docs-samples.git
cd java-docs-samples/appengine-java11/appengine-simple-jetty-main
mvn clean install
```

2. Run the Jolt Demo WAR using the Jetty runner:

```bash
cd ~/code/java-docs-samples/appengine-java11/appengine-simple-jetty-main
mvn exec:java -Dexec.args="<path-to-jolt-demo>/server/target/server-0.0.1-SNAPSHOT.war"
```

Replace `<path-to-jolt-demo>` with the actual path to your jolt-demo directory, for example:

```bash
mvn exec:java -Dexec.args="$HOME/code/jolt-demo/server/target/server-0.0.1-SNAPSHOT.war"
```

Then open your browser to:
```
http://localhost:8080
```

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

### Making Changes

1. Make your code changes in `server/src/main/java` or `server/src/main/webapp`
2. Rebuild the project:
   ```bash
   mvn clean package
   ```
3. Restart the server to see your changes

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

2. Kill the process:
   ```bash
   kill -9 <PID>
   ```

### Missing Dependencies

If you encounter dependency resolution errors, ensure you have a stable internet connection and try:

```bash
mvn clean install -U
```

The `-U` flag forces Maven to update snapshots and releases.

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

## Contributing

If you'd like to contribute to this project:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## Support

For issues and questions:

- Check the [Jolt documentation](https://github.com/bazaarvoice/jolt)
- Review the examples in the `server/src/main/webapp/examples/` directory
- Open an issue in the GitHub repository
