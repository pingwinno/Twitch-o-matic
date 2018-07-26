package com.pingwinno;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.infrastructure.JettyInitializationListener;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleDriveServiceInitialization;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.core.Application;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Main {

    private static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("log.prop"));

        } catch (IOException e) {
            e.printStackTrace();
        }


        log.info("Checking storage...");
        if (!StorageHelper.initialStorageCheck()) {
            System.exit(1);
        }
        GoogleDriveServiceInitialization.initialize();
        Server server = new Server(SettingsProperties.getServerPort());

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        JettyInitializationListener jettyInitializationListener = new JettyInitializationListener();
        ctx.addLifeCycleListener(jettyInitializationListener);
        ctx.setContextPath("/");
        server.setHandler(ctx);

        final Application application = new ResourceConfig()
                .packages("org.glassfish.jersey.examples.jackson").register(JacksonFeature.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitOrder(1);
        //Handler package
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "com.pingwinno.presentation");
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Server running failed: " + ex.toString(), ex);

        } finally {
            server.destroy();
        }
    }
}










