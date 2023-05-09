package edu.cooper.wordsfornerds;

import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static edu.cooper.wordsfornerds.MainApp.dbconnection;

public class PlayerDAO extends DataAccessObject{

    private static final String GET_USER = "SELECT user_id, user_name, password " +
            "FROM users WHERE user_id=?";

    private static final String GET_USER_ID = "SELECT user_id, password " +
            "FROM users WHERE user_name=?";

    private static final String CREATE_USER = "INSERT INTO users (user_name , password ) VALUES (? , ?) RETURNING user_id";

    public PlayerDAO() { super(dbconnection); }

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
        try(PreparedStatement statement = this.connection.prepareStatement(GET_USER_ID);) {
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                player.setPlayerId(rs.getLong("user_id"));
                player.setPassword(rs.getString("password"));
                player.setUserName(user_name);
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

    public JSONObject createPlayer (String user_name, String password) {
        JSONObject Result = new JSONObject();
        //Player player = new Player();
        try(PreparedStatement statement = this.connection.prepareStatement(CREATE_USER);) {
            statement.setString(1, user_name);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                //player.setPlayerId(rs.getLong("user_id"));
                Result.put ("result", "true");
                Result.put ("response","Created: " + user_name );
            }
            else {
                Result.put ("result", "false");
                Result.put ("response","Failed to Create : " + user_name );
            }
        } catch (SQLException e) {
            //"SQL Exception thrown";
            //e.printStackTrace();
            Result.put ("result", "false");
            Result.put ("response", "Failed: " + user_name + ". " + e.getMessage() );

            //throw new RuntimeException(e);
        }
        return Result;
    }
}