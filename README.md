jolt-demo
=========

Demo and Doc site for Jolt

# Demo Server

http://jolt-demo.appspot.com

# Local Setup

**📖 [Complete Setup Guide for macOS →](SETUP.md)**

For detailed instructions on setting up and running this project on your local Mac, see the [SETUP.md](SETUP.md) guide.

Quick start:
```bash
# Prerequisites: Java 11 and Maven 3.5+
export JAVA_HOME=$(/usr/libexec/java_home -v 11)

# Build
mvn clean package

# Run (legacy method)
cd server
mvn appengine:devserver
```

Then open http://localhost:8080 in your browser.

# Tech

Google App Engine has a truly free tier, so building a small war so that people can play with and deploying there.

