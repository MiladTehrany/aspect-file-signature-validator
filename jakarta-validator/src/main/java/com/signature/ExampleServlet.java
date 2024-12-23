package com.signature;

import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/storage")
public class ExampleServlet {

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response method(@Context HttpServletRequest request) {
        System.out.println("File Received");
        return Response.ok("File Received").build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFile(@Context HttpServletRequest request) {
        System.out.println("Show Files");
        return Response.ok("Show Files").build();
    }

}
