package com.scrabblecooper.scrabble;
import com.scrabblecooper.scrabble.util.DataAccessObject;

import java.sql.*;


public class GameDAO extends DataAccessObject {
    private static final String GET_GAME= "SELECT * FROM games WHERE game_id=?";
//    private static final String UPDATE_GAME = "UPDATE games SET board = ?::TEXT[][] WHERE game_id=?"; //no cast needed
    private static final String UPDATE_GAME = "UPDATE games SET board = ? WHERE game_id=?";

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
}

