package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO extends DataAccessObject{

    private static final String GET_USER = "SELECT user_id, user_name, password " +
            "FROM users WHERE user_id=?";

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
}

