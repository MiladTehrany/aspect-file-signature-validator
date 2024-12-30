package com.signature;


import org.apache.catalina.startup.Tomcat;
import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;

public class Application {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHttpPort(8080);
        try (final Container container = new Container(config).deployClasspathAsWebApp()) {
            // FIXME: tomcat scan classpath for the second time while jasRsApplication has registered resources
            Tomcat tomcat = container.getTomcat();
            System.out.println("Tomcat started on: http://" + tomcat.getHost().getName() + ":" + config.getHttpPort());
            tomcat.getServer().await();
        }
    }
}
