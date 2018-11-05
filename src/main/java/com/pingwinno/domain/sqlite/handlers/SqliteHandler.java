package com.pingwinno.domain.sqlite.handlers;

import com.pingwinno.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    Connection connect() {
        String url = "jdbc:sqlite:" + SettingsProperties.getSqliteFile();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void delete(String uuid) {

    }

}
