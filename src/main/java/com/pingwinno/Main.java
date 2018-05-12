package com.pingwinno;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.subscription.handler.SubscriptionModel;
import com.pingwinno.subscription.handler.SubscriptionTimer;
import com.pingwinno.subscription.handler.UserIdGetter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.core.Application;


public class Main {

    public static void main(String[] args) throws Throwable {

        Server server = new Server(SettingsProperties.getServerPort());
        //subscribe request
        UserIdGetter userIdGetter = new UserIdGetter();
        SubscriptionModel json = new SubscriptionModel("subscribe", "https://api.twitch.tv/helix/streams?user_id=" + userIdGetter.getId(SettingsProperties.getUser()), SettingsProperties.getCallbackAddress(), 864000);
        SubscriptionTimer subscriptionQuery = new SubscriptionTimer("https://api.twitch.tv/helix/webhooks/hub", json);
        System.out.println("starting sub");
        subscriptionQuery.sendRequest();
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
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
                "com.pingwinno.controllers");
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            System.out.println("Server running failed: " + ex);

        } finally {
            server.destroy();
        }
    }
}







