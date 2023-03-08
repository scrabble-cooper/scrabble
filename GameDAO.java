package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.*;

public class GameDAO extends DataAccessObject {
    private static final String GET_GAME= "SELECT * FROM games WHERE game_id=?";
//    private static final String UPDATE_GAME = "UPDATE games SET board = ?::TEXT[][] WHERE game_id=?"; //no cast needed
    private static final String UPDATE_GAME = "UPDATE games SET board = ? WHERE game_id=?";
    private static final String GET_GAME_BY_PLAYER_ID = "SELECT DISTINCT * FROM games WHERE (p1_id=? OR p2_id=?)";
    // if we did not use the word DISTINCT any game against ones self would return the same game twice
    private static final String UPDATE_WHOLE_GAME = "UPDATE games SET players_turn_id=?, passcount=?, last_update_ts = NOW(), current_round=?, " +
            "p1_score=? , p2_score=?, p1_hand=?, p2_hand=?, letters_left=?, winner=? , board = ?::TEXT[][] WHERE game_id=? ";
    // Update almost the whole table

    private static final String CREATE_GAME = "INSERT INTO games (p1_id,p2_id,players_turn_id,p1_hand,p2_hand,letters_left) " +
            "VALUES (?,?,?,?,?,?) RETURNING game_id";
//        p1ID  p2ID  PlayersTurnID p1Hand p2Hand, letterbag
    public GameDAO(Connection connection) {
        super(connection);
    }

    public long NewGame (Game game){
        // the object game has only the non default values set i.e. p1_id,p2_id,players_turn_id,p1_hand,p2_hand,letters_left
        // now create the database record of the game. The database will fill in the other fields with their default values
        // and the insert will return game_id on success

        long newGameID = -1;

        try(PreparedStatement statement = this.connection.prepareStatement(CREATE_GAME);) {
            statement.setLong(1, game.getP1ID());
            statement.setLong(2, game.getP2ID());
            statement.setLong(3, game.getPlayersTurnID());

            statement.setString(4, game.getP1Hand());
            statement.setString(5, game.getP2Hand());
            statement.setString(6, game.getLetterBag());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                newGameID = rs.getLong("game_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return newGameID;
    }

    public void updateWholeGame (Game game){
        Array updatedBoard ;
        try {
            updatedBoard = connection.createArrayOf("text", game.getJavaBoard());

            game.setBoard(connection.createArrayOf("text", game.getJavaBoard())); //sql array
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE_WHOLE_GAME);) {

            statement.setLong   (1, game.getPlayersTurnID());
            statement.setInt    (2, game.getPassCount());
            statement.setInt    (3, game.getCurrentRound());

            statement.setInt    (4, game.getP1Score());
            statement.setInt    (5, game.getP2Score());

            statement.setString (6, game.getP1Hand());
            statement.setString (7, game.getP2Hand());

            statement.setString (8, game.getLetterBag());

            statement.setInt    (9, game.getWinner());

            statement.setArray (10, updatedBoard);

            statement.setLong    (11, game.getGameId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Game findByPlayerId(long playerID){
        Game game = null;

        try(PreparedStatement statement = this.connection.prepareStatement(GET_GAME_BY_PLAYER_ID);) {
            statement.setLong(1, playerID);
            statement.setLong(2, playerID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                game = new Game();
                game.setConnection(connection);
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
                game.setPlayersTurnID (rs.getLong("players_turn_id"));
                game.setPassCount(rs.getInt("passcount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return game;
    }


    public Game findById(long id){
        Game game = new Game();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_GAME);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                game.setConnection(connection);
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
                game.setPlayersTurnID (rs.getLong("players_turn_id"));
                game.setPassCount(rs.getInt("passcount"));
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

