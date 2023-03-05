package com.scrabble.jdbc;

import java.sql.*;

public class Main {

    public static void main(String... args) {
        System.out.println("Hello Learning JDBC");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "scrabble", "postgres", "password");

        try {
            int p1Score = 0;
            Connection connection = dcm.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p1_score FROM games WHERE game_id=12");
            while(resultSet.next()){
                p1Score = resultSet.getInt(1);
                System.out.println(p1Score);
            }

            PlayerDAO playerDAO = new PlayerDAO(connection);
            Player player = playerDAO.findById(1);
            System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());

            ScoreDAO scoreDAO = new ScoreDAO(connection);
            int score = 5;
            p1Score += score;
            scoreDAO.updateP1Score(12, 2, p1Score);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}