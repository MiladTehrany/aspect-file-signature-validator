package com.signature.ws;

import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;

@RequestScoped
@Path("/storage")
@Slf4j
public class StorageResource {

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response method(@Context HttpServletRequest request,
                           @FormDataParam("file") InputStream file) {
        log.info("File received ...");
        // FIXME: responds with 404 page while returning JSON
        return Response.ok("File Received").build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFile(@Context HttpServletRequest request) {
        log.info("Showing files ...");
        return Response.ok("Show Files").build();
    }

}
