package com.scrabble.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Learning JDBC");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "tictactoe", "postgres", "password");

        try {
            Connection connection = dcm.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p1_score FROM games");
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }
            PlayerDAO playerDAO = new PlayerDAO(connection);

            Player player = playerDAO.findById(1);
            System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());

            player = new Player();
            player.setUserName("Star");
            player.setPassword("password");

            player = playerDAO.create(player);
            System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}