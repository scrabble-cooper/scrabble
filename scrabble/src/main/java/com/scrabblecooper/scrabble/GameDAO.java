package com.scrabblecooper.scrabble;
import com.scrabblecooper.scrabble.util.DataAccessObject;

import java.sql.*;


public class GameDAO extends DataAccessObject {
    private static final String GET_GAME= "SELECT * FROM games WHERE game_id=?";
    //    private static final String UPDATE_GAME = "UPDATE games SET board = ?::TEXT[][] WHERE game_id=?"; //no cast needed
    private static final String UPDATE_GAME = "UPDATE games SET board = ? WHERE game_id=?";
    private static final String GET_P1_SCORE = "SELECT p1_score FROM games WHERE game_id=?";
    private static final String GET_P2_SCORE = "SELECT p2_score FROM games WHERE game_id=?";
    private static final String UPDATE_P1_SCORE = "UPDATE games SET p1_score=? WHERE game_id=?";
    private static final String UPDATE_P2_SCORE = "UPDATE games SET p2_score=? WHERE game_id=?";

    public GameDAO(Connection connection) {
        super(connection);
    }

    public Game findById(long id){
        Game game = new Game();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_GAME);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                game.setGameId(rs.getLong("game_id"));
                game.setP1ID(rs.getLong("p1_iD"));
                game.setP2ID(rs.getLong("p2_iD"));
                game.setP1Score(rs.getInt("p1_score"));
                game.setP2Score(rs.getInt("p2_score"));
                game.setLetterBag(rs.getString("letters_left"));
                game.setCurrentRound(rs.getInt("current_round"));
                game.setP1Hand(rs.getString("p1_hand"));
                game.setP2Hand(rs.getString("p2_hand"));
                game.setWinner(rs.getInt("winner"));
                game.setBoard(rs.getArray("board"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return game;
    }

    public void updateBoard(long gameId, Array updatedBoard) {
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE_GAME);) {
            statement.setArray(1,updatedBoard);
            statement.setLong(2, gameId);
//            System.out.println(statement); // print statement will show =? for board param, but works regardless
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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

