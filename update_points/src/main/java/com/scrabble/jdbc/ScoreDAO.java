package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreDAO extends DataAccessObject{

    private static final String GET_P1_SCORE = "SELECT p1_score " +
         "FROM games WHERE p1_id=?";
    private static final String GET_P2_SCORE = "SELECT p2_score " +
         "FROM games WHERE p2_id=?";

    public ScoreDAO(Connection connection) {
        super(connection);
    }

    // @Override
    public updateP1Score(Game thisgame) {
        try(PreparedStatement statement = this.connection.prepareStatement(GET_P1_SCORE);) {
            // statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                thisgame.setP1Points(rs.getInt("p1_score"));
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return game;
    }

    public updateP2Score(Game thisgame) {
        try(PreparedStatement statement = this.connection.prepareStatement(GET_P2_SCORE);) {
            // statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                thisgame.setP1Points(rs.getInt("p2_score"));
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return game;
    }
}