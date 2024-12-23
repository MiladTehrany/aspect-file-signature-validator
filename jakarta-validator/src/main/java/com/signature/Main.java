package com.signature;


import org.apache.tomee.embedded.Container;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Container container = new Container()) {
            container.start();
            container.deployClasspathAsWebApp("/", null);
            System.out.println("TomEE Embedded Server started at http://localhost:23880");

            // Keep the server running
            Thread.currentThread().join();
        }
    }
}
