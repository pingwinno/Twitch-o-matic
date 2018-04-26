package com.pingwinno;


import com.pingwinno.SubscriptionHandler.JsonSubObject;
import com.pingwinno.SubscriptionHandler.SubscriptionQuery;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by yar 09.09.2009
 */
        public class Main {

            public static void main(String[] args) throws Throwable {

                Server server = new Server(4856);
                //subscribe query
               JsonSubObject json = new JsonSubObject("subscribe", "https://api.twitch.tv/helix/users/follows?to_id=104717035","http://31.202.48.159:4856/handler", 0 );

                SubscriptionQuery subscriptionQuery = new SubscriptionQuery("https://api.twitch.tv/helix/webhooks/hub", json);




                ServletContextHandler ctx =
                        new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

                ctx.setContextPath("/");
                server.setHandler(ctx);

                ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
                serHol.setInitOrder(1);
                //Handler package
                serHol.setInitParameter("jersey.config.server.provider.packages",
                        "com.pingwinno.res");

                System.out.println("starting sub");
                subscriptionQuery.startSub();
                try {
                    server.start();
                    server.join();


                } catch (Exception ex) {

                } finally {

                    server.destroy();
                }


            }





}







