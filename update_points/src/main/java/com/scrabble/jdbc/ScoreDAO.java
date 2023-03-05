package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreDAO extends DataAccessObject{

    private static final String GET_P1_SCORE = "UPDATE games SET p1_score=? " +
            "WHERE p1_id=? AND game_id=?";
    private static final String GET_P2_SCORE = "UPDATE games SET p2_score=? " +
            "WHERE p2_id=? AND game_id=?";

    public ScoreDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Game findById(long id){
        Game game = new Game();
        return game;
    }

    // @Override
    public void updateP1Score(long gameID, long p1ID, int score) {
        try(PreparedStatement statement = this.connection.prepareStatement(GET_P1_SCORE);) {
            statement.setLong(1, score);
            statement.setLong(2, p1ID);
            statement.setLong(3, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateP2Score(long gameID, long p2ID, int score) {
        try(PreparedStatement statement = this.connection.prepareStatement(GET_P2_SCORE);) {
            statement.setLong(1, score);
            statement.setLong(2, p2ID);
            statement.setLong(3, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}