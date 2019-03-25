package com.pingwinno.domain.servers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.JettyInitializationListener;
import com.pingwinno.infrastructure.SettingsProperties;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.ws.rs.core.Application;

public class TwitchServer {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(TwitchServer.class.getName());
    private static Server server;

    public static void start() {
        server = new Server(SettingsProperties.getTwitchServerPort());

        log.debug("Running TwitchAPI server...");
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        JettyInitializationListener jettyInitializationListener = new JettyInitializationListener();
        ctx.addLifeCycleListener(jettyInitializationListener);
        ctx.setContextPath("/");

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./status");
        dataSource.setUser(SettingsProperties.getH2User());
        dataSource.setPassword(SettingsProperties.getH2Password());

        try {
            new org.eclipse.jetty.plus.jndi.Resource(server, "jdbc/jcgDS", dataSource);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        server.setHandler(ctx);

        final Application application = new ResourceConfig()
                .packages("org.glassfish.jersey.examples.jackson").register(JacksonFeature.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitOrder(1);
        //Handler package
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "com.pingwinno.presentation.twitch.api");
        try {
            server.start();
            server.join();
            log.debug("Start server complete.");
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
