package com.pingwinno;


import com.pingwinno.domain.ManagementServer;
import com.pingwinno.domain.TwitchServer;

import java.io.FileInputStream;
import java.io.IOException;
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
        log.info("start TwitchServer");
        new Thread(TwitchServer::start).start();
        log.info("start ManagementServer");
     //  new Thread(ManagementServer::start).start();

    }
}










