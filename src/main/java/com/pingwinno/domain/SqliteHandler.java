package com.pingwinno.domain;

import com.pingwinno.infrastructure.models.StreamDataModel;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SqliteHandler {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    private Connection connect() {
        String url = "jdbc:sqlite:" + System.getProperty("user.home") + "/db/streams.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void initializeDB() throws SQLException {

        log.info("Try to create table");
        String createTable = "CREATE TABLE IF NOT EXISTS streams (\n"
                + "	uuid PRIMARY KEY NOT NULL,\n"
                + "	title NOT NULL,\n"
                + "	date NOT NULL,\n"
                + " game \n"
                + ");";
        String createIndex = "CREATE [UNIQUE] INDEX uuid ON streams(uuid);";
        try (Connection connection = this.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createTable);
            statement.execute(createTable);
            log.info("Table create complete");
        }

    }

    public void insert(StreamDataModel streamDataModel) {
        String sqlQuery = "INSERT INTO streams(uuid,title,date,game) VALUES(?,?,?,?)";
        log.info("insert stream data...");
        try (Connection connection = this.connect();

             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, streamDataModel.getUuid().toString());
            preparedStatement.setString(2, streamDataModel.getTitle());
            preparedStatement.setString(3, streamDataModel.getDate());
            preparedStatement.setString(4, streamDataModel.getGame());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("insert stream data complete");
    }

    public void delete(String column, String value) {
        String sql = "DELETE FROM warehouses WHERE " + column + " = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, value);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("delete failed {}", e);
        }
    }

}
