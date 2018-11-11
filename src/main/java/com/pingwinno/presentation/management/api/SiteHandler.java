package com.pingwinno.presentation.management.api;

import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/")
public class SiteHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndexPage() {

        ClassLoader classLoader = getClass().getClassLoader();

        try {
            return Response.ok().entity(Files.newInputStream(Paths.get(classLoader.getResource("index.html").getFile())))
                    .build();
        } catch (IOException e) {
            log.error("Page upload failed");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("{filename}")
    @GET
    @Produces(MediaType.WILDCARD)
    public InputStream getPage(@PathParam("filename") String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        try {
            return Files.newInputStream(Paths.get(classLoader.getResource(fileName).getFile()));
        } catch (IOException e) {
            log.error("Page upload failed");
            return null;
        }


    }
}
