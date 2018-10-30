package com.pingwinno.domain;

import com.pingwinno.infrastructure.models.StatusDataModel;
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
        String createStreamsTable = "CREATE TABLE IF NOT EXISTS streams (\n"
                + "	uuid PRIMARY KEY NOT NULL,\n"
                + "	title NOT NULL,\n"
                + "	date NOT NULL,\n"
                + " game \n"
                + ");";
        String createStreamsIndex = "CREATE [UNIQUE] INDEX uuid ON streams(uuid);";

        String createStatusTable = "CREATE TABLE IF NOT EXISTS streams_status (\n"
                + "	uuid PRIMARY KEY NOT NULL,\n"
                + "	vodId NOT NULL,\n"
                + "	date NOT NULL,\n"
                + " startedBy \n"
                + " state \n"
                + ");";
        String createStatusIndex = "CREATE [UNIQUE] INDEX uuid ON streams_status(uuid);";

        try (Connection connection = this.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createStatusTable);
            statement.execute(createStatusIndex);
            statement.execute(createStreamsTable);
            statement.execute(createStreamsIndex);
            log.info("Table create complete");
        }

    }

    public void insert( StreamDataModel streamDataModel) {
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

    public void insert(StatusDataModel statusDataModel) {
        String sqlQuery = "INSERT INTO streams_status(uuid,state,date,startedBy,vodId) VALUES(?,?,?,?,?)";
        log.info("insert streams_status data...");
        try (Connection connection = this.connect();

             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, statusDataModel.getUuid().toString());
            preparedStatement.setString(2, statusDataModel.getState().toString());
            preparedStatement.setString(3, statusDataModel.getDate());
            preparedStatement.setString(4, statusDataModel.getStartedBy().toString());
            preparedStatement.setString(5,statusDataModel.getVodId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("insert streams_status data complete");
    }

    public void update(StreamDataModel dataModel) {
        String sql = "UPDATE warehouses SET title = ? , "
                + "date = ? "
                + "game = ? "
                + "WHERE uuid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, dataModel.getTitle());
            pstmt.setString(2, dataModel.getDate());
            pstmt.setString(3, dataModel.getGame());
            pstmt.setString(4, dataModel.getUuid().toString());
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
           log.error("update failed {}",e);
        }
    }

    public void update(StatusDataModel dataModel) {
        String sql = "UPDATE warehouses SET vodId = ? , "
                + "date = ? "
                + "startedBy = ? "
                + "state = ? "
                + "WHERE uuid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, dataModel.getVodId());
            pstmt.setString(2, dataModel.getDate());
            pstmt.setString(3, dataModel.getStartedBy().toString());
            pstmt.setString(3, dataModel.getState().toString());
            pstmt.setString(5, dataModel.getUuid().toString());
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("update failed {}",e);
        }
    }

    public void delete(String table,  String column, String value) {
        String sql = "DELETE FROM "+table+" WHERE " + column + " = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, value);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("delete failed {}", e);
        }
    }

}
