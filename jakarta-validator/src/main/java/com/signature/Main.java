package com.signature;


import jakarta.ejb.Stateless;
import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;

@Stateless
public class Main {
    public static void main(String[] args) throws Exception {
        try (final Container container = new Container(new Configuration()).deployClasspathAsWebApp()) {
            System.out.println("Started on http://localhost:" + container.getConfiguration().getHttpPort());
            Thread.currentThread().join();
        }
    }
}
