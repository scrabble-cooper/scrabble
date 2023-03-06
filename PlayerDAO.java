package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public class PlayerDAO extends DataAccessObject{

    private static final String GET_USER = "SELECT user_id, user_name, password " +
            "FROM users WHERE user_id=?";

    private static final String GET_USER_ID = "SELECT user_id, password " +
            "FROM users WHERE user_name=?";

    public PlayerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Player findById(long id) {
        Player player = new Player();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_USER);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                player.setPlayerId(rs.getLong("user_id"));
                player.setUserName(rs.getString("user_name"));
                player.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return player;
    }

    public Player findByUsername(String user_name) {
        Player player = new Player();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_USER_ID,TYPE_SCROLL_INSENSITIVE);) {
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();
            /*
            Should only be one return at most
             */
            if (rs.last ()) {
                if (rs.getRow() == 1) {
                    player.setPlayerId(rs.getLong("user_id"));
                    player.setPassword(rs.getString("password"));
                    player.setUserName(user_name);
                }
                else {
                    // database error multiple users with the same name
                    System.out.println("Multiple players with the same user name.");
                }
            }
            else {
                // user_name not found
                System.out.println("Username not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return player;
    }
}