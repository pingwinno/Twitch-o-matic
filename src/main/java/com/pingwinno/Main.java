package com.pingwinno;


import com.pingwinno.domain.SqliteHandler;
import com.pingwinno.domain.servers.ManagementServer;
import com.pingwinno.domain.servers.TwitchServer;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class Main {


    public static void main(String[] args) {
        Main.class.getResourceAsStream("log4j2.json");

        org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());
        SqliteHandler sqliteHandler = new SqliteHandler();
        try {
            log.debug("initialize db");
            sqliteHandler.initializeDB();
        } catch (SQLException e) {
            log.error("DB initialization failed { } ", e);
        }
        log.info("start TwitchServer");
        new Thread(TwitchServer::start).start();
        log.info("start ManagementServer");
        new Thread(ManagementServer::start).start();

    }
}










