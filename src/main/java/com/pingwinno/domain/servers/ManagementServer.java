package com.pingwinno.domain.servers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.SettingsProperties;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;

public class ManagementServer {
    private static Server server;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ManagementServer.class.getName());
    public static void start() {
        server = new Server(SettingsProperties.getManagementServerPort());
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/");
        server.setHandler(ctx);
        log.debug("Running management server...");
        final Application application = new ResourceConfig()
                .packages("org.glassfish.jersey.examples.jackson").register(JacksonFeature.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitOrder(2);
        //Handler package
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "com.pingwinno.presentation.management.api");
        try {
            server.start();
            server.join();
            log.debug("Start server complete");
        } catch (Exception ex) {
            log.error("Server running failed: {}", ex);

        } finally {
            server.destroy();
        }
    }

    public static void stop() throws Exception {
        server.stop();
    }

}
