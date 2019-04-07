package com.pingwinno;


import com.pingwinno.application.JdbcHandler;
import com.pingwinno.application.RecoveryRecordHandler;
import com.pingwinno.application.StorageHelper;
import com.pingwinno.domain.MongoDBHandler;
import com.pingwinno.domain.servers.ManagementServer;
import com.pingwinno.domain.servers.TwitchServer;
import com.pingwinno.infrastructure.SettingsProperties;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;


public class Main implements Daemon {

    static org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        Main.class.getResourceAsStream("log4j2.json");

        org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());
        //use if direct connection to h2 needed
        // org.h2.tools.Server.createWebServer(new String[]{"-web","-webAllowOthers","-webPort","7071"}).start();

        if (!SettingsProperties.getMongoDBAddress().trim().equals("")) {
            log.info("Connect to MongoDB...");
            MongoDBHandler.connect();
        } else {
            log.warn("MongoDB address not set");
        }
        try {
            log.debug("initialize db");
            new JdbcHandler().initializeDB();
        } catch (SQLException e) {
            log.error("DB initialization failed {} ", e);
        }

        log.info("Checking storage...");
        try {
            if (!StorageHelper.initialStorageCheck()) {
                System.exit(1);
            }
        } catch (IOException e) {
            log.error("Checking storage failed {}", e);
        }

        Thread.sleep(100);
        log.info("start TwitchServer");
        new Thread(TwitchServer::start).start();
        if (SettingsProperties.getTwitchServerPort() != 0) {
            log.info("start ManagementServer");
            new Thread(ManagementServer::start).start();
        } else {
            log.info("Management server is disabled");
        }

        new Thread(RecoveryRecordHandler::recoverUncompletedRecordTask).start();

    }

    @Override
    public void init(DaemonContext daemonContext) {

    }

    @Override
    public void start() throws InterruptedException {
        log.info("starting...");
        main(null);
    }

    @Override
    public void stop() throws Exception {
        log.info("stopping...");
        MongoDBHandler.disconnect();
        TwitchServer.stop();
        ManagementServer.stop();
    }

    @Override
    public void destroy() {
        log.info("stop");
    }
}










