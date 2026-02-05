package com.bazaarvoice.jolt;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URL;

/**
 * Main entry point for running Jolt Playground as a standalone executable JAR.
 * 
 * Usage:
 *   java -jar jolt-playground.jar [options]
 * 
 * Options:
 *   -p, --port <port>    Server port (default: 8080)
 *   --error              Log only errors from Jolt (package: com.bazaarvoice.jolt)
 *   --info               Log info+ messages from Jolt
 *   --debug              Log all debug messages (includes server logs)
 *   -h, --help           Show this help message
 * 
 * Default (no log flag): Shows errors and warnings from Jolt only
 * 
 * Examples:
 *   java -jar jolt-playground.jar
 *   java -jar jolt-playground.jar --port 9090
 *   java -jar jolt-playground.jar --info
 *   java -jar jolt-playground.jar --debug
 *   java -jar jolt-playground.jar -p 9090 --error
 */
public class Main {

    private static final int DEFAULT_PORT = 8080;
    
    private enum LogLevel {
        ERROR("error"),
        WARN("warn"),
        INFO("info"),
        DEBUG("debug");
        
        final String level;
        LogLevel(String level) { this.level = level; }
    }

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        LogLevel logLevel = LogLevel.WARN;  // Default: errors and warnings only from Jolt
        
        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            
            if ("-h".equals(arg) || "--help".equals(arg)) {
                printHelp();
                return;
            }
            
            if ("-p".equals(arg) || "--port".equals(arg)) {
                if (i + 1 < args.length) {
                    try {
                        port = Integer.parseInt(args[i + 1]);
                        if (port < 1 || port > 65535) {
                            System.err.println("Error: Port must be between 1 and 65535");
                            System.exit(1);
                        }
                        i++; // Skip the port value in next iteration
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Invalid port number: " + args[i + 1]);
                        System.exit(1);
                    }
                } else {
                    System.err.println("Error: --port requires a value");
                    System.exit(1);
                }
            }
            
            if ("--error".equals(arg)) {
                logLevel = LogLevel.ERROR;
            } else if ("--info".equals(arg)) {
                logLevel = LogLevel.INFO;
            } else if ("--debug".equals(arg)) {
                logLevel = LogLevel.DEBUG;
            }
        }
        
        // Configure logging
        configureLogging(logLevel);
        
        startServer(port);
    }

    private static void startServer(int port) throws Exception {
        Server server = new Server(port);

        // Create servlet context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Set base resource to webapp folder in classpath (from JAR)
        URL webappUrl = Main.class.getClassLoader().getResource("webapp");
        if (webappUrl != null) {
            context.setBaseResource(Resource.newResource(webappUrl));
        } else {
            // Fallback for development: use src/main/webapp
            context.setBaseResource(Resource.newResource("src/main/webapp"));
        }

        // Add JoltServlet at /transform
        ServletHolder joltHolder = new ServletHolder("jolt", JoltServlet.class);
        context.addServlet(joltHolder, "/transform");

        // Add DefaultServlet for static files (index.html, examples/, lib/)
        ServletHolder defaultHolder = new ServletHolder("default", DefaultServlet.class);
        defaultHolder.setInitParameter("dirAllowed", "false");
        defaultHolder.setInitParameter("welcomeServlets", "false");
        defaultHolder.setInitParameter("redirectWelcome", "false");
        context.addServlet(defaultHolder, "/");

        // Set welcome file
        context.setWelcomeFiles(new String[]{"index.html"});

        server.setHandler(context);

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║             Jolt Playground Starting                   ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║  URL: http://localhost:" + port + padRight("", 37 - String.valueOf(port).length()) + "║");
        System.out.println("║  Press Ctrl+C to stop                                  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        server.start();
        server.join();
    }

    private static void configureLogging(LogLevel logLevel) {
        // Suppress verbose logging from Jetty and other libraries by default
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        
        // Configure Jolt package logging
        System.setProperty("org.slf4j.simpleLogger.log.com.bazaarvoice.jolt", logLevel.level);
        
        // Show timestamps and format for all messages
        System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
        System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "HH:mm:ss.SSS");
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "false");
        System.setProperty("org.slf4j.simpleLogger.showLogName", "false");
        
        // For full debug mode, show all logs including Jetty
        if (logLevel == LogLevel.DEBUG) {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
            System.out.println("Full debug logging enabled (all packages)\n");
        } else {
            System.out.println("Logging level: " + logLevel.level + " (Jolt package only)\n");
        }
    }

    private static void printHelp() {
        System.out.println("Jolt Playground - JSON transformation playground");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar jolt-playground.jar [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -p, --port <port>    Server port (default: 8080)");
        System.out.println("  --error              Log only errors from Jolt");
        System.out.println("  --info               Log info+ messages from Jolt");
        System.out.println("  --debug              Log all debug messages (includes server logs)");
        System.out.println("  -h, --help           Show this help message");
        System.out.println();
        System.out.println("Logging Levels:");
        System.out.println("  (default)            Show errors and warnings from Jolt only");
        System.out.println("  --error              Show only errors");
        System.out.println("  --info               Show info, warnings, and errors");
        System.out.println("  --debug              Show debug messages + server logs");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar jolt-playground.jar");
        System.out.println("  java -jar jolt-playground.jar --port 9090");
        System.out.println("  java -jar jolt-playground.jar --info");
        System.out.println("  java -jar jolt-playground.jar --debug");
        System.out.println("  java -jar jolt-playground.jar -p 9090 --error");
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
