package com.pingwinno.domain.sqlite.handlers;

import com.pingwinno.infrastructure.enums.StartedBy;
import com.pingwinno.infrastructure.enums.State;
import com.pingwinno.infrastructure.models.StatusDataModel;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.UUID;

public class SqliteStatusDataHandler extends SqliteHandler {

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public void initializeDB() throws SQLException {

        String createStatusTable = "CREATE TABLE IF NOT EXISTS streams_status (\n"
                + "	uuid PRIMARY KEY NOT NULL,\n"
                + "	vodId NOT NULL,\n"
                + "	date NOT NULL,\n"
                + " startedBy NOT NULL,\n"
                + " state NOT NULL\n"
                + ");";
        String createStatusIndex = "CREATE UNIQUE INDEX idx_status_uuid ON streams_status(uuid);";

        try (Connection connection = super.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createStatusTable);
            try {
                statement.execute(createStatusIndex);
            }
            catch (SQLException e){
                log.info("Index exists");
            }
            log.info("Table create complete");
        }

    }

    public void insert(StatusDataModel statusDataModel) {
        String sqlQuery = "INSERT INTO streams_status(uuid,state,date,startedBy,vodId) VALUES(?,?,?,?,?)";
        log.info("insert streams_status data...");
        try (Connection connection = super.connect();

             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, statusDataModel.getUuid().toString());
            preparedStatement.setString(2, statusDataModel.getState().toString());
            preparedStatement.setString(3, statusDataModel.getDate());
            preparedStatement.setString(4, statusDataModel.getStartedBy().toString());
            preparedStatement.setString(5, statusDataModel.getVodId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("insert streams_status data complete");
    }

    public LinkedList<StatusDataModel> selectAll() throws SQLException {
        String sqlQuery = "SELECT uuid, state, date, startedBy, vodId FROM streams_status";
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
                                UUID.fromString(rs.getString("uuid")));
                streams.add(streamDataModel);
            }
        } catch (SQLException e) {
            log.error("{ }", e);
        }
        return streams;
    }

    public void update(StatusDataModel dataModel) {
        String sql = "UPDATE streams_status SET vodId = ?, date = ?, startedBy = ?, state = ? WHERE uuid = ?;";

        try (Connection conn = super.connect();
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
            pstmt.setString(4, dataModel.getState().toString());
            pstmt.setString(5, dataModel.getUuid().toString());
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("update failed {}", e);
        }
    }

    public void delete(String uuid) {
        String sql = "DELETE FROM streams_status WHERE uuid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("delete failed {}", e);
        }
    }

}
