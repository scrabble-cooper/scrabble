package com.scrabble.jdbc;

import java.sql.*;

public class Main {
    public static void main(String... args) {
        System.out.println("Hello Learning JDBC");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "scrabble", "postgres", "password");

        try {
            Connection connection = dcm.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT board FROM games WHERE game_id = 12");
            //Print rs

            while(resultSet.next()){
                System.out.println(resultSet.getArray(1)); // printing an array is easier w/ sql array, like this line
            }

            // Find Player
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

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
