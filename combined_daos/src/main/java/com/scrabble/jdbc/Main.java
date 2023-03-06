package com.scrabble.jdbc;

import java.sql.*;

public class Main {

    public static void main(String... args) {
        System.out.println("Playing Scrabble: :D");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "scrabble", "postgres", "password");

        try {
            Connection connection = dcm.getConnection();

            PlayerDAO playerDAO = new PlayerDAO(connection);
            Player player = playerDAO.findById(1);
            System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());

            //Update Board
            GameDAO gameDAO = new GameDAO(connection);
            Game game = gameDAO.findById(12);
            Array sqlBoard = game.getBoard();
            String[][] javaBoard = (String[][])sqlBoard.getArray(); //cast java sql array to java array for array properties
            javaBoard[2][2] = "JDW"; // !Note javaBoard index starts at 0, but sql array starts at 1
            System.out.println(javaBoard[3][3]);
//            System.out.println(javaBoard); // cannot print java 2D array easily
            Array updatedBoard = connection.createArrayOf("text", javaBoard); //sql array
            System.out.println(updatedBoard);
            gameDAO.updateBoard(12,updatedBoard);
//            System.out.println(game.getBoard());

            int points = 5;
            gameDAO.updateP1Score(12, points); // Adds on 5 points to p1_score in games table
            gameDAO.updateP2Score(12, points); // Adds on 5 points to p2_score in games table
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}