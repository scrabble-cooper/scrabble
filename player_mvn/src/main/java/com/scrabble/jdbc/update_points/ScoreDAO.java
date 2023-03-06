package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreDAO extends DataAccessObject{
    private static final String GET_P1_SCORE = "SELECT p1_score FROM games WHERE game_id=?";
    private static final String GET_P2_SCORE = "SELECT p2_score FROM games WHERE game_id=?";
    private static final String UPDATE_P1_SCORE = "UPDATE games SET p1_score=? WHERE game_id=?";
    private static final String UPDATE_P2_SCORE = "UPDATE games SET p2_score=? WHERE game_id=?";

    public ScoreDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Game findById(long id){
        Game game = new Game();
        return game;
    }

    // @Override
    public void updateP1Score(long gameID, int points) {
        // Gets p1_score from table games
        int updated_score = 0;
        try(PreparedStatement statement = this.connection.prepareStatement(GET_P1_SCORE);) {
            statement.setLong(1, gameID);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                updated_score = rs.getInt(1) + points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Updates p1_score in table games
        try(PreparedStatement statement2 = this.connection.prepareStatement(UPDATE_P1_SCORE);) {
            statement2.setLong(1, updated_score);
            statement2.setLong(2, gameID);
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateP2Score(long gameID, int points) {
        // Gets p2_score from table games
        int updated_score = 0;
        try(PreparedStatement statement = this.connection.prepareStatement(GET_P2_SCORE);) {
            statement.setLong(1, gameID);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                updated_score = rs.getInt(1) + points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Updates p2_score in table games
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE_P2_SCORE);) {
            statement.setInt(1, updated_score);
            statement.setLong(2, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}