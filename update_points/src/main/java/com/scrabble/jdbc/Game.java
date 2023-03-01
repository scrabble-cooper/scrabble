package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataTransferObject;

public class Game implements DataTransferObject {

    private long playerId;
    private String userName;
    private String password;

    public long getId() {
        return gameId;
    }

    public long getPlayerId() {
        return playerId;
    }
    
    //might not need this, since its already in Player.java
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore= userScore;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}