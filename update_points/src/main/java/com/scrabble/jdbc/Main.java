package com.scrabble.jdbc;

import java.sql.*;

public class Main {

    public static void main(String... args) {
        System.out.println("Hello Learning JDBC");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "scrabble", "postgres", "password");

        try {
            Connection connection = dcm.getConnection();

            PlayerDAO playerDAO = new PlayerDAO(connection);
            Player player = playerDAO.findById(1);
            System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());

            ScoreDAO scoreDAO = new ScoreDAO(connection);
            int points = 5;
            scoreDAO.updateP1Score(12, points); // Adds on 5 points to p1_score in games table
            scoreDAO.updateP2Score(12, points); // Adds on 5 points to p2_score in games table
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}