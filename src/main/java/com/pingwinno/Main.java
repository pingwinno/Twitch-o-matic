package com.pingwinno;


import com.pingwinno.domain.servers.ManagementServer;
import com.pingwinno.domain.servers.TwitchServer;
import org.slf4j.LoggerFactory;


public class Main {


    public static void main(String[] args) {
        Main.class.getResourceAsStream("log4j2.json");

        org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());

        log.info("start TwitchServer");
        new Thread(TwitchServer::start).start();
        log.info("start ManagementServer");
        new Thread(ManagementServer::start).start();

    }
}










