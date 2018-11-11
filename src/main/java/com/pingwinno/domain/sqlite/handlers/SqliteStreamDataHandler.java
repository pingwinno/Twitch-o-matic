package com.pingwinno.domain.sqlite.handlers;

import com.pingwinno.infrastructure.models.StreamDataModel;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.UUID;

public class SqliteStreamDataHandler extends SqliteHandler {


    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public void initializeDB() throws SQLException {

        log.info("Try to create table");
        String createStreamsTable = "CREATE TABLE IF NOT EXISTS streams (\n"
                + "	uuid PRIMARY KEY NOT NULL,\n"
                + "	title NOT NULL,\n"
                + "	date NOT NULL,\n"
                + " game \n"
                + ");";
        String createStreamsIndex = "CREATE UNIQUE INDEX idx_stream_uuid ON streams(uuid);";
        try (Connection connection = super.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createStreamsTable);
            try {
                statement.execute(createStreamsIndex);
            }
            catch (SQLException e){
                log.info("Index exists");
            }

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

    public LinkedList<StreamDataModel> selectAll() throws SQLException {
        String sqlQuery = "SELECT uuid, title, date, game FROM streams";
        LinkedList<StreamDataModel> streams = new LinkedList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            // loop through the result set
            while (rs.next()) {
                StreamDataModel streamDataModel = new StreamDataModel(UUID.fromString(rs.getString("uuid")),
                        rs.getString("date"), rs.getString("title"), rs.getString("game"));
                streams.add(streamDataModel);
            }
        } catch (SQLException e) {
            log.error("{ }", e);
        }
        return streams;
    }


    public void update(StreamDataModel dataModel) throws SQLException {
        String sql = "UPDATE streams\n"
                + "SET title = ?, date = ?, game = ?\n"
                + "WHERE uuid = ?;";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, dataModel.getTitle());
            pstmt.setString(2, dataModel.getDate());
            pstmt.setString(3, dataModel.getGame());
            pstmt.setString(4, dataModel.getUuid().toString());
            // update
            pstmt.executeUpdate();
        }
    }

    public LinkedList<String> search(String searchRow, String resultRow){
        String sql = "SELECT " + resultRow + " FROM streams WHERE " + resultRow + " = ?";
        LinkedList<String> searchResult = new LinkedList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,searchRow);
            //
            ResultSet rs  = pstmt.executeQuery();

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
        String sql = "DELETE FROM streams WHERE uuid = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("delete failed {}", e);
        }
    }


}
