package com.pingwinno;


import com.pingwinno.domain.servers.ManagementServer;
import com.pingwinno.domain.servers.TwitchServer;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;


public class Main {



    public static void main(String[] args) {

        org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());

        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("log.prop"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("start TwitchServer");
        new Thread(TwitchServer::start).start();
        log.info("start ManagementServer");
      new Thread(ManagementServer::start).start();

    }
}










