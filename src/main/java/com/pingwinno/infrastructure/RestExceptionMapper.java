package com.pingwinno.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        log.error("toResponse() caught exception", exception);

        return Response.status(getStatusCode(exception))
                .entity(getEntity(exception))
                .build();
    }

    /*
     * Get appropriate HTTP status code for an exception.
     */
    private int getStatusCode(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse().getStatus();
        }

        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    /*
     * Get response body for an exception.
     */
    private Object getEntity(Throwable exception) {
        // return stack trace for debugging (probably don't want this in prod...)
        StringWriter errorMsg = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorMsg));
        return errorMsg.toString();
    }
}