package com.bazaarvoice.jolt;

import com.bazaarvoice.jolt.exception.JoltException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * With the move to the Java8 App Engine, I had a hell of a time
 *  trying to get JAX-RS stuff to work.   So, nevermind.
 *
 * Just do the simplest, stupidest, BOG standard Servlet.
 */
public class JoltServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JoltServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        response.getWriter().println("Pong");
    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {

        resp.setContentType("text/plain");

        String inputString, specString;
        Boolean sort;

        try {
            inputString = req.getParameter( "input" );

            specString = req.getParameter( "spec" );

            sort = Boolean.valueOf( req.getParameter( "sort" ) );
        }
        catch ( Exception e ) {
            String errorMsg = "Could not url-decode the inputs.";
            logger.error(errorMsg, e);
            resp.getWriter().println( errorMsg + "\n");
            return;
        }

        Object input, spec;

        try {
            input = JsonUtils.jsonToObject( inputString );
        }
        catch ( Exception e ) {
            String errorMsg = "Could not parse the 'input' JSON.";
            logger.error(errorMsg + " Input: " + inputString, e);
            resp.getWriter().println( errorMsg + "\n" );
            return;
        }

        try {
            spec = JsonUtils.jsonToObject( specString );
        }
        catch ( Exception e ) {
            String errorMsg = "Could not parse the 'spec' JSON.";
            logger.error(errorMsg + " Spec: " + specString, e);
            resp.getWriter().println( errorMsg + "\n" );
            return;
        }

        logger.debug("Starting Jolt transform. Input: {}, Spec: {}", inputString, specString);
        String result = doTransform( input, spec, sort );
        resp.getWriter().println( result );
    }


    private String doTransform( Object input, Object spec, boolean doSort ) throws IOException {

        try {
            logger.debug("Creating Chainr from spec");
            Chainr chainr = Chainr.fromSpec( spec );

            logger.debug("Executing transform");
            Object output = chainr.transform( input );

            if ( doSort ) {
                logger.debug("Sorting output");
                output = Sortr.sortJson( output );
            }

            String result = JsonUtils.toPrettyJsonString( output );
            logger.debug("Transform successful. Output: {}", result);
            return result;
        }
        catch ( Exception e ) {

            StringBuilder sb = new StringBuilder();
            sb.append( "Error running the Transform.\n\n" );

            // Walk up the stackTrace printing the message for any JoltExceptions.
            Throwable exception = e;
            int exceptionCount = 0;
            do {
                if ( exception instanceof JoltException ) {
                    String msg = exception.getMessage();
                    sb.append( msg );
                    sb.append( "\n\n");
                    logger.error("JoltException [" + (++exceptionCount) + "]: " + msg + " at " + exception.getStackTrace()[0], exception);
                } else {
                    logger.error("Exception during transform: " + exception.getClass().getName(), exception);
                }

                exception = exception.getCause();
            }
            while( exception != null );

            logger.error("Transform failed", e);
            return sb.toString();
        }
    }
}