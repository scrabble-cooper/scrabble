package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO extends DataAccessObject{

    private static final String GET_ONE = "SELECT player_id, user_name, password " +
            "FROM player WHERE player_id=?";

    public PlayerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Player findById(long id) {
        Player player = new Player();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                player.setPlayerId(rs.getLong("player_id"));
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

