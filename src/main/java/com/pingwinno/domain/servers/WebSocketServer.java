package com.pingwinno.domain.servers;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.presentation.management.api.StreamStatusSocketApi;
import com.pingwinno.presentation.management.api.SubscriptionsSocketApi;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;

public class WebSocketServer {
    public static void start() {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(SettingsProperties.getManagementServerPort() + 1);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        try {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

            // Add StreamStatusSocketApi endpoint to javax.websocket layer
            wscontainer.addEndpoint(StreamStatusSocketApi.class);
            wscontainer.addEndpoint(SubscriptionsSocketApi.class);

            server.start();
            server.dump(System.err);
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
