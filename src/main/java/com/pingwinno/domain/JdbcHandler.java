package com.pingwinno.domain;

import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.enums.StartedBy;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.UUID;

public class JdbcHandler {

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:h2:~/status", SettingsProperties.getH2User(), SettingsProperties.getH2Password());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void initializeDB() throws SQLException {

        String createStatusTable = "CREATE TABLE IF NOT EXISTS streams_status (\n"
                + " uuid UUID PRIMARY KEY NOT NULL,\n"
                + " vodId INT NOT NULL,\n"
                + " date TIMESTAMP NOT NULL,\n"
                + " startedBy VARCHAR NOT NULL,\n"
                + " user VARCHAR NOT NULL,\n"
                + " state VARCHAR NOT NULL\n"
                + ");";
        String createStatusIndex = "CREATE UNIQUE INDEX idx_status_uuid ON streams_status(uuid);";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createStatusTable);
            try {
                statement.execute(createStatusIndex);
            } catch (SQLException e) {
                log.info("Index exists");
            }
            log.info("Table create complete");
        }

    }

    public void insert(StatusDataModel statusDataModel) {
        String sqlQuery = "INSERT INTO streams_status(uuid,state,date,startedBy,user,vodId) VALUES(?,?,?,?,?,?)";
        log.info("insert streams_status data...");
        try (Connection connection = connect();

             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, statusDataModel.getUuid().toString());
            preparedStatement.setString(2, statusDataModel.getState().toString());
            preparedStatement.setString(3, statusDataModel.getDate());
            preparedStatement.setString(4, statusDataModel.getStartedBy().toString());
            preparedStatement.setString(6, statusDataModel.getVodId());
            preparedStatement.setString(5, statusDataModel.getUser());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Can't insert {}. {}", statusDataModel, e);
        }
        log.info("insert streams_status data complete");
    }

    public LinkedList<StatusDataModel> selectAll() {
        String sqlQuery = "SELECT uuid, state, date, startedBy, vodId, user FROM streams_status";
        LinkedList<StatusDataModel> streams = new LinkedList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            // loop through the result set
            while (rs.next()) {
                StatusDataModel streamDataModel =
                        new StatusDataModel(rs.getString("vodId"),
                                StartedBy.valueOf(rs.getString("startedBy")),
                                rs.getString("date"), State.valueOf(rs.getString("state")),
                                UUID.fromString(rs.getString("uuid")), rs.getString("user"));
                streams.add(streamDataModel);
            }
        } catch (SQLException e) {
            log.error("{ }", e);
        }
        return streams;
    }

    public void update(StatusDataModel dataModel) {
        String sql = "UPDATE streams_status SET vodId = ?, date = ?, startedBy = ?, user = ?, state = ? WHERE uuid = ?;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            log.trace(dataModel.getVodId());
            log.trace(dataModel.getDate());
            log.trace(dataModel.getStartedBy().toString());
            log.trace(dataModel.getState().toString());
            log.trace(dataModel.getUuid().toString());
            // set the corresponding param
            pstmt.setString(1, dataModel.getVodId());
            pstmt.setString(2, dataModel.getDate());
            pstmt.setString(3, dataModel.getStartedBy().toString());
            pstmt.setString(4, dataModel.getUser());
            pstmt.setString(5, dataModel.getState().toString());

            pstmt.setString(6, dataModel.getUuid().toString());
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("update failed {}", e);
        }
    }

    public LinkedList<String> search(String searchRow, String resultRow, String value) {
        String sql = "SELECT " + resultRow + " FROM streams_status WHERE " + searchRow + " = ?";
        LinkedList<String> searchResult = new LinkedList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, value);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                searchResult.add(rs.getString(resultRow));
            }
        } catch (SQLException e) {
            log.error(e.toString());
        }
        return searchResult;
    }

    public void delete(String uuid) {
        String sql = "DELETE FROM streams_status WHERE uuid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid);

            pstmt.executeUpdate();
            log.debug(uuid + " deleted");

        } catch (SQLException e) {
            log.error("delete failed {}", e);
        }
    }

}
