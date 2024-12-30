package com.signature.util;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class JaxRsApplication extends ResourceConfig {

    public JaxRsApplication() {
        packages("com.signature.ws");
        register(JacksonFeature.class);
    }
}