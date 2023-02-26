package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PointsDAO extends DataAccessObject{

    private static final String GET_PTS = "SELECT player_score " +
         "FROM player WHERE player_id=?";

    public HandDAO(Connection connection) {
        super(connection);
    }

    // @Override
    public updatePoints(Game thisgame) {
        try(PreparedStatement statement = this.connection.prepareStatement(GET_PTS);) {
            // statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                thisgame.setP1Points(rs.getInt("player_score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return game;
    }
}

 

if (player hand not full){
    replace = 7 - hand 

}