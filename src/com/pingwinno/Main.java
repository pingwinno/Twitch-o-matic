package com.pingwinno;


import com.pingwinno.subscription_handler.SubscriptionModel;
import com.pingwinno.subscription_handler.SubscriptionQuery;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.core.Application;


public class Main {

            public static void main(String[] args) throws Throwable {

                Server server = new Server(4856);
                //subscribe query
               SubscriptionModel json = new SubscriptionModel("subscribe", "https://api.twitch.tv/helix/users/follows?to_id=88618654","http://31.202.48.159:4856/handler", 1000);

                SubscriptionQuery subscriptionQuery = new SubscriptionQuery("https://api.twitch.tv/helix/webhooks/hub", json);

                System.out.println("starting sub");
                subscriptionQuery.startSub();


                ServletContextHandler ctx =
                        new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

                ctx.setContextPath("/");
                server.setHandler(ctx);
                final Application application = new ResourceConfig().register(JacksonFeature.class);

                ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
                serHol.setInitOrder(1);
                //Handler package
                serHol.setInitParameter("jersey.config.server.provider.packages",
                        "com.pingwinno.res");


                try {
                    server.start();
                    server.join();


                } catch (Exception ex) {

                } finally {

                    server.destroy();
                }


            }





}







