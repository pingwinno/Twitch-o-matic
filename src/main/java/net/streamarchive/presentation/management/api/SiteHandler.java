package net.streamarchive.presentation.management.api;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Service
@Path("/")
public class SiteHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndexPage() {

        ClassLoader classLoader = getClass().getClassLoader();

        return Response.ok().entity(
                (classLoader.getResourceAsStream("www/index.html")))
                .build();
    }

    @Path("{filename}")
    @GET
    @Produces(MediaType.WILDCARD)
    public Response getPage(@PathParam("filename") String fileName) {


        ClassLoader classLoader = getClass().getClassLoader();

        return Response.ok().entity(classLoader.getResourceAsStream(
                "www/" + fileName)).build();


    }
}
