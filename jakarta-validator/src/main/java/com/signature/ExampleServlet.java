package com.signature;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/file")
public class ExampleServlet {

    @POST
    public Response method(@Context HttpServletRequest request) {
        System.out.println("File Received");
        return Response.ok("File Received").build();
    }

}
