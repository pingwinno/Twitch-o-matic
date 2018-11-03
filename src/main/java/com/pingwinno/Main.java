package com.pingwinno;


import com.pingwinno.application.StorageHelper;
import com.pingwinno.domain.SqliteHandler;
import com.pingwinno.domain.servers.ManagementServer;
import com.pingwinno.domain.servers.TwitchServer;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class Main implements Daemon {

    static org.slf4j.Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        Main.class.getResourceAsStream("log4j2.json");

        SqliteHandler sqliteHandler = new SqliteHandler();
        try {
            log.debug("initialize db");
            sqliteHandler.initializeDB();
        } catch (SQLException e) {
            log.error("DB initialization failed { } ", e);
        }
        log.info("Checking storage...");
        if (!StorageHelper.initialStorageCheck()) {
            System.exit(1);
        }

        Thread.sleep(100);
        log.info("start TwitchServer");
        new Thread(TwitchServer::start).start();
        log.info("start ManagementServer");
        new Thread(ManagementServer::start).start();
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
        TwitchServer.stop();
        ManagementServer.stop();
    }

    @Override
    public void destroy() {
        log.info("stop");
    }
}










