package com.pingwinno;


import com.pingwinno.SubscriptionHandler.JsonSubObject;
import com.pingwinno.SubscriptionHandler.SubscriptionQuery;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by yar 09.09.2009
 */
        public class Main {

            public static void main(String[] args) throws Throwable {

                ServerSocket ss = new ServerSocket(4856);
               JsonSubObject json = new JsonSubObject("subscribe", "https://api.twitch.tv/helix/users/follows?to_id=104717035","http://31.202.48.159:4856", 0 );

                SubscriptionQuery subscriptionQuery = new SubscriptionQuery("https://api.twitch.tv/helix/webhooks/hub", json);






                subscriptionQuery.startSub();
               while (true) {
                    Socket s = ss.accept();
                    System.err.println("Client accepted");
                    new Thread(new SocketProcessor(s)).start();

                }


            }
    }






